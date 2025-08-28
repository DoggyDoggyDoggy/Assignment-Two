package denys.diomaxius.assignment_two.domain.usecase

import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Inject

//UseCase, which is optional in this case, simply serves as an additional layer between the ViewModel and the repository.
class GetImageUrisUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(): List<ImageItem> =
        imageRepository.loadImageUris()
}