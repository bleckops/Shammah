package co.bleck.shammah.data.dto

import co.bleck.shammah.domain.model.EventType
import java.util.Date

data class EventDto(
    val title: Any? = null,
    val description: Any? = null,
    val date: Date? = null,
    val time: Any? = null,
    val location: Any? = null,
    val imageUrl: Any? = null,
    val type: EventType? = null,
    val isActive: Boolean = true,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
