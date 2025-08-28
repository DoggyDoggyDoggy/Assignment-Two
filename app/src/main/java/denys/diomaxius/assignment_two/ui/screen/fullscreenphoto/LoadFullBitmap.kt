package denys.diomaxius.assignment_two.ui.screen.fullscreenphoto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import denys.diomaxius.assignment_two.utils.applyExifRotation
import denys.diomaxius.assignment_two.utils.calculateInSampleSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Loads the image based on the phone screen size. The image will not be of maximum quality.
//But much better than thumbnails. At maximum quality, the memory overflow error occurs
suspend fun loadFullBitmap(context: Context, uriString: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val uri = uriString.toUri()

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }

            val displayMetrics = context.resources.displayMetrics
            val reqWidth = displayMetrics.widthPixels
            val reqHeight = displayMetrics.heightPixels

            val sampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            val finalOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inScaled = false
            }

            val bitmap = context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, finalOptions)
            }

            bitmap?.let { applyExifRotation(context, uri, it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }