package co.bleck.shammah.composeapp.ui.home

import app.cash.turbine.test
import co.bleck.shammah.testsupport.FakeBannerRepository
import co.bleck.shammah.testsupport.MainDispatcherTest
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeBannerRepository
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        repository = FakeBannerRepository()
        viewModel = HomeViewModel(GetBannersUseCase(repository))
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
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
