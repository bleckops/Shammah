@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.model

import kotlinx.datetime.Instant

enum class EventType {
    birthdays,
    retreat,
    camp,
    prayer,
    social,
    evangelism,
    discipleship
}

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Instant = DefaultInstant,
    val time: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val type: EventType = EventType.social,
    val isActive: Boolean = true,
    val createdAt: Instant = DefaultInstant,
    val updatedAt: Instant = DefaultInstant,
)
