package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSermonByIdUseCase(private val repository: SermonRepository) {
    operator fun invoke(sermonId: String): Flow<Sermon?> =
        repository.getSermons().map { sermons -> sermons.find { it.id == sermonId } }
}
