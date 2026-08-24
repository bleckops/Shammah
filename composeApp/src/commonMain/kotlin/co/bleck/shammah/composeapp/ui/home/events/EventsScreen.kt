@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
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
import co.bleck.shammah.composeapp.platform.PlatformActions
import co.bleck.shammah.composeapp.ui.components.fullNameEs
import co.bleck.shammah.composeapp.ui.components.narrowNameEs
import co.bleck.shammah.composeapp.ui.components.shortDisplayEs
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

// ── EventType helpers ──────────────────────────────────────────────────────────

fun EventType.color(): Color = when (this) {
    EventType.birthdays  -> Color(0xFFE91E63)
    EventType.retreat    -> Color(0xFF9C27B0)
    EventType.camp       -> Color(0xFF4CAF50)
    EventType.prayer     -> Color(0xFF2196F3)
    EventType.social     -> Color(0xFFFF9800)
    EventType.evangelism -> Color(0xFFFF5722)
    EventType.discipleship -> Color(0xFF673AB7)
}

fun EventType.icon(): ImageVector = when (this) {
    EventType.birthdays  -> Icons.Filled.Cake
    EventType.retreat    -> Icons.Filled.SelfImprovement
    EventType.camp       -> Icons.Filled.NaturePeople
    EventType.prayer     -> Icons.Filled.Fireplace
    EventType.social     -> Icons.Filled.Groups
    EventType.evangelism -> Icons.Filled.RecordVoiceOver
    EventType.discipleship -> Icons.Filled.Book
}

fun EventType.label(): String = when (this) {
    EventType.birthdays  -> "Cumpleaños"
    EventType.retreat    -> "Retiro"
    EventType.camp       -> "Campamento"
    EventType.prayer     -> "Oración"
    EventType.social     -> "Social"
    EventType.evangelism -> "Evangelismo"
    EventType.discipleship -> "Discipulado"
}

// ── Month name helper ──────────────────────────────────────────────────────────

private fun YearMonth.displayName(): String {
    val monthName = month.fullNameEs.replaceFirstChar { it.uppercaseChar() }
    return "$monthName $year"
}

// ── Main Screen ────────────────────────────────────────────────────────────────

@Composable
fun EventsScreen(viewModel: EventsViewModel = koinViewModel()) {
    val actions = koinInject<PlatformActions>()
    val events          by viewModel.events.collectAsState()
    val selectedDate    by viewModel.selectedDate.collectAsState()
    val eventDates      by viewModel.eventDates.collectAsState()
    val selectedEvents  by viewModel.eventsForSelectedDate.collectAsState()

    val currentMonth    = remember { YearMonth.now() }
    val startMonth      = remember { currentMonth.minusMonths(6) }
    val endMonth        = remember { currentMonth.plusMonths(18) }
    val daysOfWeek      = remember { daysOfWeek(firstDayOfWeekFromLocale()) }

    val calendarState = rememberCalendarState(
        startMonth          = startMonth,
        endMonth            = endMonth,
        firstVisibleMonth   = currentMonth,
        firstDayOfWeek      = daysOfWeek.first()
    )
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Calendar header ──────────────────────────────────────────────
            item {
                CalendarHeader(
                    currentMonth = calendarState.firstVisibleMonth.yearMonth,
                    onPrevious   = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(
                                calendarState.firstVisibleMonth.yearMonth.minusMonths(1)
                            )
                        }
                    },
                    onNext = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(
                                calendarState.firstVisibleMonth.yearMonth.plusMonths(1)
                            )
                        }
                    },
                    onExport = {
                        if (events.isEmpty()) {
                            actions.showMessage("No hay eventos para exportar")
                        } else {
                            val ics = buildEventsIcs(
                                events = events,
                                today = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                            )
                            actions.shareIcsCalendar("eventos_shammah.ics", ics)
                        }
                    }
                )
            }

            // ── Day-of-week labels ───────────────────────────────────────────
            item {
                DaysOfWeekRow(daysOfWeek)
            }

            // ── Kizitonwose HorizontalCalendar ───────────────────────────────
            item {
                HorizontalCalendar(
                    state          = calendarState,
                    dayContent     = { day ->
                        DayCell(
                            day          = day,
                            isSelected   = day.date == selectedDate,
                            isToday      = day.date == Clock.System.todayIn(TimeZone.currentSystemDefault()),
                            dotsColors   = if (day.position == DayPosition.MonthDate) {
                                eventDates[day.date]
                                    ?.map { it.type.color() }
                                    ?.distinct()
                                    ?.take(3)
                                    ?: emptyList()
                            } else emptyList(),
                            onClick      = {
                                if (day.position == DayPosition.MonthDate) {
                                    viewModel.selectDate(day.date)
                                }
                            }
                        )
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // ── Section title ────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                EventListSectionHeader(
                    date       = selectedDate,
                    count      = selectedEvents.size
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Event cards or empty state ───────────────────────────────────
            if (selectedEvents.isEmpty()) {
                item {
                    EmptyEventsCard()
                }
            } else {
                items(selectedEvents, key = { it.id }) { event ->
                    EventCard(event = event)
                    Spacer(Modifier.height(10.dp))
                }
            }

            // ── Legend ───────────────────────────────────────────────────────
            if (events.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(12.dp))
                    EventTypeLegend()
                }
            }
        }
    }
}

