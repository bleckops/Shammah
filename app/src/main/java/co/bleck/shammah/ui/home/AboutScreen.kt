package co.bleck.shammah.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.bleck.shammah.BuildConfig
import co.bleck.shammah.R
import co.bleck.shammah.ui.auth.AuthViewModel
import co.bleck.shammah.ui.components.StaggeredEntrance

@Composable
fun AboutScreen(authViewModel: AuthViewModel = viewModel()) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero header ─────────────────────────────────────────────────
            StaggeredEntrance(index = 0) {
                AboutHero()
            }

            Column(
                modifier            = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Guest profile card
                currentUser?.let { user ->
                    StaggeredEntrance(index = 1) {
                        ProfileCard(user = user) { authViewModel.signOut() }
                    }
                }

                val offset = if (currentUser != null) 1 else 0

                StaggeredEntrance(index = 1 + offset) {
                    AboutCard(
                        title       = "Nuestra Misión",
                        content     = "Ser un reflejo del amor de Cristo para los demás, restaurar vidas y extender el Reino de Dios en nuestra comunidad a través de la formación espiritual y del servicio.",
                        icon        = Icons.Default.Groups,
                        accentColor = MaterialTheme.colorScheme.primary
                    )
                }
                StaggeredEntrance(index = 2 + offset) {
                    AboutCard(
                        title       = "Nuestra Visión",
                        content     = "Ser una iglesia apasionada por la presencia de Dios, donde cada persona es formada por Cristo para transformar su entorno.",
                        icon        = Icons.Default.Lightbulb,
                        accentColor = MaterialTheme.colorScheme.secondary
                    )
                }
                StaggeredEntrance(index = 3 + offset) {
                    AboutCard(
                        title       = "Nuestra Historia",
                        content     = "Iglesia Shammah nació con el propósito de ser un refugio de paz y crecimiento espiritual en la Ciudad de México. Desde nuestro inicio, hemos buscado ser 'Formados por Cristo' en cada paso de nuestro caminar. Jehová es nuestra fortaleza y nuestra motivación diaria",
                        icon        = Icons.Default.Church,
                        accentColor = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // App version footer
                Text(
                    text  = "Shammah · v${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AboutHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.80f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f))
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter            = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "Shammah Logo",
                    modifier           = Modifier.size(80.dp)
                )
            }
            Text(
                text      = "Acerca de Shammah",
                style     = MaterialTheme.typography.headlineMedium,
                color     = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileCard(user: com.google.firebase.auth.FirebaseUser, onSignOut: () -> Unit) {
    // Generate a subtle gradient avatar from the UID hash
    val uidHash  = user.uid.hashCode()
    val hue1     = ((uidHash and 0xFF) * 1.41f).coerceIn(0f, 360f)
    val hue2     = ((hue1 + 60f) % 360f)
    val avatarGradient = Brush.linearGradient(
        listOf(
            Color.hsv(hue1, 0.45f, 0.75f),
            Color.hsv(hue2, 0.55f, 0.85f)
        )
    )

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = MaterialTheme.shapes.large,
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border    = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Gradient avatar circle
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(avatarGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Person,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(26.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "Sesión de Invitado",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text  = "ID: ${user.uid.take(12)}…",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text       = "Activo",
                        style      = MaterialTheme.typography.labelSmall,
                        color      = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier   = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            OutlinedButton(
                onClick   = onSignOut,
                modifier  = Modifier.fillMaxWidth().height(44.dp),
                shape     = MaterialTheme.shapes.small,
                colors    = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border    = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            ) {
                Text(
                    text       = "Cerrar Sesión",
                    style      = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AboutCard(
    title:       String,
    content:     String,
    icon:        ImageVector,
    accentColor: Color
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = MaterialTheme.shapes.large,
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border    = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            // Accent bar
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            listOf(accentColor, accentColor.copy(alpha = 0.5f))
                        )
                    )
            )
            Column(
                modifier            = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = icon,
                            contentDescription = null,
                            tint               = accentColor,
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text      = title,
                        style     = MaterialTheme.typography.titleMedium,
                        color     = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text  = content,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}