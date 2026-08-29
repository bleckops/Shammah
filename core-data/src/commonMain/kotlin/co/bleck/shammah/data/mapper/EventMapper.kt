@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.DefaultInstant
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.domain.model.RECURSIVE_REPEAT
import co.bleck.shammah.domain.model.SINGLE_REPEAT
import co.bleck.shammah.domain.model.YEARLY_PERIOD

object EventMapper {
    fun toDomain(id: String, dto: EventDto): Event {
        val type = parseEventType(dto.type)
        val repeat = (dto.repeat as? String)?.lowercase()
            ?: if (type == EventType.birthdays) RECURSIVE_REPEAT else SINGLE_REPEAT
        val isRecurring = repeat == RECURSIVE_REPEAT
        return Event(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        date = dto.date ?: DefaultInstant,
        time = (dto.time as? String).orEmpty(),
        location = (dto.location as? String).orEmpty(),
        imageUrl = (dto.imageUrl as? String).orEmpty(),
        type = type,
        repeat = repeat,
        repeatNumber = if (isRecurring) parsePositiveInt(dto.repeatNumber) else null,
        period = if (isRecurring) ((dto.period as? String)?.lowercase()
            ?: if (type == EventType.birthdays) YEARLY_PERIOD else null) else null,
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: DefaultInstant,
        updatedAt = dto.updatedAt ?: DefaultInstant,
        )
    }

    private fun parsePositiveInt(value: Any?): Int? = when (value) {
        is Int -> value.takeIf { it > 0 }
        is Long -> value.toInt().takeIf { it > 0 }
        is Double -> value.toInt().takeIf { it > 0 && it.toDouble() == value }
        is String -> value.toIntOrNull()?.takeIf { it > 0 }
        else -> null
    }

    private fun parseEventType(value: Any?): EventType {
        val raw = (value as? String)?.lowercase() ?: return EventType.social
        return EventType.entries.firstOrNull { it.name == raw } ?: EventType.social
    }
}
