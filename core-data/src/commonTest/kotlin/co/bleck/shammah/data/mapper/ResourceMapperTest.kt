@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.ResourceDto
import co.bleck.shammah.domain.model.ResourceType
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ResourceMapperTest {
    @Test
    fun mapsNullFieldsToDefaults() {
        val result = ResourceMapper.toDomain("r1", ResourceDto())

        assertEquals("r1", result.id)
        assertEquals("", result.title)
        assertEquals("", result.description)
        assertEquals(ResourceType.reflection, result.type)
        assertNull(result.url)
        assertEquals(true, result.isActive)
    }

    @Test
    fun mapsPopulatedFields() {
        val instant = Instant.parse("2025-01-15T12:00:00Z")
        val dto = ResourceDto(
            title = "Nuestra Misión",
            description = "Formar a Cristo en cada uno…",
            type = "mission",
            url = "https://example.com",
            isActive = true,
            createdAt = instant,
            updatedAt = instant,
        )

        val result = ResourceMapper.toDomain("r2", dto)

        assertEquals("Nuestra Misión", result.title)
        assertEquals("Formar a Cristo en cada uno…", result.description)
        assertEquals(ResourceType.mission, result.type)
        assertEquals("https://example.com", result.url)
        assertEquals(instant, result.createdAt)
    }

    @Test
    fun parsesAllKnownResourceTypes() {
        val types = listOf(
            "reflection" to ResourceType.reflection,
            "study" to ResourceType.study,
            "mission" to ResourceType.mission,
            "vision" to ResourceType.vision,
            "aboutus" to ResourceType.aboutus,
        )
        types.forEach { (raw, expected) ->
            val result = ResourceMapper.toDomain("r", ResourceDto(type = raw))
            assertEquals(expected, result.type, "type=$raw")
        }
    }

    @Test
    fun fallsBackToReflectionForUnknownType() {
        val result = ResourceMapper.toDomain("r", ResourceDto(type = "something-else"))
        assertEquals(ResourceType.reflection, result.type)
    }

    @Test
    fun coercesNonStringFieldsToEmpty() {
        val dto = ResourceDto(
            title = 42,
            description = true,
            type = 3.14,
        )

        val result = ResourceMapper.toDomain("r3", dto)

        assertEquals("", result.title)
        assertEquals("", result.description)
        assertEquals(ResourceType.reflection, result.type)
    }
}
