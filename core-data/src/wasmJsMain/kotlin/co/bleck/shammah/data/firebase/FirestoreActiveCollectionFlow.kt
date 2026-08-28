@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.firebase

import co.bleck.shammah.data.firebase.js.ShammahFirebaseJs
import co.bleck.shammah.data.firebase.js.jsAnyToKotlinString
import co.bleck.shammah.data.repository.closeOnFirestoreError
import co.bleck.shammah.data.repository.isFirestorePermissionDenied
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.JsonObject
import kotlin.js.ExperimentalWasmJsInterop

/**
 * Live "isActive == true" collection subscription via Firebase JS `onSnapshot`.
 */
internal fun <T> firestoreActiveCollectionFlow(
    collection: String,
    mapDocument: (id: String, data: JsonObject) -> T?,
): Flow<List<T>> = callbackFlow {
    val unsubscribe = ShammahFirebaseJs.subscribeActiveCollection(
        collectionName = collection,
        onNext = { docsJson ->
            val docs = runCatching { parseFirestoreDocumentsJson(jsAnyToKotlinString(docsJson)) }
                .getOrElse { emptyList() }
            val items = docs.mapNotNull { (id, data) ->
                runCatching { mapDocument(id, data) }.getOrNull()
            }
            trySend(items)
        },
        onError = { error ->
            val message = jsAnyToKotlinString(error)
            val throwable = Exception(message)
            if (isFirestorePermissionDenied(throwable) ||
                message.contains("permission-denied", ignoreCase = true)
            ) {
                trySend(emptyList())
            }
            closeOnFirestoreError(throwable)
        },
    )
    awaitClose {
        unsubscribe()
    }
}
