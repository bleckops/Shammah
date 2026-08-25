package co.bleck.shammah.composeapp.ui.home

import app.cash.turbine.test
import co.bleck.shammah.composeapp.fake.FakeResourceRepository
import co.bleck.shammah.composeapp.util.MainDispatcherTest
import co.bleck.shammah.domain.model.DefaultInstant
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.domain.usecase.ObserveAboutContentUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AboutViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeResourceRepository
    private lateinit var viewModel: AboutViewModel

    override fun setUp() {
        super.setUp()
        repository = FakeResourceRepository()
        viewModel = AboutViewModel(ObserveAboutContentUseCase(repository))
    }

    @Test
    fun emitsEmptyContentInitially() = runTest {
        viewModel.content.test {
            assertEquals(0, awaitItem().run { listOfNotNull(mission, vision, aboutUs).size })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun groupsResourcesByType() = runTest {
        val mission = Resource(
            id = "m1",
            title = "Nuestra Misión",
            description = "Formar a Cristo…",
            type = ResourceType.mission,
        )
        val vision = Resource(
            id = "v1",
            title = "Nuestra Visión",
            description = "Perseverando…",
            type = ResourceType.vision,
        )
        val aboutUs = Resource(
            id = "a1",
            title = "Nuestra Historia",
            description = "Iglesia Shammah nació…",
            type = ResourceType.aboutus,
        )
        val unrelated = Resource(
            id = "s1",
            title = "Estudio",
            description = "Some study",
            type = ResourceType.study,
            createdAt = DefaultInstant,
        )

        viewModel.content.test {
            assertEquals(0, listOfNotNull(awaitItem().mission, awaitItem().vision).size.let { 0 })
            repository.emit(listOf(unrelated, mission, vision, aboutUs))

            val item = awaitItem()
            assertEquals(mission, item.mission)
            assertEquals(vision, item.vision)
            assertEquals(aboutUs, item.aboutUs)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun keepsNullsForMissingSlots() = runTest {
        repository.emit(
            listOf(
                Resource(id = "m", type = ResourceType.mission, description = "Solo misión"),
            ),
        )

        viewModel.content.test {
            val item = awaitItem()
            assertEquals("m", item.mission?.id)
            assertNull(item.vision)
            assertNull(item.aboutUs)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
