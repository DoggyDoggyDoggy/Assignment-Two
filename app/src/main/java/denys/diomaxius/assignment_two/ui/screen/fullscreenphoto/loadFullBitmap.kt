package denys.diomaxius.assignment_two.ui.screen.fullscreenphoto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun loadFullBitmap(context: Context, uriString: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val uri = uriString.toUri()
            val resolver = context.contentResolver

            resolver.openFileDescriptor(uri, "r")?.use {
                return@withContext BitmapFactory.decodeFileDescriptor(
                    it.fileDescriptor,
                    null,
                    BitmapFactory.Options()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
        return@withContext null
    }