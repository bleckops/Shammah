@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.BannerDto
import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.data.dto.SermonDto
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant

internal fun DocumentSnapshot.toBannerDto(): BannerDto = BannerDto(
    imageUrl = stringField("imageUrl"),
    title = stringField("title"),
    linkUrl = stringField("linkUrl"),
    audioUrl = stringField("audioUrl"),
    category = stringField("category"),
    speaker = stringField("speaker"),
    videoUrl = stringField("videoUrl"),
    order = intField("order"),
    isActive = boolField("isActive", default = true),
    createdAt = instantField("createdAt"),
    updatedAt = instantField("updatedAt"),
)

internal fun DocumentSnapshot.toSermonDto(): SermonDto = SermonDto(
    title = stringField("title"),
    description = stringField("description"),
    date = instantField("date"),
    notes = stringField("notes"),
    isActive = boolField("isActive", default = true),
    createdAt = instantField("createdAt"),
    updatedAt = instantField("updatedAt"),
)

internal fun DocumentSnapshot.toEventDto(): EventDto = EventDto(
    title = stringField("title"),
    description = stringField("description"),
    date = instantField("date"),
    time = stringField("time"),
    location = stringField("location"),
    imageUrl = stringField("imageUrl"),
    type = stringField("type"),
    isActive = boolField("isActive", default = true),
    createdAt = instantField("createdAt"),
    updatedAt = instantField("updatedAt"),
)

/**
 * GitLive decode requires a concrete reified type. [get]<[Any]> fails at runtime
 * (no serializer for Any), which previously dropped every mapped document.
 */
private fun DocumentSnapshot.stringField(name: String): String? {
    if (!contains(name)) return null
    return runCatching { get<String>(name) }.getOrNull()
}

private fun DocumentSnapshot.intField(name: String, default: Int = 0): Int {
    if (!contains(name)) return default
    runCatching { get<Int>(name) }.getOrNull()?.let { return it }
    runCatching { get<Long>(name) }.getOrNull()?.let { return it.toInt() }
    runCatching { get<Double>(name) }.getOrNull()?.let { return it.toInt() }
    return default
}

private fun DocumentSnapshot.boolField(name: String, default: Boolean): Boolean {
    if (!contains(name)) return default
    return runCatching { get<Boolean>(name) }.getOrNull() ?: default
}

private fun DocumentSnapshot.instantField(name: String): Instant? {
    if (!contains(name)) return null
    return runCatching {
        val timestamp = get<Timestamp>(name)
        Instant.fromEpochSeconds(timestamp.seconds, timestamp.nanoseconds.toLong())
    }.getOrNull()
}
