package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow

class GetSermonsUseCase(private val repository: SermonRepository) {
    operator fun invoke(): Flow<List<Sermon>> = repository.getSermons()
}
