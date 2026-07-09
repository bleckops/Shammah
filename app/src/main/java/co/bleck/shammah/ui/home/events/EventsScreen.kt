package co.bleck.shammah.ui.home.events

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ── EventType helpers ──────────────────────────────────────────────────────────

fun EventType.color(): Color = when (this) {
    EventType.birthdays  -> Color(0xFFE91E63)
    EventType.retreat    -> Color(0xFF9C27B0)
    EventType.camp       -> Color(0xFF4CAF50)
    EventType.prayer     -> Color(0xFF2196F3)
    EventType.social     -> Color(0xFFFF9800)
    EventType.evangelism -> Color(0xFFFF5722)
}

fun EventType.icon(): ImageVector = when (this) {
    EventType.birthdays  -> Icons.Filled.Cake
    EventType.retreat    -> Icons.Filled.SelfImprovement
    EventType.camp       -> Icons.Filled.NaturePeople
    EventType.prayer     -> Icons.Filled.FavoriteBorder
    EventType.social     -> Icons.Filled.Groups
    EventType.evangelism -> Icons.Filled.RecordVoiceOver
}

fun EventType.label(): String = when (this) {
    EventType.birthdays  -> "Cumpleaños"
    EventType.retreat    -> "Retiro"
    EventType.camp       -> "Campamento"
    EventType.prayer     -> "Oración"
    EventType.social     -> "Social"
    EventType.evangelism -> "Evangelismo"
}

// ── Month name helper ──────────────────────────────────────────────────────────

private fun YearMonth.displayName(): String {
    val month = this.month.getDisplayName(TextStyle.FULL, Locale("es", "MX"))
        .replaceFirstChar { it.uppercaseChar() }
    return "$month ${this.year}"
}

private fun LocalDate.shortDisplay(): String {
    val day = this.dayOfMonth
    val month = this.month.getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
        .replaceFirstChar { it.uppercaseChar() }
    return "$day $month"
}

// ── Main Screen ────────────────────────────────────────────────────────────────