// ── Calendar Header ────────────────────────────────────────────────────────────

@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onExport: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
            )
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(end = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevious) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Mes anterior",
                    tint               = MaterialTheme.colorScheme.onPrimary,
                    modifier           = Modifier.size(28.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text      = currentMonth.displayName(),
                    style     = MaterialTheme.typography.titleLarge,
                    color     = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = "Calendario de Eventos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
            }

            IconButton(onClick = onNext) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Mes siguiente",
                    tint               = MaterialTheme.colorScheme.onPrimary,
                    modifier           = Modifier.size(28.dp)
                )
            }
        }

        IconButton(
            onClick  = onExport,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.Share,
                contentDescription = "Exportar calendario",
                tint               = MaterialTheme.colorScheme.onPrimary,
                modifier           = Modifier.size(24.dp)
            )
        }
    }
}

// ── Days-of-week row ───────────────────────────────────────────────────────────

@Composable
private fun DaysOfWeekRow(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        daysOfWeek.forEach { day ->
            Text(
                modifier  = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text      = day.narrowNameEs,
                style     = MaterialTheme.typography.labelSmall,
                color     = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
    }
}

// ── Day cell ──────────────────────────────────────────────────────────────────

@Composable
private fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    isToday: Boolean,
    dotsColors: List<Color>,
    onClick: () -> Unit
) {
    val isCurrentMonth = day.position == DayPosition.MonthDate

    val bgColor by animateColorAsState(
        targetValue = when {
            isSelected  -> MaterialTheme.colorScheme.primary
            isToday     -> MaterialTheme.colorScheme.secondaryContainer
            else        -> Color.Transparent
        },
        animationSpec = tween(180),
        label         = "day_bg"
    )

    val textColor = when {
        isSelected       -> MaterialTheme.colorScheme.onPrimary
        isToday          -> MaterialTheme.colorScheme.onSecondaryContainer
        isCurrentMonth   -> MaterialTheme.colorScheme.onSurface
        else             -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
    }

    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(enabled = isCurrentMonth, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text      = day.date.day.toString(),
            style     = MaterialTheme.typography.bodySmall,
            color     = textColor,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )

        // Dot indicators
        if (dotsColors.isNotEmpty()) {
            Spacer(Modifier.height(2.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                dotsColors.forEach { dotColor ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else dotColor)
                    )
                }
            }
        }
    }
}

// ── Section header ─────────────────────────────────────────────────────────────

@Composable
private fun EventListSectionHeader(date: LocalDate, count: Int) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = "Eventos — ${date.shortDisplayEs()}",
            style      = MaterialTheme.typography.titleSmall,
            color      = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
        )
        if (count > 0) {
            Surface(
                shape  = RoundedCornerShape(50),
                color  = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text      = count.toString(),
                    style     = MaterialTheme.typography.labelSmall,
                    color     = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier  = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

// ── Event card ─────────────────────────────────────────────────────────────────

@Composable
private fun EventCard(event: Event) {
    val typeColor  = event.type.color()
    val typeIcon   = event.type.icon()
    val typeLabel  = event.type.label()
    val isBirthday = event.type == EventType.birthdays

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Color accent left border
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(typeColor)
            )

            Row(
                modifier          = Modifier
                    .padding(horizontal = 14.dp, vertical = 14.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon pill
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(typeColor.copy(alpha = 0.12f))
                        .border(1.dp, typeColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = typeIcon,
                        contentDescription = null,
                        tint               = typeColor,
                        modifier           = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = event.title,
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(4.dp))

                    if (isBirthday) {
                        // Show recurring indicator instead of a specific year date
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector        = Icons.Filled.Repeat,
                                contentDescription = null,
                                tint               = typeColor.copy(alpha = 0.75f),
                                modifier           = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text  = "Cada año",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        if (event.time.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector        = Icons.Filled.AccessTime,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier           = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text  = event.time,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (event.location.isNotBlank()) {
                            Spacer(Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector        = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier           = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text  = event.location,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Type chip
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = typeColor.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text      = typeLabel,
                                style     = MaterialTheme.typography.labelSmall,
                                color     = typeColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier  = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                        // Anual badge for birthdays
                        if (isBirthday) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = typeColor.copy(alpha = 0.08f)
                            ) {
                                Text(
                                    text      = "Anual",
                                    style     = MaterialTheme.typography.labelSmall,
                                    color     = typeColor.copy(alpha = 0.75f),
                                    fontWeight = FontWeight.Medium,
                                    modifier  = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ── Empty state ────────────────────────────────────────────────────────────────

@Composable
private fun EmptyEventsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector        = Icons.Filled.EventBusy,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier           = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text      = "Sin eventos",
                style     = MaterialTheme.typography.titleSmall,
                color     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text      = "No hay eventos programados\npara este día",
                style     = MaterialTheme.typography.bodySmall,
                color     = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Legend ─────────────────────────────────────────────────────────────────────

@Composable
private fun EventTypeLegend() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text      = "Tipos de Eventos",
            style     = MaterialTheme.typography.labelSmall,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
        )
        Spacer(Modifier.height(10.dp))
        val types = EventType.entries
        val chunked = types.chunked(3)
        chunked.forEach { row ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier          = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(type.color())
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text  = type.label(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // Pad row if fewer than 3
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
