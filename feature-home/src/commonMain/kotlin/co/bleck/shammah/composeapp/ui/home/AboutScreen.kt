package co.bleck.shammah.composeapp.ui.home

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.bleck.shammah.composeapp.ui.auth.AuthViewModel
import co.bleck.shammah.composeapp.ui.components.StaggeredEntrance
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AboutScreen(authViewModel: AuthViewModel, appVersionName: String) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val userUid = currentUser?.id

    val aboutViewModel: AboutViewModel = koinViewModel()
    val aboutContent by aboutViewModel.content.collectAsState()

    // Built-in fallback copy used when a Resource document is missing, empty, or
    // unreadable (e.g. PERMISSION_DENIED on the `resources` collection).
    val missionText = aboutContent.mission?.description?.takeIf { it.isNotBlank() }
        ?: "Formar a Cristo en cada uno de los miembros de su iglesia, esto se mostrará en corazones que oran, que leen su Palabra, que se preocupan por otros miembros de la Iglesia y hermanos en la fe, así como que tienen compasión por los que aún no conocen a Dios."
    val visionText = aboutContent.vision?.description?.takeIf { it.isNotBlank() }
        ?: "Perseverando en tratar con todas nuestras fuerzas cumplir la voluntad de Dios, para ser hallados por Él sin mancha e irreprensibles, en paz."
    val aboutUsText = aboutContent.aboutUs?.description?.takeIf { it.isNotBlank() }
        ?: "Iglesia Shammah nació con el propósito de glorificar a Dios en un mundo caido dentro de la Ciudad de México. Desde nuestro inicio, hemos buscado ser 'Formados por Cristo' en cada paso de nuestro caminar. Jehová es nuestro gozo, nuestra fortaleza y nuestra motivación diaria para transmitir este mensaje"

    val missionTitle = aboutContent.mission?.title?.takeIf { it.isNotBlank() } ?: "Nuestra Misión"
    val visionTitle = aboutContent.vision?.title?.takeIf { it.isNotBlank() } ?: "Nuestra Visión"
    val aboutUsTitle = aboutContent.aboutUs?.title?.takeIf { it.isNotBlank() } ?: "Nuestra Historia"

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
                userUid?.let { uid ->
                    StaggeredEntrance(index = 1) {
                        ProfileCard(uid = uid) { authViewModel.signOut() }
                    }
                }

                val offset = if (userUid != null) 1 else 0

                StaggeredEntrance(index = 1 + offset) {
                    AboutCard(
                        title       = missionTitle,
                        content     = missionText,
                        icon        = Icons.Default.Groups,
                        accentColor = MaterialTheme.colorScheme.primary
                    )
                }
                StaggeredEntrance(index = 2 + offset) {
                    AboutCard(
                        title       = visionTitle,
                        content     = visionText,
                        icon        = Icons.Default.Lightbulb,
                        accentColor = MaterialTheme.colorScheme.secondary
                    )
                }
                StaggeredEntrance(index = 3 + offset) {
                    AboutCard(
                        title       = aboutUsTitle,
                        content     = aboutUsText,
                        icon        = Icons.Default.Church,
                        accentColor = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // App version footer
                Text(
                    text  = "Shammah · v$appVersionName",
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
                Icon(
                    imageVector = Icons.Default.Church,
                    contentDescription = "Shammah Logo",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(48.dp)
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
fun ProfileCard(uid: String, onSignOut: () -> Unit) {
    // Generate a subtle gradient avatar from the UID hash
    val uidHash  = uid.hashCode()
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
                        text  = "ID: ${uid.take(12)}…",
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
