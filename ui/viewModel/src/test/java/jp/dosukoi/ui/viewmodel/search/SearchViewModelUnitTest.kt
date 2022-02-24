package jp.dosukoi.ui.viewmodel.search

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import jp.dosukoi.data.entity.search.Search
import jp.dosukoi.data.usecase.search.GetSearchDataUseCase
import jp.dosukoi.testing.common.testRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var searchPageListener: SearchPageListener

    @RelaxedMockK
    private lateinit var getSearchDataUseCase: GetSearchDataUseCase

    @InjectMockKs
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { getSearchDataUseCase.execute(any(), any()) } returns Search(
            totalCount = 3,
            incompleteResults = true,
            items = listOf(
                mockk(),
                mockk(),
                mockk()
            )
        )
    }

    @Test
    fun onSearchWordChanged() = runBlockingTest {
        // given
        val searchUiState = mutableListOf<SearchUiState>()
        val job = viewModel.searchUiState.onEach {
            searchUiState.add(it)
        }.launchIn(this)

        // when
        viewModel.onSearchWordChanged("kotlin")
        viewModel.onSearchWordChanged("java")

        // then
        assertThat(searchUiState[1].searchWord).isEqualTo("kotlin")
        assertThat(searchUiState[2].searchWord).isEqualTo("java")
        job.cancel()
    }
}
