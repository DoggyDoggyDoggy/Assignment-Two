package denys.diomaxius.assignment_two.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import denys.diomaxius.assignment_two.domain.model.ImageItem

interface ImageRepository {
    suspend fun loadImageUris(): List<ImageItem>
    suspend fun loadThumbnail(uri: Uri, width: Int, height: Int): Bitmap?
}