package co.bleck.shammah.data.repository

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FirestoreFlowTest {
    @Test
    fun detectsPermissionDeniedFromMessage() {
        val error = RuntimeException("PERMISSION_DENIED: Missing or insufficient permissions.")
        assertTrue(isFirestorePermissionDenied(error))
    }

    @Test
    fun detectsPermissionDeniedFromCauseMessage() {
        val error = RuntimeException("Firestore error", RuntimeException("PERMISSION_DENIED"))
        assertTrue(isFirestorePermissionDenied(error))
    }

    @Test
    fun doesNotTreatOtherErrorsAsPermissionDenied() {
        assertFalse(isFirestorePermissionDenied(RuntimeException("Unavailable")))
    }
}
