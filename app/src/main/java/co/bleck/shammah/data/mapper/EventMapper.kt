package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import java.util.Date

object EventMapper {

    fun toDomain(id: String, dto: EventDto): Event = Event(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        date = dto.date ?: Date(),
        time = (dto.time as? String).orEmpty(),
        location = (dto.location as? String).orEmpty(),
        imageUrl = (dto.imageUrl as? String).orEmpty(),
        type = dto.type ?: EventType.social,
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}
