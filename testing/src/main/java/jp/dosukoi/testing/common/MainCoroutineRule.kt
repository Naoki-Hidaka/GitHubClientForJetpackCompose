package jp.dosukoi.testing.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

fun testRule(): TestRule = RuleChain.outerRule(InstantTaskExecutorRule())
    .around(MainCoroutineRule())

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        testDispatcher.advanceUntilIdle()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
