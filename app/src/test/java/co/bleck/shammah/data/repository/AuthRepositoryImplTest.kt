package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var capturedAuthListener: FirebaseAuth.AuthStateListener

    @Before
    fun setUp() {
        mockAuth = mock {
            on { currentUser } doReturn null
            on { addAuthStateListener(any()) } doAnswer { invocation ->
                capturedAuthListener = invocation.getArgument(0)
                mock()
            }
        }
    }

    @Test
    fun `initial currentUser is null when no firebase user`() = runTest {
        val repo = AuthRepositoryImpl(mockAuth)

        assertNull(repo.currentUser.value)
    }

    @Test
    fun `auth state listener updates current user`() = runTest {
        val repo = AuthRepositoryImpl(mockAuth)
        val firebaseUser = mock<FirebaseUser> {
            on { uid } doReturn "uid-1"
            on { isAnonymous } doReturn true
        }
        val mockFirebaseAuth = mock<FirebaseAuth> {
            on { currentUser } doReturn firebaseUser
        }

        capturedAuthListener.onAuthStateChanged(mockFirebaseAuth)

        assertEquals(User(id = "uid-1", isAnonymous = true), repo.currentUser.value)
    }

    @Test
    fun `signInAnonymously returns user on success`() = runTest {
        val firebaseUser = mock<FirebaseUser> {
            on { uid } doReturn "anon-1"
            on { isAnonymous } doReturn true
        }
        val authResult = mock<AuthResult> {
            on { user } doReturn firebaseUser
        }
        lateinit var task: Task<AuthResult>
        task = mock {
            on { isSuccessful } doReturn true
            on { result } doReturn authResult
            on { addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } doAnswer { invocation ->
                invocation.getArgument<OnCompleteListener<AuthResult>>(0).onComplete(task)
                task
            }
        }
        val authWithSignIn = mock<FirebaseAuth> {
            on { currentUser } doReturn null
            on { addAuthStateListener(any()) } doAnswer { invocation ->
                capturedAuthListener = invocation.getArgument(0)
                mock()
            }
            on { signInAnonymously() } doReturn task
        }

        val repo = AuthRepositoryImpl(authWithSignIn)
        val result = repo.signInAnonymously()

        assertTrue(result.isSuccess)
        assertEquals(User(id = "anon-1", isAnonymous = true), result.getOrNull())
    }

    @Test
    fun `signInAnonymously returns failure on task error`() = runTest {
        val exception = Exception("Auth failed")
        lateinit var task: Task<AuthResult>
        task = mock {
            on { isSuccessful } doReturn false
            on { getException() } doReturn exception
            on { addOnCompleteListener(any<OnCompleteListener<AuthResult>>()) } doAnswer { invocation ->
                invocation.getArgument<OnCompleteListener<AuthResult>>(0).onComplete(task)
                task
            }
        }
        val authWithSignIn = mock<FirebaseAuth> {
            on { currentUser } doReturn null
            on { addAuthStateListener(any()) } doAnswer { invocation ->
                capturedAuthListener = invocation.getArgument(0)
                mock()
            }
            on { signInAnonymously() } doReturn task
        }

        val repo = AuthRepositoryImpl(authWithSignIn)
        val result = repo.signInAnonymously()

        assertTrue(result.isFailure)
        assertEquals("Auth failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signOut delegates to firebase auth`() {
        val repo = AuthRepositoryImpl(mockAuth)

        repo.signOut()

        verify(mockAuth).signOut()
    }
}
