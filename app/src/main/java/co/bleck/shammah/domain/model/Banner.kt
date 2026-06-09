package co.bleck.shammah.domain.model

import java.util.Date

data class Banner(
    val id: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val linkUrl: String? = null,
    val audioUrl: String = "",
    val category: String = "",
    val speaker: String = "",
    val videoUrl: String = "",
    val order: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
