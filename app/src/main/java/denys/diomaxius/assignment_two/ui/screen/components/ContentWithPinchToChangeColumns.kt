package denys.diomaxius.assignment_two.ui.screen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt

@Composable
fun ContentWithPinchToChangeColumns(
    modifier: Modifier = Modifier,
    minColumns: Int = 1,
    maxColumns: Int = 6,
    initialColumns: Int = 3,
    content: @Composable (columns: Int, prevColumns: Int) -> Unit,
) {
    var columns by rememberSaveable {
        mutableStateOf(
            initialColumns.coerceIn(minColumns, maxColumns)
        )
    }
    var prevColumns by remember { mutableStateOf(columns) }

    LaunchedEffect(columns) {
        prevColumns = columns
    }

    var zoom by remember { mutableStateOf(1f) }
    val animatedZoom by animateFloatAsState(
        targetValue = zoom,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = animatedZoom
                scaleY = animatedZoom
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    do {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        val zoomChange = event.calculateZoom()
                        if (zoomChange != 1f) {
                            zoom *= zoomChange
                            if (zoom != 0f) {
                                val nextCols = (columns / zoom).roundToInt()
                                    .coerceIn(minColumns, maxColumns)
                                if (nextCols != columns) {
                                    columns = nextCols
                                    zoom = 1f
                                }
                            }
                            event.changes.forEach { it.consume() }
                        }
                    } while (event.changes.any { it.pressed })
                    zoom = 1f
                }
            }
    ) {
        content(columns, prevColumns)
    }
}