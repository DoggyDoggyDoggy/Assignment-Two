package denys.diomaxius.assignment_two.ui.screen.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun ImageCellThumbnail(
    modifier: Modifier,
    uri: Uri,
    sizeDp: Dp,
    loadThumbnail: suspend (Uri, Int, Int) -> Bitmap?,
) {
    val density = LocalDensity.current
    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = uri) {
        val sizePx = with(density) { sizeDp.roundToPx() }
        value = loadThumbnail(uri, sizePx, sizePx)
    }

    val bmp = bitmapState.value

    Box(
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = bmp != null,
            enter = scaleIn(tween(200))
        ) {
            if (bmp != null) {
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}