package co.bleck.shammah.data.repository

import kotlinx.coroutines.channels.ProducerScope

internal fun isFirestorePermissionDenied(error: Throwable): Boolean {
    var current: Throwable? = error
    while (current != null) {
        val message = current.message.orEmpty()
        if (message.contains("PERMISSION_DENIED", ignoreCase = true) ||
            message.contains("permission-denied", ignoreCase = true)
        ) {
            return true
        }
        current = current.cause
    }
    return false
}

internal fun <T> ProducerScope<T>.closeOnFirestoreError(error: Throwable) {
    if (isFirestorePermissionDenied(error)) {
        close()
    } else {
        close(error)
    }
}
