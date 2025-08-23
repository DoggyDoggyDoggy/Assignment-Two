package denys.diomaxius.assignment_two.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import denys.diomaxius.assignment_two.ui.screen.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.ui.screen.components.ImageCellThumbnail

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GalleryScreen(
    viewModel: GalleryScreenViewModel = hiltViewModel(),
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
            TopBar(
                scanPictures = viewModel::scanPictures,
            )
        }
    ) { innerPadding ->
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
    viewModel: GalleryScreenViewModel,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        items(images.size) { index ->
            ImageCellThumbnail(
                uri = images[index].uri,
                loadThumbnail = viewModel::loadThumbnail
            )
        }
    }
}