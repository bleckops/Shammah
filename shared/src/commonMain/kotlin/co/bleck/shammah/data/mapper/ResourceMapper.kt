@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.ResourceDto
import co.bleck.shammah.domain.model.DefaultInstant
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType

object ResourceMapper {
    fun toDomain(id: String, dto: ResourceDto): Resource = Resource(
        id = id,
        title = (dto.title as? String).orEmpty(),
        description = (dto.description as? String).orEmpty(),
        type = parseResourceType(dto.type),
        url = dto.url as? String,
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: DefaultInstant,
        updatedAt = dto.updatedAt ?: DefaultInstant,
    )

    private fun parseResourceType(value: Any?): ResourceType {
        val raw = (value as? String)?.lowercase() ?: return ResourceType.reflection
        return ResourceType.entries.firstOrNull { it.name == raw } ?: ResourceType.reflection
    }
}
