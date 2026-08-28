@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.testsupport.FakeEventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObserveEventByIdUseCaseTest {
    private val repository = FakeEventRepository()
    private val useCase = ObserveEventByIdUseCase(repository)

    private fun dateAt(year: Int, month: Int, day: Int) =
        LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun returnsMatchingEventById() = runTest {
        repository.emit(
            listOf(
                Event(id = "e1", title = "First",  type = EventType.prayer, date = dateAt(2025, 8, 1)),
                Event(id = "e2", title = "Second", type = EventType.social, date = dateAt(2025, 9, 5)),
            ),
        )
        val result = useCase("e2").first()
        assertEquals("e2", result?.id)
        assertEquals("Second", result?.title)
    }

    @Test
    fun returnsNullWhenEventNotFound() = runTest {
        repository.emit(listOf(Event(id = "e1", title = "Only", date = dateAt(2025, 8, 1))))
        val result = useCase("missing").first()
        assertNull(result)
    }
}
