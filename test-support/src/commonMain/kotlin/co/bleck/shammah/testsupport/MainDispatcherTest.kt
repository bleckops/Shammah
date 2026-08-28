package co.bleck.shammah.testsupport

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Sets [Dispatchers.Main] for ViewModel `viewModelScope` in common tests.
 * Subclasses that override [setUp]/[tearDown] must call super.
 */
@OptIn(ExperimentalCoroutinesApi::class)
open class MainDispatcherTest {
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    open fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    open fun tearDown() {
        Dispatchers.resetMain()
    }
}
