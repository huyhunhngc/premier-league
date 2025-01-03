package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import io.github.ifa.compose_windown_title.CustomWindowDecorationAccessing
import io.github.ifa.compose_windown_title.HitSpots
import io.github.ifa.compose_windown_title.ProvideWindowSpotContainer
import io.github.ifa.compose_windown_title.windowFrameItem

@Composable
private fun FrameWindowScope.CustomWindowFrame(
    onRequestMinimize: (() -> Unit)?,
    onRequestClose: () -> Unit,
    onRequestToggleMaximize: (() -> Unit)?,
    title: String,
    windowIcon: ImageVector? = null,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    center: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides colorScheme.onBackground.copy(0.75f)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(colorScheme.surface)
        ) {
            SnapDraggableToolbar(
                title = title,
                windowIcon = windowIcon,
                center = center,
                onRequestMinimize = onRequestMinimize,
                onRequestClose = onRequestClose,
                onRequestToggleMaximize = onRequestToggleMaximize,
                windowState = LocalWindowState.current
            )
            content()
        }
    }
}

@Composable
fun isWindowFocused(): Boolean {
    return LocalWindowInfo.current.isWindowFocused
}

@Composable
fun isWindowMaximized(): Boolean {
    return LocalWindowState.current.placement == WindowPlacement.Maximized
}

@Composable
fun isWindowFloating(): Boolean {
    return LocalWindowState.current.placement == WindowPlacement.Floating
}

@Composable
fun FrameWindowScope.SnapDraggableToolbar(
    title: String,
    windowIcon: ImageVector? = null,
    center: @Composable () -> Unit,
    onRequestMinimize: (() -> Unit)?,
    onRequestToggleMaximize: (() -> Unit)?,
    onRequestClose: () -> Unit,
    windowState: WindowState,
) {
    ProvideWindowSpotContainer(
        windowState = windowState
    ) {
        if (CustomWindowDecorationAccessing.isSupported) {
            FrameContent(
                title,
                windowIcon,
                center,
                onRequestMinimize,
                onRequestToggleMaximize,
                onRequestClose
            )
        } else {
            WindowDraggableArea {
                FrameContent(
                    title,
                    windowIcon,
                    center,
                    onRequestMinimize,
                    onRequestToggleMaximize,
                    onRequestClose
                )
            }
        }
    }
}

@Composable
private fun FrameWindowScope.FrameContent(
    title: String,
    windowIcon: ImageVector? = null,
    center: @Composable () -> Unit,
    onRequestMinimize: (() -> Unit)?,
    onRequestToggleMaximize: (() -> Unit)?,
    onRequestClose: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(24.dp))
        windowIcon?.let {
            Icon(it, null)
            Spacer(Modifier.width(8.dp))
        }

        Text(
            title, maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            modifier = Modifier
                .windowFrameItem("title", HitSpots.DRAGGABLE_AREA)
        )

        Box(Modifier.weight(1f)) {
            center()
        }
        WindowsActionButtons(
            onRequestClose,
            onRequestMinimize,
            onRequestToggleMaximize,
        )
    }
}

@Composable
fun CustomWindow(
    state: WindowState,
    onCloseRequest: () -> Unit,
    onRequestMinimize: (() -> Unit)? = {
        state.isMinimized = true
    },
    onRequestToggleMaximize: (() -> Unit)? = {
        if (state.placement == WindowPlacement.Maximized) {
            state.placement = WindowPlacement.Floating
        } else {
            state.placement = WindowPlacement.Maximized
        }
    },
    defaultTitle: String = "Untitled",
    defaultIcon: Painter? = null,
    colorScheme: ColorScheme,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    //two-way binding
    val windowController = remember {
        WindowController()
    }
    val center = windowController.center ?: {}

    val transparent: Boolean
    val undecorated: Boolean
    val isAeroSnapSupported = CustomWindowDecorationAccessing.isSupported
    if (isAeroSnapSupported) {
        //we use aero snap
        transparent = false
        undecorated = false
    } else {
        //we decorate window and add our custom layout
        transparent = true
        undecorated = true
    }
    Window(
        state = state,
        transparent = transparent,
        undecorated = undecorated,
        icon = defaultIcon,
        onCloseRequest = onCloseRequest,
    ) {
        val title = windowController.title ?: defaultTitle
        LaunchedEffect(title) {
            window.title = title
        }
        CompositionLocalProvider(
            LocalWindowController provides windowController,
            LocalWindowState provides state,
        ) {
            val icon by rememberUpdatedState(windowController.icon)
            val onIconClick by rememberUpdatedState(windowController.onIconClick)
            // a window frame which totally rendered with compose
            CustomWindowFrame(
                onRequestMinimize = onRequestMinimize,
                onRequestClose = onCloseRequest,
                onRequestToggleMaximize = onRequestToggleMaximize,
                title = title,
                windowIcon = icon,
                colorScheme = colorScheme,
                center = { center() }
            ) {
                content()
            }
        }
    }
}


class WindowController {
    var title by mutableStateOf(null as String?)
    var icon by mutableStateOf(null as ImageVector?)
    var onIconClick by mutableStateOf(null as (() -> Unit)?)
    var center: (@Composable () -> Unit)? by mutableStateOf(null)
}

private val LocalWindowController =
    compositionLocalOf<WindowController> { error("window controller not provided") }
private val LocalWindowState =
    compositionLocalOf<WindowState> { error("window controller not provided") }

@Composable
fun WindowCenter(content: @Composable () -> Unit) {
    val c = LocalWindowController.current
    c.center = content
    DisposableEffect(Unit) {
        onDispose {
            c.center = null
        }
    }
}

@Composable
fun WindowTitle(title: String) {
    val c = LocalWindowController.current
    LaunchedEffect(title) {
        c.title = title
    }
    DisposableEffect(Unit) {
        onDispose {
            c.title = null
        }
    }
}

@Composable
fun WindowIcon(icon: ImageVector, onClick: () -> Unit) {
    val current = LocalWindowController.current
    DisposableEffect(icon) {
        current.let {
            it.icon = icon
            it.onIconClick = onClick
        }
        onDispose {
            current.let {
                it.icon = null
                it.onIconClick = null
            }
        }
    }
}