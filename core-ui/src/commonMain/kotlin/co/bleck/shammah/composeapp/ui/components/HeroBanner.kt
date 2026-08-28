package co.bleck.shammah.composeapp.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Church
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun HeroBanner(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    parallaxScale: Float = 1f,
    height: Dp = 260.dp
) {
    val primary   = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = parallaxScale; scaleY = parallaxScale }
            .height(height)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        primary,
                        primary.copy(alpha = 0.82f),
                        secondary.copy(alpha = 0.22f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LakesideBackdrop(accent = onPrimary)

        LakeScene(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            secondary = secondary,
            onPrimary = onPrimary
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier
                .offset(y = (-14).dp)
                .padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(onPrimary.copy(alpha = 0.12f))
                    .border(1.5.dp, secondary.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Church,
                    contentDescription = "Shammah Logo",
                    tint = onPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text      = title,
                style     = MaterialTheme.typography.headlineLarge.copy(
                    shadow = Shadow(
                        color      = primary.copy(alpha = 0.55f),
                        offset     = Offset(0f, 2f),
                        blurRadius = 8f
                    )
                ),
                color     = onPrimary,
                textAlign = TextAlign.Center
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text      = subtitle,
                    style     = MaterialTheme.typography.titleMedium.copy(
                        fontStyle = FontStyle.Italic,
                        shadow    = Shadow(
                            color      = primary.copy(alpha = 0.5f),
                            offset     = Offset(0f, 1f),
                            blurRadius = 6f
                        )
                    ),
                    color     = onPrimary.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun LakesideBackdrop(accent: Color) {
    val transition = rememberInfiniteTransition(label = "backdrop")
    val sway by transition.animateFloat(
        initialValue  = -1f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tree_sway"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val groundY = h * 0.8f
        val swayPx  = sway * 4.dp.toPx()

        drawPine(baseX = w * 0.09f, groundY = groundY, height = h * 0.46f, sway = swayPx, color = accent)
        drawPine(baseX = w * 0.21f, groundY = groundY + h * 0.03f, height = h * 0.3f, sway = swayPx * 0.7f, color = accent)
        drawPine(baseX = w * 0.91f, groundY = groundY, height = h * 0.48f, sway = -swayPx, color = accent)
        drawPine(baseX = w * 0.79f, groundY = groundY + h * 0.03f, height = h * 0.28f, sway = -swayPx * 0.7f, color = accent)
    }
}

private fun DrawScope.drawPine(
    baseX: Float,
    groundY: Float,
    height: Float,
    sway: Float,
    color: Color
) {
    val trunkW = height * 0.06f
    val trunkH = height * 0.14f
    drawRect(
        color   = color.copy(alpha = 0.4f),
        topLeft = Offset(baseX - trunkW / 2f, groundY - trunkH),
        size    = Size(trunkW, trunkH)
    )

    val foliageBase   = groundY - trunkH
    val foliageHeight = height * 0.86f
    val tiers         = 3
    val baseHalf      = height * 0.27f

    for (i in 0 until tiers) {
        val frac        = i.toFloat() / tiers
        val nextFrac    = (i + 1).toFloat() / tiers
        val tierBottomY = foliageBase - foliageHeight * frac
        val tierTopY    = foliageBase - foliageHeight * (nextFrac + 0.12f)
        val halfW       = baseHalf * (1f - frac * 0.78f)
        val apexX       = baseX + sway * (frac + 0.4f)

        val tier = Path().apply {
            moveTo(baseX - halfW, tierBottomY)
            lineTo(apexX, tierTopY)
            lineTo(baseX + halfW, tierBottomY)
            close()
        }
        drawPath(path = tier, color = color.copy(alpha = 0.08f))
        drawPath(path = tier, color = color.copy(alpha = 0.34f), style = Stroke(width = 2f))
    }
}

@Composable
private fun LakeScene(
    modifier: Modifier = Modifier,
    secondary: Color,
    onPrimary: Color
) {
    val transition = rememberInfiniteTransition(label = "lake")

    val wavePhase by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation  = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )
    val rippleProgress by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )
    val fish1 by transition.animateFloat(
        initialValue  = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Restart),
        label = "fish1"
    )
    val fish2 by transition.animateFloat(
        initialValue  = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(19000, easing = LinearEasing), RepeatMode.Restart),
        label = "fish2"
    )
    val fish3 by transition.animateFloat(
        initialValue  = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(23000, easing = LinearEasing), RepeatMode.Restart),
        label = "fish3"
    )
    val bob by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(3400, easing = LinearEasing), RepeatMode.Restart),
        label = "bob"
    )

    val shallowWater = lerp(secondary, Color(0xFF9BD7E6), 0.55f)
    val midWater     = lerp(secondary, Color(0xFF3E93B5), 0.6f)
    val deepWater    = lerp(secondary, Color(0xFF0F546C), 0.7f)
    val fishColor    = onPrimary.copy(alpha = 0.5f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val wavelength = w / 1.3f
        val baseY = h * 0.16f
        val amp   = h * 0.07f

        fun shoreY(x: Float): Float =
            baseY + amp * sin((x / wavelength) * 2f * PI.toFloat() + wavePhase)

        val steps = 48

        val water = Path().apply {
            moveTo(0f, shoreY(0f))
            for (i in 1..steps) {
                val x = w * i / steps
                lineTo(x, shoreY(x))
            }
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path  = water,
            brush = Brush.verticalGradient(
                colors = listOf(shallowWater, midWater, deepWater),
                startY = baseY,
                endY   = h
            )
        )

        val shoreLine = Path().apply {
            moveTo(0f, shoreY(0f))
            for (i in 1..steps) {
                val x = w * i / steps
                lineTo(x, shoreY(x))
            }
        }
        drawPath(
            path  = shoreLine,
            color = onPrimary.copy(alpha = 0.45f),
            style = Stroke(width = 2f)
        )

        for (c in 1..2) {
            val offsetY = c * h * 0.24f
            val contour = Path().apply {
                moveTo(0f, shoreY(0f) + offsetY)
                for (i in 1..steps) {
                    val x = w * i / steps
                    lineTo(x, shoreY(x) + offsetY)
                }
            }
            drawPath(
                path  = contour,
                color = onPrimary.copy(alpha = 0.08f),
                style = Stroke(width = 1.5f)
            )
        }

        val yFar  = h * 0.96f
        val yNear = baseY + amp + h * 0.06f
        for (r in 0 until 3) {
            val t = (rippleProgress + r / 3f) % 1f
            val y = yFar + (yNear - yFar) * t
            val fade = sin(t * PI).toFloat()
            val ripple = Path().apply {
                moveTo(0f, y)
                for (i in 1..steps) {
                    val x = w * i / steps
                    val ry = y + (h * 0.035f) * sin((x / (w / 1.6f)) * 2f * PI.toFloat() + wavePhase * 1.3f + r)
                    lineTo(x, ry)
                }
            }
            drawPath(
                path  = ripple,
                color = onPrimary.copy(alpha = 0.16f * fade),
                style = Stroke(width = 2f)
            )
        }

        fun fishX(progress: Float, facingRight: Boolean, margin: Float): Float {
            val span = w + margin * 2f
            return if (facingRight) -margin + span * progress
            else w + margin - span * progress
        }

        drawFish(
            center      = Offset(fishX(fish1, true, w * 0.12f), h * 0.5f + sin(bob) * h * 0.04f),
            length      = w * 0.07f,
            color       = fishColor,
            facingRight = true,
            tailWiggle  = sin(bob * 1.4f) * h * 0.02f
        )
        drawFish(
            center      = Offset(fishX(fish2, false, w * 0.12f), h * 0.74f + sin(bob + 1.7f) * h * 0.035f),
            length      = w * 0.055f,
            color       = fishColor.copy(alpha = 0.42f),
            facingRight = false,
            tailWiggle  = sin(bob * 1.4f + 1.7f) * h * 0.018f
        )
        drawFish(
            center      = Offset(fishX(fish3, true, w * 0.1f), h * 0.62f + sin(bob + 3.1f) * h * 0.03f),
            length      = w * 0.045f,
            color       = fishColor.copy(alpha = 0.36f),
            facingRight = true,
            tailWiggle  = sin(bob * 1.4f + 3.1f) * h * 0.015f
        )
    }
}

private fun DrawScope.drawFish(
    center: Offset,
    length: Float,
    color: Color,
    facingRight: Boolean,
    tailWiggle: Float
) {
    val dir    = if (facingRight) 1f else -1f
    val bodyW  = length
    val bodyH  = length * 0.5f

    drawOval(
        color    = color,
        topLeft  = Offset(center.x - bodyW / 2f, center.y - bodyH / 2f),
        size     = Size(bodyW, bodyH)
    )

    val tailBaseX = center.x - dir * (bodyW / 2f)
    val tailLen   = length * 0.4f
    val tailH     = bodyH * 0.95f
    val tail = Path().apply {
        moveTo(tailBaseX, center.y)
        lineTo(tailBaseX - dir * tailLen, center.y - tailH / 2f + tailWiggle)
        lineTo(tailBaseX - dir * tailLen, center.y + tailH / 2f + tailWiggle)
        close()
    }
    drawPath(path = tail, color = color)
}
