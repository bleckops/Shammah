package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.fake.FakeEventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetEventsUseCaseTest {

    private lateinit var repository: FakeEventRepository
    private lateinit var useCase: GetEventsUseCase

    @Before
    fun setUp() {
        repository = FakeEventRepository()
        useCase = GetEventsUseCase(repository)
    }

    @Test
    fun `returns empty list when repository has no events`() = runTest {
        val result = useCase().first()
        assertTrue("Expected empty list", result.isEmpty())
    }

    @Test
    fun `returns events emitted by repository`() = runTest {
        val events = listOf(
            Event(
                id = "e1",
                title = "Youth Retreat",
                type = EventType.retreat,
                date = Date(),
                isActive = true
            )
        )
        repository.emit(events)

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
        assertEquals("Youth Retreat", result[0].title)
        assertEquals(EventType.retreat, result[0].type)
    }

    @Test
    fun `propagates flow updates from repository`() = runTest {
        val first = listOf(Event(id = "e1", title = "First"))
        val second = listOf(
            Event(id = "e1", title = "First"),
            Event(id = "e2", title = "Second")
        )

        repository.emit(first)
        assertEquals(1, useCase().first().size)

        repository.emit(second)
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("e2", updated[1].id)
    }

    @Test
    fun `event with birthday type is preserved`() = runTest {
        val event = Event(id = "e1", title = "John's Birthday", type = EventType.birthdays)
        repository.emit(listOf(event))

        val result = useCase().first()
        assertEquals(EventType.birthdays, result[0].type)
    }

    @Test
    fun `event with empty time string is preserved`() = runTest {
        val event = Event(id = "e1", title = "All-day Event", time = "")
        repository.emit(listOf(event))

        val result = useCase().first()
        assertEquals("", result[0].time)
    }
}
