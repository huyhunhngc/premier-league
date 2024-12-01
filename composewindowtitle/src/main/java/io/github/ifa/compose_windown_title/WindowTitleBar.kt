package io.github.ifa.compose_windown_title

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import java.awt.Rectangle
import java.awt.Shape
import java.awt.Window
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

object HitSpots {
    const val NO_HIT_SPOT = 0
    const val OTHER_HIT_SPOT = 1
    const val MINIMIZE_BUTTON = 2
    const val MAXIMIZE_BUTTON = 3
    const val CLOSE_BUTTON = 4
    const val MENU_BAR = 5
    const val DRAGGABLE_AREA = 6
}


private val LocalWindowHitSpots =
    compositionLocalOf<MutableMap<Any, Pair<Rectangle, Int>>> { error("LocalWindowHitSpots not provided") }

@Composable
private fun FrameWindowScope.getCurrentWindowSize(): DpSize {
    var windowSize by remember {
        mutableStateOf(DpSize(window.width.dp, window.height.dp))
    }
    //observe window size
    DisposableEffect(window) {
        val listener = object : ComponentAdapter() {
            override fun componentResized(p0: ComponentEvent?) {
                windowSize = DpSize(window.width.dp, window.height.dp)
            }
        }
        window.addComponentListener(listener)
        onDispose {
            window.removeComponentListener(listener)
        }
    }
    return windowSize
}


@Composable
private fun Modifier.onPositionInRect(
    onChange: (Rectangle) -> Unit,
) = composed {
    val density = LocalDensity.current
    onGloballyPositioned {
        onChange(
            it.positionInWindow().toDpRectangle(
                width = it.size.width,
                height = it.size.height,
                density = density
            )
        )
    }
}


private fun Offset.toDpRectangle(
    width: Int,
    height: Int,
    density: Density,
): Rectangle {
    val offset = this
    return Rectangle(
        (offset.x).toAwtUnitSize(density),
        offset.y.toAwtUnitSize(density),
        width.toAwtUnitSize(density),
        height.toAwtUnitSize(density),
    )
}


private fun Int.toAwtUnitSize(density: Density) = with(density) { toDp().value.toInt() }

private fun Float.toAwtUnitSize(density: Density) = with(density) { toDp().value.toInt() }


private fun placeHitSpots(
    window: Window,
    spots: Map<Shape, Int>,
    height: Int,
) {
    CustomWindowDecorationAccessing.setCustomDecorationEnabled(window, true)
    CustomWindowDecorationAccessing.setCustomDecorationTitleBarHeight(
        window,
        height,
    )
    CustomWindowDecorationAccessing.setCustomDecorationHitTestSpotsMethod(window, spots)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FrameWindowScope.ProvideWindowSpotContainer(
    windowState: WindowState,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val windowSize = getCurrentWindowSize()
    val containerSize = with(density) {
        LocalWindowInfo.current.containerSize.let {
            DpSize(it.width.toDp(), it.height.toDp())
        }
    }
    // we pass it to composition local provider
    val spotInfoState = remember {
        mutableStateMapOf<Any, Pair<Rectangle, Int>>()
    }
    var toolbarHeight by remember {
        mutableStateOf(0)
    }

    val spotsWithInfo = spotInfoState.toMap()
    var shouldRestorePlacement by remember(window) {
        mutableStateOf(true)
    }
    //if any of this keys change we will re position hit spots
    LaunchedEffect(
        spotsWithInfo,
        toolbarHeight,
        window,
        windowSize,
        containerSize,
    ) {
        //
        if (CustomWindowDecorationAccessing.isSupported) {
            val startOffset = (windowSize - containerSize) / 2
            val startWidthOffsetInDp = startOffset.width.value.toInt()
//          val startHeightInDp=delta.height.value.toInt() //it seems no need here
            val spots: Map<Shape, Int> = spotsWithInfo.values.associate { (rect, spot) ->
                Rectangle(rect.x + startWidthOffsetInDp, rect.y, rect.width, rect.height) to spot
            }
            //it seems after activating hit spots window class will change its placement
            //we only want to restore placement whe windows is loaded for first time
            if (shouldRestorePlacement) {
                //this block only called once for each window
                val lastPlacement = windowState.placement
                placeHitSpots(window, spots, toolbarHeight)
                window.placement = lastPlacement
                shouldRestorePlacement = false
            } else {
                placeHitSpots(window, spots, toolbarHeight)
            }

        }
    }
    CompositionLocalProvider(
        LocalWindowHitSpots provides spotInfoState
    ) {
        Box(Modifier.onGloballyPositioned {
            toolbarHeight = it.size.height.toAwtUnitSize(density)
        }) {
            content()
        }
    }
}

@Composable
fun Modifier.windowFrameItem(
    key: Any,
    spot: Int,
) = composed {
    var shape by remember(key) {
        mutableStateOf(null as Rectangle?)
    }
    val localWindowSpots = LocalWindowHitSpots.current
    DisposableEffect(shape, key) {
        shape.let { shape ->
            if (shape != null) {
                localWindowSpots[key] = shape to spot
                onDispose {
                    localWindowSpots.remove(key)
                }
            } else {
                onDispose {}
            }
        }
    }
    onPositionInRect { shape = it }
}