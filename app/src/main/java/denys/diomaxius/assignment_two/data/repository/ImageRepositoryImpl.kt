package denys.diomaxius.assignment_two.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepositoryImpl(private val context: Context) : ImageRepository {
    private val resolver: ContentResolver get() = context.contentResolver

    override suspend fun loadImageUris(): List<ImageItem> = withContext(Dispatchers.IO) {
        val uris = mutableListOf<ImageItem>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        resolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                uris += ImageItem(id = id, uri = contentUri)
            }
        }
        uris
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun loadThumbnail(
        uri: Uri,
        width: Int,
        height: Int,
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            resolver.loadThumbnail(uri, android.util.Size(width, height), null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}