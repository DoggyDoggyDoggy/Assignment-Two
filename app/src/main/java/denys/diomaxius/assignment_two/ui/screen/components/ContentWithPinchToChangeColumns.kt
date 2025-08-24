package denys.diomaxius.assignment_two.ui.screen.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.max
import kotlin.math.min

fun distance(p0: Offset, p1: Offset): Float {
    val dx = p0.x - p1.x
    val dy = p0.y - p1.y
    return kotlin.math.sqrt(dx * dx + dy * dy)
}

fun safeZoom(zoom: Float): Float =
    if (zoom.isFinite() && !zoom.isNaN() && zoom > 0f) zoom else 1f

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

    fun maybeUpdateColumns(accum: Float) {
        if (accum > zoomInThreshold) {
            val next = max(minColumns, columns - 1)
            if (next != columns) columns = next
            accumScale = 1f
        } else if (accum < zoomOutThreshold) {
            val next = min(maxColumns, columns + 1)
            if (next != columns) columns = next
            accumScale = 1f
        }
    }

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

                            val dist = distance(pressed[0].position, pressed[1].position)

                            prevDistance?.let { prev ->
                                if (kotlin.math.abs(dist - prev) >= debounceDistance) {
                                    val zoom = safeZoom(if (prev > 0f) dist / prev else 1f)
                                    accumScale *= zoom

                                    maybeUpdateColumns(accumScale)
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