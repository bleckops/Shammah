package co.bleck.shammah.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

internal fun <T> firestoreActiveCollectionFlow(
    collection: String,
    mapDocument: (id: String, document: DocumentSnapshot) -> T?,
): Flow<List<T>> = callbackFlow {
    val job = launch {
        try {
            Firebase.firestore
                .collection(collection)
                .where { "isActive" equalTo true }
                .snapshots
                .collect { snapshot ->
                    val items = snapshot.documents.mapNotNull { document ->
                        runCatching { mapDocument(document.id, document) }.getOrNull()
                    }
                    trySend(items)
                }
        } catch (error: Throwable) {
            // Emit empty so UI leaves the "loading" shimmer instead of hanging forever.
            if (isFirestorePermissionDenied(error)) {
                trySend(emptyList())
            }
            closeOnFirestoreError(error)
        }
    }
    awaitClose { job.cancel() }
}
