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
    /** Firestore values are kept as strings to remain compatible with the admin schema. */
    val repeat: String? = null,
    val repeatNumber: Int? = null,
    val period: String? = null,
    val isActive: Boolean = true,
    val createdAt: Instant = DefaultInstant,
    val updatedAt: Instant = DefaultInstant,
)
