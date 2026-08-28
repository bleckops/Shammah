@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.BannerDto
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BannerMapperTest {
    @Test
    fun mapsNullFieldsToDefaults() {
        val result = BannerMapper.toDomain("b1", BannerDto())

        assertEquals("b1", result.id)
        assertEquals("", result.imageUrl)
        assertEquals("", result.title)
        assertNull(result.linkUrl)
        assertEquals("", result.audioUrl)
        assertEquals(0, result.order)
    }

    @Test
    fun mapsPopulatedFields() {
        val instant = Instant.parse("2025-01-15T12:00:00Z")
        val dto = BannerDto(
            imageUrl = "https://img",
            title = "Welcome",
            linkUrl = "https://link",
            audioUrl = "https://audio",
            category = "sermon",
            speaker = "Pastor",
            videoUrl = "https://video",
            order = 3,
            isActive = true,
            createdAt = instant,
            updatedAt = instant,
        )

        val result = BannerMapper.toDomain("b2", dto)

        assertEquals("Welcome", result.title)
        assertEquals("https://link", result.linkUrl)
        assertEquals(3, result.order)
        assertEquals(instant, result.createdAt)
    }

    @Test
    fun coercesNonStringFieldsToEmpty() {
        val dto = BannerDto(
            title = 42,
            imageUrl = true,
            linkUrl = 3.14,
        )

        val result = BannerMapper.toDomain("b3", dto)

        assertEquals("", result.title)
        assertEquals("", result.imageUrl)
        assertNull(result.linkUrl)
    }
}
