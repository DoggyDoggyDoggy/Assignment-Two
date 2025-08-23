package denys.diomaxius.assignment_two

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import denys.diomaxius.assignment_two.ui.screen.GalleryScreen
import denys.diomaxius.assignment_two.ui.theme.Assignment_twoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_twoTheme {
                GalleryScreen()
            }
        }
    }
}