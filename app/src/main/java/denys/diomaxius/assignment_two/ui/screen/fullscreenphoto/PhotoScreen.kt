package denys.diomaxius.assignment_two.ui.screen.fullscreenphoto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun PhotoScreen(uri: String) {
    val context = LocalContext.current

    val bitmapState = produceState<Bitmap?>(initialValue = null, uri) {
        value = loadFullBitmapViaBitmapFactory(context, uri)
    }

    val bmp = bitmapState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

suspend fun loadFullBitmapViaBitmapFactory(context: Context, uriString: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val uri = uriString.toUri()
            val resolver = context.contentResolver

            resolver.openFileDescriptor(uri, "r")?.use {
                return@withContext BitmapFactory.decodeFileDescriptor(
                    it.fileDescriptor,
                    null,
                    BitmapFactory.Options()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
        return@withContext null
    }