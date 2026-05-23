package co.bleck.shammah.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative geometric background elements for elegant aesthetic
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 40.dp, y = (-20).dp)
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f))
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (-60).dp, y = 80.dp)
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.03f))
            )
        }

        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn() + scaleIn(initialScale = 0.95f),
            exit = fadeOut() + scaleOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                                )
                            )
                        ),
                        RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Church Branding Circle Emblem
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Church,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Title
                    Text(
                        text = "Iglesia Shammah",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.8).sp,
                            lineHeight = 36.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle
                    Text(
                        text = "Formados por Cristo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Description text
                    Text(
                        text = "Bienvenido a nuestra aplicación. Únete a nosotros en una sesión segura de invitado para explorar nuestros sermones, avisos e información espiritual.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // Dynamic loading/button state machine
                    when (uiState) {
                        is AuthUiState.Loading -> {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Autenticando de forma segura...",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        is AuthUiState.Error -> {
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
                                    text = (uiState as AuthUiState.Error).message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            WelcomeButton { viewModel.signInAnonymously() }
                        }

                        else -> {
                            WelcomeButton { viewModel.signInAnonymously() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "Comenzar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = 0.5.sp
        )
    }
}
