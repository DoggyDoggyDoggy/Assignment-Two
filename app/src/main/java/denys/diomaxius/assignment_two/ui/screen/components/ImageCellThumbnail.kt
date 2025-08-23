package denys.diomaxius.assignment_two.ui.screen.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageCellThumbnail(
    modifier: Modifier = Modifier,
    uri: Uri,
    sizeDp: Dp = 100.dp,
    loadThumbnail: suspend (Uri, Int, Int) -> Bitmap?,
) {
    val density = LocalDensity.current
    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = uri) {
        val sizePx = with(density) { sizeDp.roundToPx() }
        value = loadThumbnail(uri, sizePx, sizePx)
    }

    val bmp = bitmapState.value

    if (bmp != null){
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(modifier = modifier) {
            CircularProgressIndicator()
        }
    }
}
