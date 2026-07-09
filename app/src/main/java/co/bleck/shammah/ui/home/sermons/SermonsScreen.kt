package co.bleck.shammah.ui.home.sermons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.ui.components.ShimmerBox
import co.bleck.shammah.ui.components.StaggeredEntrance

private val filterOptions = listOf("Todos", "Recientes", "Series")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SermonsScreen(
    navController: NavHostController,
    vm: SermonsViewModel = hiltViewModel()
) {
    val sermons by vm.sermons.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar   = {
            LargeTopAppBar(
                title = {
                    Text(
                        text  = "Sermones",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors        = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor            = MaterialTheme.colorScheme.background,
                    scrolledContainerColor    = MaterialTheme.colorScheme.surface,
                    titleContentColor         = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        if (sermons.isEmpty()) {
            // Loading: shimmer skeletons OR empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(height = 110.dp, cornerRadius = 20.dp)
                ShimmerBox(height = 110.dp, cornerRadius = 20.dp)
                ShimmerBox(height = 110.dp, cornerRadius = 20.dp)
            }
        } else {
            LazyColumn(
                modifier        = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding  = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Filter chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding        = PaddingValues(vertical = 4.dp)
                    ) {
                        items(filterOptions.size) { index ->
                            val label    = filterOptions[index]
                            val selected = label == selectedFilter
                            val bgColor by animateColorAsState(
                                targetValue   = if (selected) MaterialTheme.colorScheme.secondary
                                               else MaterialTheme.colorScheme.surfaceVariant,
                                animationSpec = tween(200),
                                label         = "chip_bg"
                            )
                            val textColor by animateColorAsState(
                                targetValue   = if (selected) MaterialTheme.colorScheme.onSecondary
                                               else MaterialTheme.colorScheme.onSurfaceVariant,
                                animationSpec = tween(200),
                                label         = "chip_text"
                            )
                            FilterChip(
                                selected = selected,
                                onClick  = { selectedFilter = label },
                                label    = {
                                    Text(
                                        text  = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = textColor,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    containerColor         = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(50)
                            )
                        }
                    }
                }

                itemsIndexed(
                    items = sermons,
                    key = { _, sermon -> sermon.id }
                ) { index, sermon ->
                    StaggeredEntrance(index = index + 1) {
                        SermonCard(sermon) {
                            navController.navigate("sermon_detail/${sermon.id}") {
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SermonCard(sermon: Sermon, onClick: () -> Unit) {
    val dateStr = remember(sermon.date) {
        java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.forLanguageTag("es")).format(sermon.date)
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        onClick   = onClick,
        shape     = MaterialTheme.shapes.medium,
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Left accent bar in brand gold
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    // Date chip
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text      = dateStr,
                            style     = MaterialTheme.typography.labelSmall,
                            color     = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.SemiBold,
                            modifier  = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Chevron icon
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver sermón",
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier           = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text      = sermon.title,
                    style     = MaterialTheme.typography.titleLarge,
                    color     = MaterialTheme.colorScheme.onSurface,
                    maxLines  = 2,
                    overflow  = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text      = sermon.description,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines  = 2,
                    overflow  = TextOverflow.Ellipsis
                )
            }
        }
    }
}
