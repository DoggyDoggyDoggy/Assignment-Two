package denys.diomaxius.assignment_two.domain.repository

import denys.diomaxius.assignment_two.domain.model.ImageItem

interface ImageRepository {
    suspend fun loadImageUris(): List<ImageItem>
}