package denys.diomaxius.assignment_two.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import denys.diomaxius.assignment_two.ui.screen.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.ui.screen.components.ContentWithPinchToChangeColumns
import denys.diomaxius.assignment_two.ui.screen.components.ImageCellThumbnail

@Composable
fun GalleryScreen(
    viewModel: GalleryScreenViewModel
) {
    val context = LocalContext.current
    val images by viewModel.images.collectAsState()

    val mediaPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE


    var hasMediaPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                mediaPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMediaPermission = granted
        if (granted) viewModel.loadImages()
    }

    LaunchedEffect(Unit) {
        if (!hasMediaPermission) permissionLauncher.launch(mediaPermission)
        else viewModel.loadImages()
    }

    Scaffold(
        topBar = {
            TopBar()
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
    ContentWithPinchToChangeColumns { columns, prevColumns ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = modifier.fillMaxSize()
        ) {
           items(
               items = images,
               key = { it.id }
           ) { image ->
                ImageCellThumbnail(
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    uri = image.uri,
                    sizeDp = 100.dp,
                    loadThumbnail = viewModel::loadThumbnail
                )
            }
        }
    }
}