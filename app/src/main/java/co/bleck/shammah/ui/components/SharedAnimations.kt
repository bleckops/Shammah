package co.bleck.shammah.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Staggered entrance animation. Each element enters with a fade + slide-up
 * after [index] * [delayPerItem] ms.
 */
@Composable
fun StaggeredEntrance(
    index: Int,
    delayPerItem: Long = 110L,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * delayPerItem)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(480)) + slideInVertically(
            animationSpec = tween(480, easing = LinearOutSlowInEasing),
            initialOffsetY = { 56 }
        ),
        exit = fadeOut(),
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

/**
 * Shimmer loading placeholder. Shows a horizontally sweeping shimmer gradient
 * in a rounded rectangle of the given [height].
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    height: Dp = 180.dp,
    cornerRadius: Dp = 20.dp
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.25f),
        Color.LightGray.copy(alpha = 0.55f),
        Color.LightGray.copy(alpha = 0.25f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 300f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

/**
 * Applies a shimmer sweep to any Modifier — useful for applying to existing shapes.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_modifier")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_modifier_translate"
    )
    background(
        Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.22f),
                Color.LightGray.copy(alpha = 0.50f),
                Color.LightGray.copy(alpha = 0.22f)
            ),
            start = Offset(translateAnim - 300f, 0f),
            end = Offset(translateAnim, 0f)
        )
    )
}
