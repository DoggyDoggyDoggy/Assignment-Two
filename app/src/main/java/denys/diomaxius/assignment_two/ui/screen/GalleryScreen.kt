package denys.diomaxius.assignment_two.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import denys.diomaxius.assignment_two.domain.model.ImageItem
import denys.diomaxius.assignment_two.ui.screen.components.ImageCellThumbnail
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

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
        if (granted) viewModel.loadImages()
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
    ContentWithPinchToChangeColumns {
        LazyVerticalGrid(
            columns = GridCells.Fixed(it),
            modifier = modifier.fillMaxSize()
        ) {
            items(images.size) { index ->
                ImageCellThumbnail(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp),
                    uri = images[index].uri,
                    sizeDp = 100.dp,
                    loadThumbnail = viewModel::loadThumbnail
                )
            }
        }
    }
}

@Composable
fun ContentWithPinchToChangeColumns(
    modifier: Modifier = Modifier,
    minColumns: Int = 1,
    maxColumns: Int = 6,
    content: @Composable (Int) -> Unit,
) {
    var columns by rememberSaveable { mutableStateOf(3) }
    var accumScale by remember { mutableStateOf(1f) }
    val animatedColumns by animateIntAsState(targetValue = columns)

    val zoomInThreshold = 1.80f
    val zoomOutThreshold = 0.20f
    val debounceDistance = 2f

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)

                    var prevDistance: Float? = null
                    var event: PointerEvent

                    do {
                        event = awaitPointerEvent()

                        val pressed = event.changes.filter { it.pressed }

                        if (pressed.size >= 2) {
                            pressed.forEach { it.consume() }

                            val p0 = pressed[0].position
                            val p1 = pressed[1].position

                            val dx = p0.x - p1.x
                            val dy = p0.y - p1.y
                            val dist = sqrt(dx * dx + dy * dy)

                            prevDistance?.let { prev ->
                                if (kotlin.math.abs(dist - prev) >= debounceDistance) {
                                    val zoom = if (prev > 0f) dist / prev else 1f
                                    val safeZoom = if (zoom.isFinite() && !zoom.isNaN() && zoom > 0f) zoom else 1f
                                    accumScale *= safeZoom

                                    if (accumScale > zoomInThreshold) {
                                        val next = max(minColumns, columns - 1)
                                        if (next != columns) columns = next
                                        accumScale = 1f
                                    } else if (accumScale < zoomOutThreshold) {
                                        val next = min(maxColumns, columns + 1)
                                        if (next != columns) columns = next
                                        accumScale = 1f
                                    }
                                }
                            }

                            prevDistance = dist
                        } else {
                            prevDistance = null
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
    ) {
        content(animatedColumns)
    }
}