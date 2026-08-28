@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.SermonDto
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SermonMapperTest {
    @Test
    fun mapsAllFieldsWithDefaultsForNulls() {
        val dto = SermonDto(
            title = null,
            description = null,
            date = null,
            notes = null,
            isActive = true,
            createdAt = null,
            updatedAt = null,
        )

        val result = SermonMapper.toDomain("s1", dto)

        assertEquals("s1", result.id)
        assertEquals("", result.title)
        assertEquals("", result.description)
        assertEquals("", result.notes)
        assertTrue(result.isActive)
    }

    @Test
    fun mapsStringFieldsCorrectly() {
        val date = Instant.fromEpochMilliseconds(1_000L)
        val dto = SermonDto(
            title = "Faith",
            description = "John 3:16",
            date = date,
            notes = "Note",
            isActive = false,
            createdAt = date,
            updatedAt = date,
        )

        val result = SermonMapper.toDomain("s2", dto)

        assertEquals("Faith", result.title)
        assertEquals("John 3:16", result.description)
        assertEquals("Note", result.notes)
        assertEquals(date, result.date)
        assertFalse(result.isActive)
    }
}
