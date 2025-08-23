package denys.diomaxius.assignment_two.ui.screen.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageCellThumbnail(
    uri: Uri,
    sizeDp: Dp = 120.dp,
    loadThumbnail: suspend (Uri, Int, Int) -> Bitmap?,
) {
    val density = LocalDensity.current

    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = uri) {
        val px = with(density) { sizeDp.roundToPx() }
        value = loadThumbnail(uri, px, px)
    }

    Card(modifier = Modifier.size(sizeDp)) {
        val bmp = bitmapState.value
        if (bmp == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("…")
            }
        } else {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}