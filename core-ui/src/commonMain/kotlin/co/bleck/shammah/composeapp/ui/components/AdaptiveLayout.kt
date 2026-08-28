package co.bleck.shammah.composeapp.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Width buckets used for laptop/tablet adaptations of the shared UI.
 * Compact keeps the existing phone layout; larger buckets center and cap content.
 */
enum class WindowWidthClass {
    Compact,
    Medium,
    Expanded,
}

@Immutable
data class AdaptiveMetrics(
    val widthClass: WindowWidthClass,
    val maxWidth: Dp,
    val contentMaxWidth: Dp,
    val compactContentMaxWidth: Dp,
) {
    val useNavigationRail: Boolean get() = widthClass == WindowWidthClass.Expanded
    val isWide: Boolean get() = widthClass != WindowWidthClass.Compact
}

@Composable
fun rememberAdaptiveMetrics(maxWidth: Dp): AdaptiveMetrics {
    return remember(maxWidth) {
        val widthClass = when {
            maxWidth >= 840.dp -> WindowWidthClass.Expanded
            maxWidth >= 600.dp -> WindowWidthClass.Medium
            else -> WindowWidthClass.Compact
        }
        val contentMaxWidth = when (widthClass) {
            WindowWidthClass.Expanded -> 880.dp.coerceAtMost(maxWidth - 120.dp)
            WindowWidthClass.Medium -> 640.dp.coerceAtMost(maxWidth - 48.dp)
            WindowWidthClass.Compact -> maxWidth
        }
        AdaptiveMetrics(
            widthClass = widthClass,
            maxWidth = maxWidth,
            contentMaxWidth = contentMaxWidth,
            compactContentMaxWidth = 440.dp.coerceAtMost(maxWidth - 40.dp)
        )
    }
}

@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.(AdaptiveMetrics) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        content(rememberAdaptiveMetrics(maxWidth))
    }
}

fun Modifier.adaptiveContentWidth(metrics: AdaptiveMetrics): Modifier =
    this.widthIn(max = metrics.contentMaxWidth)

fun Modifier.adaptiveAuthWidth(metrics: AdaptiveMetrics): Modifier =
    this.widthIn(max = metrics.compactContentMaxWidth)
