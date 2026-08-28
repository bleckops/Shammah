@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.SermonDto
import co.bleck.shammah.domain.model.DefaultInstant
import co.bleck.shammah.domain.model.Sermon

object SermonMapper {
    fun toDomain(id: String, dto: SermonDto): Sermon = Sermon(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        date = dto.date ?: DefaultInstant,
        notes = (dto.notes as? String).orEmpty(),
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: DefaultInstant,
        updatedAt = dto.updatedAt ?: DefaultInstant,
    )
}
