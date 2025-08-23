package denys.diomaxius.assignment_two.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import denys.diomaxius.assignment_two.domain.usecase.GetImageUrisUseCase
import javax.inject.Inject

@HiltViewModel
class GalleryScreenViewModel @Inject constructor(
    private val getImageUrisUseCase: GetImageUrisUseCase
): ViewModel()  {

}