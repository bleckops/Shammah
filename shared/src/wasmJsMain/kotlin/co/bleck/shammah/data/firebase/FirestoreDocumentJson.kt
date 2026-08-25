@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.firebase

import co.bleck.shammah.data.dto.BannerDto
import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.data.dto.ResourceDto
import co.bleck.shammah.data.dto.SermonDto
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

internal val firebaseWebJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

internal fun parseFirebaseUserJson(json: String): Pair<String, Boolean> {
    val obj = firebaseWebJson.parseToJsonElement(json).jsonObject
    val uid = obj["uid"]?.jsonPrimitive?.contentOrNull
        ?: error("Usuario nulo tras autenticación")
    val isAnonymous = obj["isAnonymous"]?.jsonPrimitive?.booleanOrNull ?: true
    return uid to isAnonymous
}

/**
 * Parses the JSON array emitted by `ShammahFirebase.subscribeActiveCollection`.
 * Shape: `[{ "id": "...", "data": { ...fields } }, ...]`
 */
internal fun parseFirestoreDocumentsJson(json: String): List<Pair<String, JsonObject>> {
    val array: JsonArray = firebaseWebJson.parseToJsonElement(json).jsonArray
    return array.mapNotNull { element ->
        val doc = element as? JsonObject ?: return@mapNotNull null
        val id = doc["id"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
        val data = doc["data"]?.jsonObject ?: JsonObject(emptyMap())
        id to data
    }
}

internal fun JsonObject.toBannerDto(): BannerDto = BannerDto(
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

internal fun JsonObject.toEventDto(): EventDto = EventDto(
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

internal fun JsonObject.toResourceDto(): ResourceDto = ResourceDto(
    title = stringField("title"),
    description = stringField("description"),
    type = stringField("type"),
    url = stringField("url"),
    isActive = boolField("isActive", default = true),
    createdAt = instantField("createdAt"),
    updatedAt = instantField("updatedAt"),
)

internal fun JsonObject.toSermonDto(): SermonDto = SermonDto(
    title = stringField("title"),
    description = stringField("description"),
    date = instantField("date"),
    notes = stringField("notes"),
    isActive = boolField("isActive", default = true),
    createdAt = instantField("createdAt"),
    updatedAt = instantField("updatedAt"),
)

private fun JsonObject.field(name: String): JsonElement? = this[name]

private fun JsonObject.stringField(name: String): String? {
    val value = field(name) ?: return null
    return when (value) {
        is JsonPrimitive -> value.contentOrNull
        else -> value.toString()
    }
}

private fun JsonObject.intField(name: String, default: Int = 0): Int {
    val value = field(name) ?: return default
    if (value is JsonPrimitive) {
        value.longOrNull?.let { return it.toInt() }
        value.doubleOrNull?.let { return it.toInt() }
        value.contentOrNull?.toIntOrNull()?.let { return it }
    }
    return default
}

private fun JsonObject.boolField(name: String, default: Boolean): Boolean {
    val value = field(name) ?: return default
    if (value is JsonPrimitive) {
        value.booleanOrNull?.let { return it }
        value.contentOrNull?.toBooleanStrictOrNull()?.let { return it }
    }
    return default
}

private fun JsonObject.instantField(name: String): Instant? {
    val value = field(name) ?: return null
    // Serialized Firestore Timestamp: { "seconds": N, "nanoseconds": N }
    if (value is JsonObject) {
        val seconds = value["seconds"]?.jsonPrimitive?.longOrNull
            ?: value["seconds"]?.jsonPrimitive?.contentOrNull?.toLongOrNull()
            ?: return null
        val nanos = value["nanoseconds"]?.jsonPrimitive?.longOrNull
            ?: value["nanoseconds"]?.jsonPrimitive?.contentOrNull?.toLongOrNull()
            ?: 0L
        return Instant.fromEpochSeconds(seconds, nanos)
    }
    if (value is JsonPrimitive) {
        val text = value.contentOrNull ?: return null
        return runCatching { Instant.parse(text) }.getOrNull()
    }
    return null
}
