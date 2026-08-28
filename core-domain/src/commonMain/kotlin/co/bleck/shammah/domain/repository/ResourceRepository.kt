package co.bleck.shammah.domain.repository

import co.bleck.shammah.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    /**
     * Streams the active documents in the `resources` collection.
     *
     * Only documents with `isActive == true` are emitted, matching the behaviour of the
     * other Firestore-backed repositories. Ordering is left to Firestore — consumers
     * that need a specific document should filter by [Resource.type].
     */
    fun getResources(): Flow<List<Resource>>
}
