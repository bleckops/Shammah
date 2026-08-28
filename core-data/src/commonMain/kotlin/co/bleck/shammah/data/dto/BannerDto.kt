@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.dto

import kotlinx.datetime.Instant

data class BannerDto(
    val imageUrl: Any? = null,
    val title: Any? = null,
    val linkUrl: Any? = null,
    val audioUrl: Any? = null,
    val category: Any? = null,
    val speaker: Any? = null,
    val videoUrl: Any? = null,
    val order: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
