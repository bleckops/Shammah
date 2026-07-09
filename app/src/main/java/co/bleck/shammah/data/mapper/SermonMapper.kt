package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.SermonDto
import co.bleck.shammah.domain.model.Sermon
import java.util.Date

object SermonMapper {

    fun toDomain(id: String, dto: SermonDto): Sermon = Sermon(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        date = dto.date ?: Date(),
        notes = (dto.notes as? String).orEmpty(),
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}
