package denys.diomaxius.assignment_two.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.domain.usecase.GetImageUrisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryScreenViewModel @Inject constructor(
    private val getImageUrisUseCase: GetImageUrisUseCase
): ViewModel()  {
    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images = _images.asStateFlow()

    init {
        loadImages()
    }

    private fun loadImages() = viewModelScope.launch {
        _images.value = getImageUrisUseCase()
    }
}