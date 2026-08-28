package co.bleck.shammah.domain.repository

import co.bleck.shammah.domain.model.Sermon
import kotlinx.coroutines.flow.Flow

interface SermonRepository {
    fun getSermons(): Flow<List<Sermon>>
}
