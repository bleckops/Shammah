package co.bleck.shammah.data.dto

import java.util.Date

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
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