@Composable
fun EventsScreen(viewModel: EventsViewModel = hiltViewModel()) {
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
                val context = LocalContext.current
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
                        exportEventsToIcs(context, events)
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
                            isToday      = day.date == LocalDate.now(),
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
                text      = day.getDisplayName(TextStyle.NARROW, Locale("es", "MX"))
                    .uppercase(),
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
            text      = day.date.dayOfMonth.toString(),
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
            text       = "Eventos — ${date.shortDisplay()}",
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
        Row(modifier = Modifier.fillMaxWidth()) {
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

// ── iCalendar Export Helpers ───────────────────────────────────────────────────

private fun exportEventsToIcs(context: Context, events: List<Event>) {
    if (events.isEmpty()) {
        Toast.makeText(context, "No hay eventos para exportar", Toast.LENGTH_SHORT).show()
        return
    }

    val builder = StringBuilder()
    builder.append("BEGIN:VCALENDAR\r\n")
    builder.append("VERSION:2.0\r\n")
    builder.append("PRODID:-//BleckOps//Shammah//ES\r\n")
    builder.append("CALSCALE:GREGORIAN\r\n")
    builder.append("METHOD:PUBLISH\r\n")
    
    val sdf = java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }
    val dateOnlyFmt = java.text.SimpleDateFormat("yyyyMMdd", Locale.US)
    
    val currentYear = java.time.LocalDate.now().year

    for (event in events) {
        val id = (event.id as? String).orEmpty()
        val title = (event.title as? String).orEmpty()
        val description = (event.description as? String).orEmpty()
        val location = (event.location as? String).orEmpty()
        val eventTime = (event.time as? String).orEmpty()
        val type = event.type ?: EventType.social
        val eventDate = event.date ?: java.util.Date()
        val createdAt = event.createdAt ?: java.util.Date()

        builder.append("BEGIN:VEVENT\r\n")
        builder.append("UID:${id.ifBlank { java.util.UUID.randomUUID().toString() }}\r\n")
        
        val createdAtStr = sdf.format(createdAt)
        builder.append("DTSTAMP:$createdAtStr\r\n")
        
        if (type == EventType.birthdays) {
            // Birthdays match by month+day and recur every year.
            val cal = java.util.Calendar.getInstance()
            cal.time = eventDate
            
            // Set start date to current year
            cal.set(java.util.Calendar.YEAR, currentYear)
            cal.set(java.util.Calendar.HOUR_OF_DAY, 9)
            cal.set(java.util.Calendar.MINUTE, 0)
            cal.set(java.util.Calendar.SECOND, 0)
            
            val startStr = sdf.format(cal.time)
            cal.add(java.util.Calendar.HOUR_OF_DAY, 1)
            val endStr = sdf.format(cal.time)
            
            builder.append("DTSTART:$startStr\r\n")
            builder.append("DTEND:$endStr\r\n")
            builder.append("RRULE:FREQ=YEARLY\r\n")
        } else {
            // Normal event
            val startCal = java.util.Calendar.getInstance().apply { setTime(eventDate) }
            var timeParsed = false
            if (eventTime.isNotBlank()) {
                try {
                    val timeClean = eventTime.trim().uppercase()
                    // Try to parse formats like "12:30 PM", "12:30PM", "18:00", etc.
                    val matcher = java.util.regex.Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(AM|PM)?").matcher(timeClean)
                    if (matcher.find()) {
                        val hourStr = matcher.group(1)
                        val minuteStr = matcher.group(2)
                        if (hourStr != null && minuteStr != null) {
                            var hour = hourStr.toInt()
                            val minute = minuteStr.toInt()
                            val ampm = matcher.group(3)
                            if (ampm != null) {
                                if (ampm == "PM" && hour < 12) hour += 12
                                if (ampm == "AM" && hour == 12) hour = 0
                            }
                            startCal.set(java.util.Calendar.HOUR_OF_DAY, hour)
                            startCal.set(java.util.Calendar.MINUTE, minute)
                            startCal.set(java.util.Calendar.SECOND, 0)
                            timeParsed = true
                        }
                    }
                } catch (e: Exception) {
                    // ignore, fallback to all-day
                }
            }
            
            if (timeParsed) {
                val startStr = sdf.format(startCal.time)
                startCal.add(java.util.Calendar.HOUR_OF_DAY, 2) // default duration 2 hours
                val endStr = sdf.format(startCal.time)
                builder.append("DTSTART:$startStr\r\n")
                builder.append("DTEND:$endStr\r\n")
            } else {
                // All-day event
                val startStr = dateOnlyFmt.format(eventDate)
                val endCal = java.util.Calendar.getInstance().apply {
                    setTime(eventDate)
                    add(java.util.Calendar.DAY_OF_MONTH, 1)
                }
                val endStr = dateOnlyFmt.format(endCal.time)
                builder.append("DTSTART;VALUE=DATE:$startStr\r\n")
                builder.append("DTEND;VALUE=DATE:$endStr\r\n")
            }
        }
        
        builder.append("SUMMARY:${escapeIcsValue(title)}\r\n")
        if (description.isNotBlank()) {
            builder.append("DESCRIPTION:${escapeIcsValue(description)}\r\n")
        }
        if (location.isNotBlank()) {
            builder.append("LOCATION:${escapeIcsValue(location)}\r\n")
        }
        builder.append("END:VEVENT\r\n")
    }
    builder.append("END:VCALENDAR\r\n")
    
    try {
        val cacheDir = File(context.cacheDir, "calendars")
        if (!cacheDir.exists()) cacheDir.mkdirs()
        val file = File(cacheDir, "eventos_shammah.ics")
        file.writeText(builder.toString())
        
        val authority = "${context.packageName}.fileprovider"
        val uri = androidx.core.content.FileProvider.getUriForFile(context, authority, file)
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/calendar"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Calendario de Eventos - Iglesia Shammah")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar calendario con..."))
    } catch (e: Exception) {
        Toast.makeText(context, "Error al exportar calendario: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}

private fun escapeIcsValue(value: String): String {
    return value.replace("\\", "\\\\")
        .replace(";", "\\;")
        .replace(",", "\\,")
        .replace("\n", "\\n")
}
