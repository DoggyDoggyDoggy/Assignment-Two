package denys.diomaxius.assignment_two.domain.usecase

import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Inject

class ClearThumbnailCacheUseCase  @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke() = imageRepository.clearThumbnailCache()
}