package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.fake.FakeResourceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMissionVisionAboutUsResourcesUseCaseTest {
    private val repository = FakeResourceRepository()
    private val useCase = GetMissionVisionAboutUsResourcesUseCase(repository)

    @Test
    fun returnEmptyWhenRepositoryIsEmpty() = runTest {
        assertTrue(useCase().first().isEmpty())
    }

    @Test
    fun returnsResourcesEmittedByRepository() = runTest {
        repository.emit(
            listOf(
                Resource(
                    id = "r1",
                    title = "Church's mission",
                    description = "This is the Church's mission",
                    isActive = true,
                    type = ResourceType.mission,
                ),
            ),
        )

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("r1", result[0].id)
        assertEquals("Church's mission", result[0].title)
        assertEquals(ResourceType.mission, result[0].type)
    }

    @Test
    fun propagatesFlowUpdatesFromRepository() = runTest {
        repository.emit(listOf(Resource(id = "r1", title = "Mission", type = ResourceType.mission)))
        assertEquals(1, useCase().first().size)

        repository.emit(
            listOf(
                Resource(id = "r1", title = "Mission", type = ResourceType.mission),
                Resource(id = "r2", title = "Vision", type = ResourceType.vision),
            ),
        )
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("r2", updated[1].id)
    }

    @Test
    fun returnOnlyActiveResources() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", isActive = false, type = ResourceType.mission),
                Resource(id = "r2", title = "Second Resource", isActive = true, type = ResourceType.mission),
                Resource(id = "r3", title = "Third Resource", isActive = false, type = ResourceType.mission),
            ),
        )
        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("r2", result[0].id)
    }

    @Test
    fun resourceNoContainsOtherTypes() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", type = ResourceType.study),
                Resource(id = "r2", title = "Second Resource", type = ResourceType.reflection),
                Resource(id = "r3", title = "Third Resource", type = ResourceType.study),
            ),
        )
        val result = useCase().first()
        assertEquals(0, result.size)
    }

    @Test
    fun resourceContainsOnlyMissionVisionOrAboutUsTypes() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", type = ResourceType.mission),
                Resource(id = "r2", title = "Second Resource", type = ResourceType.vision),
                Resource(id = "r3", title = "Third Resource", type = ResourceType.aboutus),
                Resource(id = "r4", title = "Fourth Resource", type = ResourceType.study),
            ),
        )
        val result = useCase().first()
        assertEquals(3, result.size)
    }

}