package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.fake.FakeEventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetEventsUseCaseTest {
    private val repository = FakeEventRepository()
    private val useCase = GetEventsUseCase(repository)

    @Test
    fun returnsEmptyListWhenRepositoryHasNoEvents() = runTest {
        assertTrue(useCase().first().isEmpty())
    }

    @Test
    fun returnsEventsEmittedByRepository() = runTest {
        repository.emit(
            listOf(
                Event(
                    id = "e1",
                    title = "Youth Retreat",
                    type = EventType.retreat,
                    isActive = true,
                ),
            ),
        )

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
        assertEquals("Youth Retreat", result[0].title)
        assertEquals(EventType.retreat, result[0].type)
    }

    @Test
    fun propagatesFlowUpdatesFromRepository() = runTest {
        repository.emit(listOf(Event(id = "e1", title = "First")))
        assertEquals(1, useCase().first().size)

        repository.emit(
            listOf(
                Event(id = "e1", title = "First"),
                Event(id = "e2", title = "Second"),
            ),
        )
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("e2", updated[1].id)
    }

    @Test
    fun eventWithBirthdayTypeIsPreserved() = runTest {
        repository.emit(
            listOf(Event(id = "e1", title = "John's Birthday", type = EventType.birthdays)),
        )
        assertEquals(EventType.birthdays, useCase().first()[0].type)
    }

    @Test
    fun eventWithEmptyTimeStringIsPreserved() = runTest {
        repository.emit(listOf(Event(id = "e1", title = "All-day Event", time = "")))
        assertEquals("", useCase().first()[0].time)
    }
}
