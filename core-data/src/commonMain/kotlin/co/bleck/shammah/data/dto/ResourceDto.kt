@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.dto

import kotlinx.datetime.Instant

/**
 * Mirrors the `resources` Firestore collection.
 *
 * Fields use [Any?] defensively: GitLive's [dev.gitlive.firebase.firestore.get] reifies the
 * target type and throws when the stored value isn't a [String]. Keeping DTO fields as
 * [Any?] lets [FirestoreDocumentMapping] decode each field with a typed helper, then the
 * mapper normalises them into the strongly-typed [co.bleck.shammah.domain.model.Resource].
 */
data class ResourceDto(
    val title: Any? = null,
    val description: Any? = null,
    val type: Any? = null,
    val url: Any? = null,
    val isActive: Boolean = true,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
