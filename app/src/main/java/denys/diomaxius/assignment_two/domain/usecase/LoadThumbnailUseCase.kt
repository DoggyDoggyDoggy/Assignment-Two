package denys.diomaxius.assignment_two.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Inject

class LoadThumbnailUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(
        uri: Uri,
        width: Int,
        height: Int,
    ): Bitmap? = imageRepository.loadThumbnail(
        uri = uri,
        width = width,
        height = height
    )
}