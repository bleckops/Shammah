package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.fake.FakeBannerRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetBannersUseCaseTest {
    private val repository = FakeBannerRepository()
    private val useCase = GetBannersUseCase(repository)

    @Test
    fun returnsEmptyListWhenRepositoryHasNoBanners() = runTest {
        assertTrue(useCase().first().isEmpty())
    }

    @Test
    fun returnsBannersEmittedByRepository() = runTest {
        repository.emit(
            listOf(
                Banner(
                    id = "b1",
                    title = "Sunday Service",
                    imageUrl = "https://example.com/img.jpg",
                    isActive = true,
                ),
            ),
        )

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("b1", result[0].id)
        assertEquals("Sunday Service", result[0].title)
    }

    @Test
    fun propagatesFlowUpdatesFromRepository() = runTest {
        repository.emit(listOf(Banner(id = "b1", title = "First")))
        assertEquals(1, useCase().first().size)

        repository.emit(
            listOf(
                Banner(id = "b1", title = "First"),
                Banner(id = "b2", title = "Second"),
            ),
        )
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("b2", updated[1].id)
    }

    @Test
    fun bannerWithNullLinkUrlIsPreservedAsNull() = runTest {
        repository.emit(listOf(Banner(id = "b1", title = "Banner", linkUrl = null)))
        assertNull(useCase().first()[0].linkUrl)
    }
}
