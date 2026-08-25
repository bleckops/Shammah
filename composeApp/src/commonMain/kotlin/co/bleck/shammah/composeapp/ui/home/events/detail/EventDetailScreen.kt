@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import co.bleck.shammah.composeapp.ui.components.ShimmerBox
import co.bleck.shammah.composeapp.ui.components.toLongDateEs
import co.bleck.shammah.composeapp.ui.home.events.color
import co.bleck.shammah.composeapp.ui.home.events.icon
import co.bleck.shammah.composeapp.ui.home.events.label
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavController? = null,
    backStackEntry: NavBackStackEntry
) {
    val eventId = remember(backStackEntry) {
        backStackEntry.savedStateHandle.get<String>("eventId").orEmpty()
    }
    val vm: EventDetailViewModel = koinViewModel(
        key = "event_detail_$eventId",
        parameters = { parametersOf(eventId) }
    )
    val event by vm.event.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = event?.title ?: "Evento",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (val currentEvent = event) {
            null -> {
                // Shimmer loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ShimmerBox(height = 36.dp, cornerRadius = 8.dp)
                    ShimmerBox(height = 24.dp, cornerRadius = 8.dp, modifier = Modifier.fillMaxWidth(0.5f))
                    ShimmerBox(height = 220.dp, cornerRadius = 20.dp)
                    ShimmerBox(height = 16.dp, cornerRadius = 6.dp, modifier = Modifier.fillMaxWidth(0.9f))
                    ShimmerBox(height = 16.dp, cornerRadius = 6.dp, modifier = Modifier.fillMaxWidth(0.7f))
                    ShimmerBox(height = 16.dp, cornerRadius = 6.dp, modifier = Modifier.fillMaxWidth(0.85f))
                }
            }
            else -> {
                var visible by remember(currentEvent.id) { mutableStateOf(false) }
                LaunchedEffect(currentEvent.id) { visible = true }

                val dateStr = remember(currentEvent.id, currentEvent.date) {
                    currentEvent.date.toLongDateEs()
                }
                val typeColor = currentEvent.type.color()
                val typeIcon = currentEvent.type.icon()
                val typeLabel = currentEvent.type.label()
                val isBirthday = currentEvent.type == EventType.birthdays
                val hasImage = currentEvent.imageUrl.isNotBlank()

                val scrollState = rememberScrollState()
                LaunchedEffect(currentEvent.id) {
                    scrollState.scrollTo(0)
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { 70 },
                    modifier = Modifier.padding(innerPadding).fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // ── Event type header row ────────────────────────────────
                        EventTypeHeader(
                            typeLabel = typeLabel,
                            typeColor = typeColor,
                            typeIcon = typeIcon,
                            isBirthday = isBirthday
                        )

                        // ── Decorative gradient bar ──────────────────────────────
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(typeColor, MaterialTheme.colorScheme.primary)
                                    )
                                )
                        )

                        // ── Optional image ───────────────────────────────────────
                        if (hasImage) {
                            EventImageCard(
                                imageUrl = currentEvent.imageUrl,
                                accent = typeColor
                            )
                        }

                        // ── Date & Hour info chips ───────────────────────────────
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InfoChip(
                                icon = Icons.Default.CalendarToday,
                                text = dateStr,
                                accent = MaterialTheme.colorScheme.primary
                            )

                            if (currentEvent.time.isNotBlank() && !isBirthday) {
                                InfoChip(
                                    icon = Icons.Default.AccessTime,
                                    text = currentEvent.time,
                                    accent = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        // ── Location (if present) ────────────────────────────────
                        if (currentEvent.location.isNotBlank() && !isBirthday) {
                            LocationRow(
                                location = currentEvent.location,
                                accent = typeColor
                            )
                        }

                        // ── Description ─────────────────────────────────────────
                        if (currentEvent.description.isNotBlank()) {
                            DescriptionCard(
                                description = currentEvent.description,
                                accent = typeColor
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

// ── Event type header (chip + icon pill) ──────────────────────────────────────
@Composable
private fun EventTypeHeader(
    typeLabel: String,
    typeColor: androidx.compose.ui.graphics.Color,
    typeIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isBirthday: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(typeColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = typeIcon,
                contentDescription = null,
                tint = typeColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Tipo de evento",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = typeLabel,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = typeColor
                )
                if (isBirthday) {
                    Surface(
                        shape = CircleShape,
                        color = typeColor.copy(alpha = 0.14f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = null,
                                tint = typeColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Anual",
                                style = MaterialTheme.typography.labelSmall,
                                color = typeColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Image card (optional) ──────────────────────────────────────────────────────
@Composable
private fun EventImageCard(imageUrl: String, accent: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
            // Subtle gradient overlay with type-colored corner accent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                androidx.compose.ui.graphics.Color.Transparent,
                                accent.copy(alpha = 0.25f)
                            )
                        )
                    )
            )
            // Accent border on top
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ── Info chip (date / hour) ────────────────────────────────────────────────────
@Composable
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    accent: androidx.compose.ui.graphics.Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = accent.copy(alpha = 0.10f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = accent,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ── Location row ───────────────────────────────────────────────────────────────
@Composable
private fun LocationRow(
    location: String,
    accent: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = location,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Description card ──────────────────────────────────────────────────────────
@Composable
private fun DescriptionCard(
    description: String,
    accent: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
