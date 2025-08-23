package denys.diomaxius.assignment_two.domain.usecase

import android.util.Log
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageUrisUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(): List<ImageItem> {
        val uris = imageRepository.loadImageUris()
        Log.i("Check uris", "$uris")
        return uris
    }

}