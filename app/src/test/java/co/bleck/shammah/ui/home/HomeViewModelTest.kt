package co.bleck.shammah.ui.home

import app.cash.turbine.test
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import co.bleck.shammah.fake.FakeBannerRepository
import co.bleck.shammah.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private lateinit var repository: FakeBannerRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeBannerRepository()
        viewModel = HomeViewModel(GetBannersUseCase(repository))
    }

    @Test
    fun `loads banners from use case`() = runTest {
        val banners = listOf(Banner(id = "b1", title = "Welcome"))

        viewModel.banners.test {
            assertEquals(emptyList<Banner>(), awaitItem())
            repository.emit(banners)
            assertEquals(banners, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
