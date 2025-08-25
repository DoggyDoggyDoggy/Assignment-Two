package denys.diomaxius.assignment_two.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.usecase.GetImageUrisUseCase
import denys.diomaxius.assignment_two.domain.usecase.LoadThumbnailUseCase
import denys.diomaxius.assignment_two.domain.usecase.MediaScannerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryScreenViewModel @Inject constructor(
    private val getImageUrisUseCase: GetImageUrisUseCase,
    private val loadThumbnailUseCase: LoadThumbnailUseCase,
    private val mediaScannerUseCase: MediaScannerUseCase
): ViewModel()  {
    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images = _images.asStateFlow()

    // Redundant
    fun scanPictures() = mediaScannerUseCase()

    fun loadImages() = viewModelScope.launch {
        _images.value = getImageUrisUseCase()
    }

    suspend fun loadThumbnail(uri: Uri, widthPx: Int, heightPx: Int): Bitmap? {
        return loadThumbnailUseCase(uri, widthPx, heightPx)
    }
}