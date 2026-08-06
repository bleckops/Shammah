package co.bleck.shammah.composeapp.ui.home

import app.cash.turbine.test
import co.bleck.shammah.composeapp.fake.FakeBannerRepository
import co.bleck.shammah.composeapp.util.MainDispatcherTest
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeBannerRepository
    private lateinit var viewModel: HomeViewModel

    override fun setUp() {
        super.setUp()
        repository = FakeBannerRepository()
        viewModel = HomeViewModel(GetBannersUseCase(repository))
    }

    @Test
    fun loadsBannersFromUseCase() = runTest {
        val banners = listOf(Banner(id = "b1", title = "Welcome"))

        viewModel.banners.test {
            assertEquals(emptyList(), awaitItem())
            repository.emit(banners)
            assertEquals(banners, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
