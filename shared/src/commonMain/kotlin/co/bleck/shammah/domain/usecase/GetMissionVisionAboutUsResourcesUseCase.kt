package co.bleck.shammah.domain.usecase


import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.domain.repository.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class GetMissionVisionAboutUsResourcesUseCase(private val repository: ResourceRepository) {
    private val requiredResources = listOf(
        ResourceType.vision,
        ResourceType.aboutus,
        ResourceType.mission
    )
    operator fun invoke(): Flow<List<Resource>> = repository
        .getResources()
        .map { resources ->
            resources.filter { it.isActive }
                .filter { requiredResources.contains(it.type) }
        }
}