package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.fake.FakeBannerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetBannersUseCaseTest {

    private lateinit var repository: FakeBannerRepository
    private lateinit var useCase: GetBannersUseCase

    @Before
    fun setUp() {
        repository = FakeBannerRepository()
        useCase = GetBannersUseCase(repository)
    }

    @Test
    fun `returns empty list when repository has no banners`() = runTest {
        val result = useCase().first()
        assertTrue("Expected empty list", result.isEmpty())
    }

    @Test
    fun `returns banners emitted by repository`() = runTest {
        val banners = listOf(
            Banner(
                id = "b1",
                title = "Sunday Service",
                imageUrl = "https://example.com/img.jpg",
                isActive = true,
                createdAt = Date(),
                updatedAt = Date()
            )
        )
        repository.emit(banners)

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("b1", result[0].id)
        assertEquals("Sunday Service", result[0].title)
    }

    @Test
    fun `propagates flow updates from repository`() = runTest {
        val first = listOf(Banner(id = "b1", title = "First"))
        val second = listOf(
            Banner(id = "b1", title = "First"),
            Banner(id = "b2", title = "Second")
        )

        // Initial state
        repository.emit(first)
        val initial = useCase().first()
        assertEquals(1, initial.size)

        // Update state
        repository.emit(second)
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("b2", updated[1].id)
    }

    @Test
    fun `banner with null linkUrl is preserved as null`() = runTest {
        val banner = Banner(id = "b1", title = "Banner", linkUrl = null)
        repository.emit(listOf(banner))

        val result = useCase().first()
        assertTrue(result[0].linkUrl == null)
    }
}
