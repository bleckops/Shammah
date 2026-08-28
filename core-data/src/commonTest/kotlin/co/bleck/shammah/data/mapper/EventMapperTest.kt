@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class EventMapperTest {
    @Test
    fun defaultsNullTypeToSocial() {
        val result = EventMapper.toDomain("e1", EventDto(type = null))

        assertEquals(EventType.social, result.type)
        assertEquals("", result.title)
        assertEquals("", result.location)
    }

    @Test
    fun mapsBirthdayEventFromStringType() {
        val date = Instant.fromEpochMilliseconds(86_400_000L)
        val dto = EventDto(
            title = "Birthday",
            type = "birthdays",
            date = date,
            time = "10:00",
            location = "Church",
        )

        val result = EventMapper.toDomain("e2", dto)

        assertEquals("Birthday", result.title)
        assertEquals(EventType.birthdays, result.type)
        assertEquals("10:00", result.time)
        assertEquals("Church", result.location)
        assertEquals(date, result.date)
    }

    @Test
    fun mapsUnknownOrMixedCaseType() {
        assertEquals(EventType.camp, EventMapper.toDomain("e3", EventDto(type = "CAMP")).type)
        assertEquals(EventType.social, EventMapper.toDomain("e4", EventDto(type = "unknown")).type)
        assertEquals(EventType.social, EventMapper.toDomain("e5", EventDto(type = 5)).type)
    }
}
