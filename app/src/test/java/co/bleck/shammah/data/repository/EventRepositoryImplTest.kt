package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.EventType
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
class EventRepositoryImplTest {

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
            on { collection("events") } doReturn mockCollection
        }
    }

    private fun makeSnapshot(documents: List<DocumentSnapshot>): QuerySnapshot = mock {
        on { this.documents } doReturn documents
    }

    private fun makeDocument(id: String, dto: EventDto): DocumentSnapshot = mock {
        on { this.id } doReturn id
        on { toObject(EventDto::class.java) } doReturn dto
    }

    @Test
    fun `emits mapped event list on snapshot`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)
        val dto = EventDto(
            title = "Youth Camp",
            type = EventType.camp,
            date = Date(0),
            isActive = true
        )

        var result: List<co.bleck.shammah.domain.model.Event>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getEvents().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("e1", dto))), null)
        job.join()

        assertEquals(1, result?.size)
        assertEquals("e1", result?.get(0)?.id)
        assertEquals(EventType.camp, result?.get(0)?.type)
    }

    @Test
    fun `emits empty list when snapshot has no documents`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)

        var result: List<co.bleck.shammah.domain.model.Event>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getEvents().first()
        }

        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        assertTrue("Expected empty list", result?.isEmpty() == true)
    }

    @Test
    fun `defaults EventType to social when type field is null`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)
        val dto = EventDto(title = "Unnamed", type = null)

        var result: List<co.bleck.shammah.domain.model.Event>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getEvents().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("e1", dto))), null)
        job.join()

        assertEquals(EventType.social, result?.get(0)?.type)
    }

    @Test
    fun `sanitizes null title and location with empty strings`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)
        val dto = EventDto(title = null, location = null, isActive = true)

        var result: List<co.bleck.shammah.domain.model.Event>? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            result = repo.getEvents().first()
        }

        capturedListener.onEvent(makeSnapshot(listOf(makeDocument("e1", dto))), null)
        job.join()

        assertEquals("", result?.get(0)?.title)
        assertEquals("", result?.get(0)?.location)
    }

    @Test
    fun `closes flow when listener receives an error`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)
        val error: FirebaseFirestoreException = mock {
            on { message } doReturn "Unavailable"
        }

        var thrown: Throwable? = null
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            try {
                repo.getEvents().first()
            } catch (e: Throwable) {
                thrown = e
            }
        }

        capturedListener.onEvent(null, error)
        job.join()

        assertEquals("Unavailable", thrown?.message)
    }

    @Test
    fun `removes listener registration when flow is cancelled`() = runTest {
        val repo = EventRepositoryImpl(mockFirestore)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.getEvents().first()
        }

        capturedListener.onEvent(makeSnapshot(emptyList()), null)
        job.join()

        verify(mockRegistration).remove()
    }
}
