@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.DefaultInstant
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType

object EventMapper {
    fun toDomain(id: String, dto: EventDto): Event = Event(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        date = dto.date ?: DefaultInstant,
        time = (dto.time as? String).orEmpty(),
        location = (dto.location as? String).orEmpty(),
        imageUrl = (dto.imageUrl as? String).orEmpty(),
        type = parseEventType(dto.type),
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: DefaultInstant,
        updatedAt = dto.updatedAt ?: DefaultInstant,
    )

    private fun parseEventType(value: Any?): EventType {
        val raw = (value as? String)?.lowercase() ?: return EventType.social
        return EventType.entries.firstOrNull { it.name == raw } ?: EventType.social
    }
}
