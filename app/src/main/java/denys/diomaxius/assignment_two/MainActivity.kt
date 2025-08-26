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

    override fun onResume() {
        super.onResume()
        galleryViewModel.loadImages()
    }
}