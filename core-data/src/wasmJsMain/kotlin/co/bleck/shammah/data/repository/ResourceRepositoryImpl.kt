package co.bleck.shammah.data.repository

import co.bleck.shammah.data.firebase.firestoreActiveCollectionFlow
import co.bleck.shammah.data.firebase.toResourceDto
import co.bleck.shammah.data.mapper.ResourceMapper
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.repository.ResourceRepository
import kotlinx.coroutines.flow.Flow

class ResourceRepositoryImpl : ResourceRepository {
    override fun getResources(): Flow<List<Resource>> = firestoreActiveCollectionFlow("resources") { id, data ->
        ResourceMapper.toDomain(id, data.toResourceDto())
    }
}
