package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSermonRepository(initialSermons: List<Sermon> = emptyList()) : SermonRepository {
    private val sermons = MutableStateFlow(initialSermons)

    fun emit(value: List<Sermon>) {
        sermons.value = value
    }

    override fun getSermons(): Flow<List<Sermon>> = sermons.asStateFlow()
}
