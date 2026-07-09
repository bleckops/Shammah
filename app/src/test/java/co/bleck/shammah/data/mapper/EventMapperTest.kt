package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.EventType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class EventMapperTest {

    @Test
    fun `defaults null type to social`() {
        val dto = EventDto(type = null)

        val result = EventMapper.toDomain("e1", dto)

        assertEquals(EventType.social, result.type)
        assertEquals("", result.title)
        assertEquals("", result.location)
    }

    @Test
    fun `maps birthday event`() {
        val date = Date(86_400_000L)
        val dto = EventDto(
            title = "Birthday",
            type = EventType.birthdays,
            date = date,
            time = "10:00",
            location = "Church"
        )

        val result = EventMapper.toDomain("e2", dto)

        assertEquals("Birthday", result.title)
        assertEquals(EventType.birthdays, result.type)
        assertEquals("10:00", result.time)
        assertEquals("Church", result.location)
    }
}
