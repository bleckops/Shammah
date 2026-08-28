@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.dto

import kotlinx.datetime.Instant

data class EventDto(
    val title: Any? = null,
    val description: Any? = null,
    val date: Instant? = null,
    val time: Any? = null,
    val location: Any? = null,
    val imageUrl: Any? = null,
    val type: Any? = null,
    val isActive: Boolean = true,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
