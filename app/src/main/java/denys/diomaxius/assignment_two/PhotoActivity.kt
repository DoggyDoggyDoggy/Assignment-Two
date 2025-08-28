package denys.diomaxius.assignment_two

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import denys.diomaxius.assignment_two.ui.screen.fullscreenphoto.PhotoScreen

//Activity for single photo screen
class PhotoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoUri = intent.getStringExtra("photoUri")

        setContent {
            if (photoUri != null) {
                PhotoScreen(photoUri)
            }
        }
    }
}