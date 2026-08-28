@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.dto

import kotlinx.datetime.Instant

data class SermonDto(
    val title: Any? = null,
    val description: Any? = null,
    val date: Instant? = null,
    val notes: Any? = null,
    val isActive: Boolean = true,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
