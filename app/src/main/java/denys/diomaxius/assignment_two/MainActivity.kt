package denys.diomaxius.assignment_two

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import denys.diomaxius.assignment_two.ui.screen.GalleryScreen
import denys.diomaxius.assignment_two.ui.screen.GalleryScreenViewModel
import denys.diomaxius.assignment_two.ui.theme.Assignment_twoTheme

class MainActivity : ComponentActivity() {
    val viewModel = GalleryScreenViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_twoTheme {
                GalleryScreen(viewModel = viewModel)
            }
        }
    }
}