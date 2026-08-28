@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.model

import kotlinx.datetime.Instant

enum class ResourceType {
    reflection,
    study,
    mission,
    vision,
    aboutus,
}

data class Resource(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: ResourceType = ResourceType.reflection,
    val url: String? = null,
    val isActive: Boolean = true,
    val createdAt: Instant = DefaultInstant,
    val updatedAt: Instant = DefaultInstant,
)
