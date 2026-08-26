package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.fake.FakeResourceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPublicResourcesUseCaseTest {
    private val repository = FakeResourceRepository()
    private val useCase = GetPublicResourcesUseCase(repository)

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
                    title = "Psalms devotional",
                    description = "This is Psalms devotional from Chapter 1 to 150",
                    isActive = true,
                    type = ResourceType.study,
                ),
            ),
        )

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("r1", result[0].id)
        assertEquals("Psalms devotional", result[0].title)
        assertEquals(ResourceType.study, result[0].type)
    }

    @Test
    fun propagatesFlowUpdatesFromRepository() = runTest {
        repository.emit(listOf(Resource(id = "r1", title = "First Resource")))
        assertEquals(1, useCase().first().size)

        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource"),
                Resource(id = "r2", title = "Second Resource"),
            ),
        )
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("r2", updated[1].id)
    }

    @Test
    fun returnOnlyActiveAndPublicResources() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", isActive = false, type = ResourceType.study),
                Resource(id = "r2", title = "Second Resource", isActive = true, type = ResourceType.study),
                Resource(id = "r3", title = "Third Resource", isActive = false, type = ResourceType.study),
            ),
        )
        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("r2", result[0].id)
    }

    @Test
    fun publicResourceContainsNoMissionVisionNorAboutUs() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", type = ResourceType.mission),
                Resource(id = "r2", title = "Second Resource", type = ResourceType.vision),
                Resource(id = "r3", title = "Third Resource", type = ResourceType.aboutus),
            ),
        )
        val result = useCase().first()
        assertEquals(0, result.size)
    }

    @Test
    fun publicResourceContainsAllOtherTypes() = runTest {
        repository.emit(
            listOf(
                Resource(id = "r1", title = "First Resource", type = ResourceType.study),
                Resource(id = "r2", title = "Second Resource", type = ResourceType.reflection),
                Resource(id = "r3", title = "Third Resource", type = ResourceType.study),
                Resource(id = "r4", title = "Fourth Resource", type = ResourceType.mission),
                Resource(id = "r5", title = "Fifth Resource", type = ResourceType.vision),
                Resource(id = "r6", title = "Sixth Resource", type = ResourceType.aboutus)
            ),
        )
        val result = useCase().first()
        assertEquals(3, result.size)
    }
}