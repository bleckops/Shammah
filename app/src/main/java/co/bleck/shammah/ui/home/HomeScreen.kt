package co.bleck.shammah.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.bleck.shammah.R
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.ui.components.ShimmerBox
import co.bleck.shammah.ui.components.StaggeredEntrance
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val banners by viewModel.banners.collectAsState()
    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            state           = listState,
            modifier        = Modifier.fillMaxSize(),
            contentPadding  = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                HeaderSection(listState)
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                StaggeredEntrance(index = 1) {
                    if (banners.isEmpty()) {
                        // Show shimmer placeholders while loading
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ShimmerBox(height = 200.dp, cornerRadius = 20.dp)
                        }
                    } else {
                        BannersSection(banners = banners) { url ->
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            } catch (e: Exception) { /* ignore */ }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                StaggeredEntrance(index = 2) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        InfoSectionHeader("Comunidad")
                        VisionCard()
                        ServiceHoursCard()
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(14.dp)) }
            item {
                StaggeredEntrance(index = 3) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        InfoSectionHeader("Visítanos")
                        Spacer(modifier = Modifier.height(14.dp))
                        AddressCard {
                            val address = "Calle Cerro de La Juvencia 130, Col. Campestre Churubusco, Alc. Coyoacán, Mexico City, Mexico, 04200"
                            val encodedAddress = Uri.encode(address)
                            val gmmIntentUri = Uri.parse("geo:19.3444066,-99.1388425?q=$encodedAddress")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            try {
                                mapIntent.setPackage("com.google.android.apps.maps")
                                context.startActivity(mapIntent)
                            } catch (e: Exception) {
                                try {
                                    mapIntent.setPackage(null)
                                    context.startActivity(mapIntent)
                                } catch (e2: Exception) {
                                    try {
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedAddress"))
                                        )
                                    } catch (e3: Exception) { /* no maps app */ }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(listState: LazyListState = rememberLazyListState()) {
    // Parallax: as user scrolls, the header scales down slightly
    val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
    val parallaxScale = (1f - (firstVisibleItemScrollOffset / 3000f)).coerceIn(0.92f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = parallaxScale; scaleY = parallaxScale }
            .height(260.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.82f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)
                    )
                )
            )
            .border(
                BorderStroke(
                    0.dp,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative circles
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Box(
                modifier = Modifier
                    .offset(x = (-40).dp, y = (-40).dp)
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.07f))
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .offset(x = 30.dp, y = 30.dp)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.04f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(horizontal = 24.dp)
        ) {
            // Actual app logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f))
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter            = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "Shammah Logo",
                    modifier           = Modifier.size(72.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text          = "Iglesia Shammah",
                style         = MaterialTheme.typography.headlineLarge,
                color         = MaterialTheme.colorScheme.onPrimary,
                textAlign     = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text      = "Bienvenido a casa",
                style     = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                color     = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
fun BannersSection(banners: List<Banner>, onBannerClick: (String) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-scroll every 4 seconds
    LaunchedEffect(pagerState, banners.size) {
        if (banners.size > 1) {
            while (true) {
                delay(4000)
                val next = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalPager(
            state          = pagerState,
            modifier       = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing    = 12.dp
        ) { page ->
            val banner = banners[page]
            val interaction = remember { MutableInteractionSource() }
            val isPressed by interaction.collectIsPressedAsState()
            val cardScale by animateFloatAsState(
                targetValue   = if (isPressed) 0.97f else 1f,
                animationSpec = tween(120),
                label         = "banner_scale"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .scale(cardScale)
                    .clickable(
                        interactionSource = interaction,
                        indication        = null
                    ) {
                        if (!banner.linkUrl.isNullOrBlank()) onBannerClick(banner.linkUrl)
                    },
                shape     = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box {
                    coil.compose.AsyncImage(
                        model              = banner.imageUrl,
                        contentDescription = banner.title,
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop
                    )
                    // Gradient overlay at bottom for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }
            }
        }

        // Animated indicator dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                val active = pagerState.currentPage == index
                val width by animateDpAsState(
                    targetValue   = if (active) 18.dp else 6.dp,
                    animationSpec = tween(280),
                    label         = "dot_width"
                )
                val color by animateColorAsState(
                    targetValue   = if (active)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.28f),
                    animationSpec = tween(280),
                    label         = "dot_color"
                )
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(width)
                        .clip(RoundedCornerShape(3.dp))
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun InfoSectionHeader(title: String) {
    Text(
        text       = title,
        style      = MaterialTheme.typography.titleSmall,
        color      = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.2.sp,
        modifier   = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
fun VisionCard() {
    PressableInfoCard {
        Row(
            modifier          = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconPill(icon = Icons.Default.Star, usePrimary = false)
            Spacer(modifier = Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Nuestra visión",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text       = "Ser transformados por Cristo y transformar a la sociedad",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ServiceHoursCard() {
    PressableInfoCard {
        Row(
            modifier          = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconPill(icon = Icons.Default.WatchLater, usePrimary = true)
            Spacer(modifier = Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Hora de Culto",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text       = "Domingo a las 12:30 PM",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AddressCard(onMapClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.98f else 1f,
        animationSpec = tween(120),
        label         = "addr_scale"
    )

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null) { onMapClick() },
        shape     = MaterialTheme.shapes.medium,
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border    = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconPill(icon = Icons.Default.LocationOn, usePrimary = true)
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text       = "Nuestra Ubicación",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text  = "Calle Cerro de La Juvencia No. 130,\nCol. Campestre Churubusco, Alc. Coyoacán,\nCDMX, México, C.P. 04200",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick   = onMapClick,
                modifier  = Modifier.fillMaxWidth().height(48.dp),
                shape     = MaterialTheme.shapes.small,
                colors    = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector        = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onPrimary,
                    modifier           = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text       = "Ver en Google Maps",
                    style      = MaterialTheme.typography.labelLarge,
                    color      = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Shared card shell with press animation ─────────────────────────────────────
@Composable
private fun PressableInfoCard(content: @Composable () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.98f else 1f,
        animationSpec = tween(120),
        label         = "card_press_scale"
    )

    Card(
        modifier  = Modifier.fillMaxWidth().scale(scale),
        shape     = MaterialTheme.shapes.medium,
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border    = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick   = {},
        interactionSource = interaction
    ) {
        content()
    }
}

// ── Reusable icon pill ─────────────────────────────────────────────────────────
@Composable
private fun IconPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    usePrimary: Boolean
) {
    val bg    = if (usePrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val tint  = if (usePrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Box(
        modifier         = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg.copy(alpha = 0.12f))
            .border(1.dp, bg.copy(alpha = 0.28f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = bg, modifier = Modifier.size(24.dp))
    }
}