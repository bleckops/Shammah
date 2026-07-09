package co.bleck.shammah.data.dto

import java.util.Date

data class SermonDto(
    val title: Any? = null,
    val description: Any? = null,
    val date: Date? = null,
    val notes: Any? = null,
    val isActive: Boolean = true,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
