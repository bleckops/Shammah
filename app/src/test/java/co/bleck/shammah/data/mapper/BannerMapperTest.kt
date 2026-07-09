package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.BannerDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class BannerMapperTest {

    @Test
    fun `maps null fields to defaults`() {
        val dto = BannerDto()

        val result = BannerMapper.toDomain("b1", dto)

        assertEquals("b1", result.id)
        assertEquals("", result.imageUrl)
        assertEquals("", result.title)
        assertNull(result.linkUrl)
        assertEquals("", result.audioUrl)
    }

    @Test
    fun `maps populated fields`() {
        val date = Date()
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
            createdAt = date,
            updatedAt = date
        )

        val result = BannerMapper.toDomain("b2", dto)

        assertEquals("Welcome", result.title)
        assertEquals("https://link", result.linkUrl)
        assertEquals(3, result.order)
    }
}
