package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.SermonDto
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
class SermonRepositoryImplTest {

    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollection: CollectionReference
    private lateinit var mockQuery: Query
    private lateinit var mockRegistration: ListenerRegistration
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
            on { collection("sermons") } doReturn mockCollection
        }
    }

    private fun makeSnapshot(documents: List<DocumentSnapshot>): QuerySnapshot = mock {
        on { this.documents } doReturn documents
    }

    private fun makeDocument(id: String, dto: SermonDto): DocumentSnapshot = mock {
        on { this.id } doReturn id
        on { toObject(SermonDto::class.java) } doReturn dto
    }

    @Test
    fun `emits mapped sermon list on snapshot`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)
        val dto = SermonDto(
            title = "Walking by Faith",
            description = "2 Corinthians 5:7",
            date = Date(0),
            isActive = true
        )

        var result: List<co.bleck.shammah.domain.model.Sermon>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getSermons().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("s1", dto))), null)
        job.join()

        assertEquals(1, result?.size)
        assertEquals("s1", result?.get(0)?.id)
        assertEquals("Walking by Faith", result?.get(0)?.title)
    }

    @Test
    fun `emits empty list when snapshot has no documents`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)

        var result: List<co.bleck.shammah.domain.model.Sermon>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getSermons().first()
        }

        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        assertTrue("Expected empty list", result?.isEmpty() == true)
    }

    @Test
    fun `sanitizes null title and description with empty strings`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)
        val dto = SermonDto(title = null, description = null, isActive = true)

        var result: List<co.bleck.shammah.domain.model.Sermon>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getSermons().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("s1", dto))), null)
        job.join()

        assertEquals("", result?.get(0)?.title)
        assertEquals("", result?.get(0)?.description)
    }

    @Test
    fun `sanitizes null notes with empty string`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)
        val dto = SermonDto(title = "No Notes", notes = null, isActive = true)

        var result: List<co.bleck.shammah.domain.model.Sermon>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getSermons().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("s1", dto))), null)
        job.join()

        assertEquals("", result?.get(0)?.notes)
    }

    @Test
    fun `closes flow when listener receives an error`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)
        val error: FirebaseFirestoreException = mock {
            on { message } doReturn "Firestore error"
        }

        var thrown: Throwable? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            try {
                repo.getSermons().first()
            } catch (e: Throwable) {
                thrown = e
            }
        }

        capturedListener.onEvent(null, error)
        job.join()

        assertEquals("Firestore error", thrown?.message)
    }

    @Test
    fun `removes listener registration when flow is cancelled`() = runTest {
        val repo = SermonRepositoryImpl(mockFirestore)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.getSermons().first()
        }

        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        verify(mockRegistration).remove()
    }
}
