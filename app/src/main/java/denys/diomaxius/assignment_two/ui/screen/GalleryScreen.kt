package denys.diomaxius.assignment_two.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import denys.diomaxius.assignment_two.ui.screen.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import denys.diomaxius.assignment_two.domain.model.ImageItem

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GalleryScreen(
    viewModel: GalleryScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val images by viewModel.images.collectAsState()

    var hasMediaPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMediaPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasMediaPermission) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    Scaffold(
        topBar = {
            TopBar()
        }
    ) { innerPadding->
        Content(
            modifier = Modifier.padding(innerPadding),
            images = images,
            viewModel = viewModel
        )
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    images: List<ImageItem>,
    viewModel: GalleryScreenViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        items(images.size) { index ->
            ImageCellThumbnail(
                uri = images[index].uri,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ImageCellThumbnail(
    uri: Uri,
    viewModel: GalleryScreenViewModel,
    sizeDp: Dp = 120.dp
) {
    val density = LocalDensity.current

    val bitmapState = produceState<Bitmap?>(initialValue = null, key1 = uri) {
        val px = with(density) { sizeDp.roundToPx() }
        value = viewModel.loadThumbnail(uri, px, px)
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
