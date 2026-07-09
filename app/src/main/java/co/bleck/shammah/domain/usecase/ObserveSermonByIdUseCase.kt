package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveSermonByIdUseCase @Inject constructor(
    private val repository: SermonRepository
) {
    operator fun invoke(sermonId: String): Flow<Sermon?> =
        repository.getSermons().map { sermons -> sermons.find { it.id == sermonId } }
}
