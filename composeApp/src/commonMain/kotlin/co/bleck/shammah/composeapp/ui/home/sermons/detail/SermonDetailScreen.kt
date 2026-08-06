@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.sermons.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import co.bleck.shammah.composeapp.ui.components.ShimmerBox
import co.bleck.shammah.composeapp.ui.components.toLongDateEs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SermonDetailScreen(
    navController: NavController? = null,
    backStackEntry: NavBackStackEntry
) {
    val sermonId = remember(backStackEntry) {
        backStackEntry.savedStateHandle.get<String>("sermonId").orEmpty()
    }
    val vm: SermonDetailViewModel = koinViewModel(
        key = "sermon_detail_$sermonId",
        parameters = { parametersOf(sermonId) }
    )
    val sermon by vm.sermon.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar   = {
            LargeTopAppBar(
                title = {
                    Text(
                        text  = sermon?.title ?: "Sermón",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor         = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor      = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (val currentSermon = sermon) {
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
                ShimmerBox(height = 28.dp, cornerRadius = 8.dp, modifier = Modifier.fillMaxWidth(0.7f))
                ShimmerBox(height = 240.dp, cornerRadius = 20.dp)
            }
            }
            else -> {
            var visible by remember(currentSermon.id) { mutableStateOf(false) }
            LaunchedEffect(currentSermon.id) { visible = true }

            // Estimate reading time (avg 200 words/min)
            val wordCount = remember(currentSermon.id) {
                (currentSermon.notes.ifEmpty { currentSermon.description }).split("\\s+".toRegex()).size
            }
            val readingMinutes = maxOf(1, wordCount / 200)

            val dateStr = remember(currentSermon.id, currentSermon.date) {
                currentSermon.date.toLongDateEs()
            }

            val scrollState = rememberScrollState()
            LaunchedEffect(currentSermon.id) {
                scrollState.scrollTo(0)
            }

            AnimatedVisibility(
                visible  = visible,
                enter    = fadeIn(tween(500)) + slideInVertically(tween(500)) { 70 },
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // ── Header info row ────────────────────────────────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        // Date chip
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier          = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector        = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.primary,
                                    modifier           = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text       = dateStr,
                                    style      = MaterialTheme.typography.labelMedium,
                                    color      = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Reading time chip
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier          = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector        = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.secondary,
                                    modifier           = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text       = "~$readingMinutes min de lectura",
                                    style      = MaterialTheme.typography.labelMedium,
                                    color      = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // ── Decorative gradient header bar ─────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            )
                    )

                    // ── Description summary ────────────────────────────────────
                    if (currentSermon.description.isNotEmpty() && currentSermon.notes.isNotEmpty()) {
                        Text(
                            text  = currentSermon.description,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontStyle = FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    // ── Content card with quote decoration ─────────────────────
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = MaterialTheme.shapes.large,
                        colors    = CardDefaults.cardColors(
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
                                    imageVector        = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.secondary,
                                    modifier           = Modifier.size(20.dp)
                                )
                                Text(
                                    text       = "Notas del Sermón",
                                    style      = MaterialTheme.typography.labelLarge,
                                    color      = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Quote mark watermark
                            Box {
                                Icon(
                                    imageVector        = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f),
                                    modifier           = Modifier.size(80.dp).align(Alignment.TopStart)
                                )
                                Text(
                                    text     = if (currentSermon.notes.isNotEmpty()) currentSermon.notes else currentSermon.description,
                                    style    = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                                    color    = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 28.dp, start = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            }
        }
    }
}
