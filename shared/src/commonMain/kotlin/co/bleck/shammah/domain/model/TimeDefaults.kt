@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.model

import kotlinx.datetime.Instant

internal val DefaultInstant: Instant = Instant.parse("1970-01-01T00:00:00Z")
