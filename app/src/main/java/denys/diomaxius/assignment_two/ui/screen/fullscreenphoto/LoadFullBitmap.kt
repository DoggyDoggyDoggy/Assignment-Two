package denys.diomaxius.assignment_two.ui.screen.fullscreenphoto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import denys.diomaxius.assignment_two.utils.applyExifRotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun loadFullBitmap(context: Context, uriString: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val uri = uriString.toUri()

            val bitmap = context.contentResolver.openFileDescriptor(uri, "r")?.use {
                BitmapFactory.decodeFileDescriptor(
                    it.fileDescriptor,
                    null,
                    BitmapFactory.Options()
                )
            }

            bitmap?.let { applyExifRotation(context, uri, it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }