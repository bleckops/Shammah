package co.bleck.shammah.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(viewModel: AuthViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        startAnimation = true
    }

    // Background shimmer for decorative orbs
    val shimmer = rememberInfiniteTransition(label = "bg_shimmer")
    val shimmerX by shimmer.animateFloat(
        initialValue = 0f, targetValue = 30f,
        animationSpec = infiniteRepeatable(tween(4000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "shimmer_x"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative floating orbs
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .offset(x = (40 + shimmerX).dp, y = (-20).dp)
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f))
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Box(
                modifier = Modifier
                    .offset(x = (-60).dp, y = (80 + shimmerX).dp)
                    .size(320.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.04f))
            )
        }

        AnimatedVisibility(
            visible = startAnimation,
            enter   = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.94f),
            exit    = fadeOut() + scaleOut()
        ) {
            // Animated shimmer border on the card
            val borderShimmer = rememberInfiniteTransition(label = "card_border")
            val borderAlpha by borderShimmer.animateFloat(
                initialValue  = 0.25f, targetValue = 0.6f,
                animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
                label = "border_alpha"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = borderAlpha),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = borderAlpha * 0.2f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = borderAlpha)
                                ),
                                start = Offset.Zero,
                                end   = Offset(800f, 800f)
                            )
                        ),
                        RoundedCornerShape(32.dp)
                    ),
                shape     = RoundedCornerShape(32.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 44.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Animated church emblem
                    ChurchEmblem()

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text      = "Iglesia Shammah",
                        style     = MaterialTheme.typography.headlineMedium,
                        color     = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text      = "Formados por Cristo",
                        style     = MaterialTheme.typography.titleMedium.copy(
                            fontStyle     = FontStyle.Italic,
                            letterSpacing = 0.6.sp
                        ),
                        color     = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ornamental divider
                    OrnamentalDivider()

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text      = "Bienvenido a nuestra aplicación. Únete a nosotros en una sesión segura de invitado para explorar nuestros sermones, avisos e información espiritual.",
                        style     = MaterialTheme.typography.bodyLarge,
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    AnimatedContent(
                        targetState = uiState,
                        transitionSpec = {
                            fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                        },
                        label = "auth_state"
                    ) { state ->
                        when (state) {
                            is AuthUiState.Loading -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(
                                        color    = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text       = "Autenticando de forma segura...",
                                        style      = MaterialTheme.typography.labelLarge,
                                        color      = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            is AuthUiState.Error -> {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text     = state.message,
                                            style    = MaterialTheme.typography.bodyMedium,
                                            color    = MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    WelcomeButton { viewModel.signInAnonymously() }
                                }
                            }
                            else -> WelcomeButton { viewModel.signInAnonymously() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChurchEmblem() {
    val pulse = rememberInfiniteTransition(label = "emblem_pulse")
    val pulseScale by pulse.animateFloat(
        initialValue  = 1.0f,
        targetValue   = 1.06f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emblem_scale"
    )

    Box(
        modifier = Modifier
            .size(96.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    )
                )
            )
            .border(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.55f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = Icons.Default.Church,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.primary,
            modifier           = Modifier.size(46.dp)
        )
    }
}

@Composable
private fun OrnamentalDivider() {
    Row(
        modifier            = Modifier.fillMaxWidth(),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.0f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                        )
                    )
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text  = "✦",
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.0f)
                        )
                    )
                )
        )
    }
}

@Composable
fun WelcomeButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue   = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(120),
        label         = "btn_scale"
    )

    Button(
        onClick           = onClick,
        modifier          = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale),
        shape             = RoundedCornerShape(16.dp),
        colors            = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation         = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        interactionSource = interactionSource
    ) {
        Text(
            text       = "Comenzar",
            style      = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier           = Modifier.size(18.dp),
            tint               = MaterialTheme.colorScheme.onPrimary
        )
    }
}
