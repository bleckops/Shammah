package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Banner
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class BannerRepositoryImplTest {

    // --- Mocks ---
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollection: CollectionReference
    private lateinit var mockQuery: Query
    private lateinit var mockRegistration: ListenerRegistration

    // Captured listener so tests can drive it directly
    private lateinit var capturedListener: EventListener<QuerySnapshot>

    @Before
    fun setUp() {
        mockRegistration = mock()
        mockQuery = mock {
            on { addSnapshotListener(any<EventListener<QuerySnapshot>>()) } doAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                capturedListener = invocation.getArgument(0) as EventListener<QuerySnapshot>
                mockRegistration
            }
        }
        mockCollection = mock {
            on { whereEqualTo("isActive", true) } doReturn mockQuery
        }
        mockFirestore = mock {
            on { collection("banners") } doReturn mockCollection
        }
    }

    private fun makeSnapshot(documents: List<DocumentSnapshot>): QuerySnapshot = mock {
        on { this.documents } doReturn documents
    }

    private fun makeDocument(banner: Banner): DocumentSnapshot = mock {
        on { id } doReturn banner.id
        on { toObject(Banner::class.java) } doReturn banner
    }

    // -------------------------------------------------------------------------

    @Test
    fun `emits mapped banner list on snapshot`() = runTest {
        val repo = BannerRepositoryImpl(mockFirestore)
        val banner = Banner(
            id = "b1",
            title = "Test Banner",
            imageUrl = "https://img.example.com/1.jpg",
            isActive = true,
            createdAt = Date(0),
            updatedAt = Date(0)
        )

        var result: List<Banner>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getBanners().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument(banner))), null)
        job.join()

        assertEquals(1, result?.size)
        assertEquals("b1", result?.get(0)?.id)
        assertEquals("Test Banner", result?.get(0)?.title)
    }

    @Test
    fun `emits empty list when snapshot has no documents`() = runTest {
        val repo = BannerRepositoryImpl(mockFirestore)

        var result: List<Banner>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getBanners().first()
        }

        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        assertTrue("Expected empty list", result?.isEmpty() == true)
    }

    @Test
    fun `sanitizes null title and imageUrl with empty strings`() = runTest {
        val repo = BannerRepositoryImpl(mockFirestore)
        // Banner with default empty strings (simulates null from Firestore)
        val banner = Banner(id = "b1", title = "", imageUrl = "", isActive = true)

        var result: List<Banner>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getBanners().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument(banner))), null)
        job.join()

        assertEquals("", result?.get(0)?.title)
        assertEquals("", result?.get(0)?.imageUrl)
    }

    @Test
    fun `closes flow when listener receives an error`() = runTest {
        val repo = BannerRepositoryImpl(mockFirestore)
        val error: FirebaseFirestoreException = mock {
            on { message } doReturn "Permission denied"
        }

        var thrown: Throwable? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            try {
                repo.getBanners().first()
            } catch (e: Throwable) {
                thrown = e
            }
        }

        capturedListener.onEvent(null, error)
        job.join()

        assertEquals("Permission denied", thrown?.message)
    }

    @Test
    fun `removes listener registration when flow is cancelled`() = runTest {
        val repo = BannerRepositoryImpl(mockFirestore)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.getBanners().first()
        }

        // Trigger a snapshot so `first()` completes
        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        verify(mockRegistration).remove()
    }
}
