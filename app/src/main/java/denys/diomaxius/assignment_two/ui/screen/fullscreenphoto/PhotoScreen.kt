package denys.diomaxius.assignment_two.ui.screen.fullscreenphoto

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@Composable
fun PhotoScreen(uri: String) {
    val context = LocalContext.current

    val bitmapState = produceState<Bitmap?>(initialValue = null, uri) {
        value = loadFullBitmap(context, uri)
    }

    val bmp = bitmapState.value

    PinchToZoomView{
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}