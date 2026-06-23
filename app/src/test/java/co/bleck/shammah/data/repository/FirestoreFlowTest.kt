package co.bleck.shammah.data.repository

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreFlowTest {

    @Test
    fun `detects permission denied from message`() {
        val error = RuntimeException("PERMISSION_DENIED: Missing or insufficient permissions.")
        assertTrue(isFirestorePermissionDenied(error))
    }

    @Test
    fun `detects permission denied from cause message`() {
        val error = RuntimeException(
            "Firestore error",
            RuntimeException("PERMISSION_DENIED: Missing or insufficient permissions.")
        )
        assertTrue(isFirestorePermissionDenied(error))
    }

    @Test
    fun `does not treat other errors as permission denied`() {
        val error = RuntimeException("Unavailable")
        assertFalse(isFirestorePermissionDenied(error))
    }
}
