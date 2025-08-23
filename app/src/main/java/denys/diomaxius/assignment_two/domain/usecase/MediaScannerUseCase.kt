package denys.diomaxius.assignment_two.domain.usecase

import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Inject


class MediaScannerUseCase @Inject constructor(
    private val repository: ImageRepository
)  {
    operator fun invoke() = repository.scanPictures()
}