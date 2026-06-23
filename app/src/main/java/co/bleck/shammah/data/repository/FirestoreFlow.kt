package co.bleck.shammah.data.repository

import kotlinx.coroutines.channels.ProducerScope

internal fun isFirestorePermissionDenied(error: Throwable): Boolean {
    var current: Throwable? = error
    while (current != null) {
        if (current.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true) {
            return true
        }
        current = current.cause
    }
    return false
}

/**
 * Closes a Firestore [callbackFlow] collector. Permission-denied errors are treated as a
 * normal close because they are expected when auth is revoked (e.g. sign-out) while
 * listeners are still winding down.
 */
internal fun <T> ProducerScope<T>.closeOnFirestoreError(error: Throwable) {
    if (isFirestorePermissionDenied(error)) {
        close()
    } else {
        close(error)
    }
}
