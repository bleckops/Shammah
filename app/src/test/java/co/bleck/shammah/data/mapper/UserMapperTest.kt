package co.bleck.shammah.data.mapper

import co.bleck.shammah.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class UserMapperTest {

    @Test
    fun `maps firebase user to domain user`() {
        val firebaseUser = mock<com.google.firebase.auth.FirebaseUser> {
            on { uid } doReturn "uid-123"
            on { isAnonymous } doReturn true
        }

        val result = UserMapper.toDomain(firebaseUser)

        assertEquals(User(id = "uid-123", isAnonymous = true), result)
    }
}
