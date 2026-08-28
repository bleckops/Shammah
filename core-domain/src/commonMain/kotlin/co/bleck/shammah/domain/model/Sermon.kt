@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.model

import kotlinx.datetime.Instant

data class Sermon(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Instant = DefaultInstant,
    val notes: String = "",
    val isActive: Boolean = true,
    val createdAt: Instant = DefaultInstant,
    val updatedAt: Instant = DefaultInstant,
)
