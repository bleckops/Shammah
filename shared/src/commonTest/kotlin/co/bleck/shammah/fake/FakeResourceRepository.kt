package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.repository.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeResourceRepository(initialResources: List<Resource> = emptyList()) : ResourceRepository {
    private val _resources = MutableStateFlow(initialResources)

    fun emit(resources: List<Resource>) {
        _resources.value = resources
    }

    override fun getResources(): Flow<List<Resource>> = _resources.asStateFlow()
}
