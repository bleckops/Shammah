package co.bleck.shammah.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.bleck.shammah.ui.auth.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun AboutStaggeredEntrance(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 120L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
            animationSpec = tween(500, easing = LinearOutSlowInEasing),
            initialOffsetY = { 60 }
        ),
        exit = fadeOut(),
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun AboutScreen(authViewModel: AuthViewModel = viewModel()) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            AboutStaggeredEntrance(index = 0) {
                Text(
                    text = "Acerca de Shammah",
                    style = MaterialTheme.typography.displaySmall.copy(
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            // Guest User Profile Card
            currentUser?.let { user ->
                AboutStaggeredEntrance(index = 1) {
                    ProfileCard(user = user) {
                        authViewModel.signOut()
                    }
                }
            }

            // Mission Card (index shifted if profile card is shown)
            val offset = if (currentUser != null) 1 else 0
            AboutStaggeredEntrance(index = 1 + offset) {
                AboutCard(
                    title = "Nuestra Misión",
                    content = "Compartir el amor de Cristo, restaurar vidas y extender el Reino de Dios en nuestra comunidad a través de la formación espiritual y el servicio.",
                    icon = Icons.Default.Groups,
                    accentColor = MaterialTheme.colorScheme.primary
                )
            }

            // Vision Card
            AboutStaggeredEntrance(index = 2 + offset) {
                AboutCard(
                    title = "Nuestra Visión",
                    content = "Ser una iglesia apasionada por la presencia de Dios, donde cada persona es formada por Cristo para transformar su entorno.",
                    icon = Icons.Default.Lightbulb,
                    accentColor = MaterialTheme.colorScheme.secondary
                )
            }

            // History Card
            AboutStaggeredEntrance(index = 3 + offset) {
                AboutCard(
                    title = "Nuestra Historia",
                    content = "Iglesia Shammah nació con el propósito de ser un refugio de paz y crecimiento espiritual en la Ciudad de México. Desde nuestro inicio, hemos buscado ser 'Formados por Cristo' en cada paso de nuestro caminar.",
                    icon = Icons.Default.Church,
                    accentColor = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileCard(user: com.google.firebase.auth.FirebaseUser, onSignOut: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                        .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sesión de Invitado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ID: ${user.uid.take(12)}...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "Activo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))

            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f),
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "Cerrar Sesión",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AboutCard(
    title: String,
    content: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Forces Row's height to stretch to match the tallest sibling (the Column)
        ) {
            // High-legibility left color strip accentuating the brand theme
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.2).sp
                    )
                }
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}