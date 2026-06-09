package co.bleck.shammah.domain.model

import java.util.Date

enum class EventType {
    birthdays,
    retreat,
    camp,
    prayer,
    social,
    evangelism
}

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Date = Date(),
    val time: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val type: EventType = EventType.social,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
