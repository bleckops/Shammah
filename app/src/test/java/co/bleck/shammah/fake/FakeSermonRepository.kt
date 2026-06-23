package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSermonRepository(initialSermons: List<Sermon> = emptyList()) : SermonRepository {

    private val _sermons = MutableStateFlow(initialSermons)

    /** Emit a new list to simulate a Firestore snapshot update. */
    fun emit(sermons: List<Sermon>) {
        _sermons.value = sermons
    }

    override fun getSermons(): Flow<List<Sermon>> = _sermons.asStateFlow()
}
