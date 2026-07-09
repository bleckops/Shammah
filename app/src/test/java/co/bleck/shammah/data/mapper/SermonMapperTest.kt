package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.SermonDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class SermonMapperTest {

    @Test
    fun `maps all fields with defaults for nulls`() {
        val dto = SermonDto(
            title = null,
            description = null,
            date = null,
            notes = null,
            isActive = true,
            createdAt = null,
            updatedAt = null
        )

        val result = SermonMapper.toDomain("s1", dto)

        assertEquals("s1", result.id)
        assertEquals("", result.title)
        assertEquals("", result.description)
        assertEquals("", result.notes)
        assertTrue(result.isActive)
    }

    @Test
    fun `maps string fields correctly`() {
        val date = Date(1_000L)
        val dto = SermonDto(
            title = "Faith",
            description = "John 3:16",
            date = date,
            notes = "Note",
            isActive = false,
            createdAt = date,
            updatedAt = date
        )

        val result = SermonMapper.toDomain("s2", dto)

        assertEquals("Faith", result.title)
        assertEquals("John 3:16", result.description)
        assertEquals("Note", result.notes)
        assertEquals(date, result.date)
        assertEquals(false, result.isActive)
    }
}
