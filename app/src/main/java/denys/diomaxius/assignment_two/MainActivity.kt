package denys.diomaxius.assignment_two

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import denys.diomaxius.assignment_two.ui.screen.gallery.GalleryScreen
import denys.diomaxius.assignment_two.ui.screen.gallery.GalleryScreenViewModel
import denys.diomaxius.assignment_two.ui.theme.Assignment_twoTheme

//Used Hilt to make it easier to make a cleaner project architecture and follow the MVVM pattern.
//No need to write "custom" viewmodel factories.

//The implementation of full screen photo itself can be made cleaner in terms
//of architecture and LoadFullBitmap can be moved out of the UI layer.
//But for this project it will be redundant.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val galleryViewModel: GalleryScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_twoTheme {
                GalleryScreen(galleryViewModel)
            }
        }
    }

    //Reload the images if the image was deleted or photo was taken
    override fun onResume() {
        super.onResume()
        galleryViewModel.loadImages()
    }
}