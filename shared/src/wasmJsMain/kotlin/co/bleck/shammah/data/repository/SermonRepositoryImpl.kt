package co.bleck.shammah.data.repository

import co.bleck.shammah.data.firebase.firestoreActiveCollectionFlow
import co.bleck.shammah.data.firebase.toSermonDto
import co.bleck.shammah.data.mapper.SermonMapper
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow

class SermonRepositoryImpl : SermonRepository {
    override fun getSermons(): Flow<List<Sermon>> = firestoreActiveCollectionFlow("sermons") { id, data ->
        SermonMapper.toDomain(id, data.toSermonDto())
    }
}
