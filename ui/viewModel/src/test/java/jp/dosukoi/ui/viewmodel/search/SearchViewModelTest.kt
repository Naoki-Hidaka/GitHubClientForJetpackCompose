package jp.dosukoi.ui.viewmodel.search

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import jp.dosukoi.data.entity.common.LoadState
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.search.Search
import jp.dosukoi.data.entity.search.SearchPageState
import jp.dosukoi.data.usecase.search.GetSearchDataUseCase
import jp.dosukoi.ui.viewmodel.common.test
import jp.dosukoi.ui.viewmodel.common.testRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var searchPageListener: SearchPageListener

    @RelaxedMockK
    private lateinit var getSearchDataUseCase: GetSearchDataUseCase

    @InjectMockKs
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onSearchWordChanged() {
        // given
        val searchWord = viewModel.searchWord.test()
        viewModel.searchWord.value = "Java"

        // when
        viewModel.onSearchWordChanged("Kotlin")

        // then
        assertThat(searchWord.lastValue()).isEqualTo("Kotlin")
    }

    @Test
    fun onSearchButtonClick_success_has_more() {
        // given
        val loadState = viewModel.loadState.test()
        val hasMore = viewModel.hasMore.test()
        val searchData = viewModel.searchData.test()
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } returns Unit
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
        assertThat(hasMore.lastValue()).isTrue()
        assertThat(searchData.lastValue()).isInstanceOf(SearchPageState.Data::class.java)
    }

    @Test
    fun onSearchButtonClick_success_no_more() {
        // given
        val loadState = viewModel.loadState.test()
        val hasMore = viewModel.hasMore.test()
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } returns Unit
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
        assertThat(hasMore.lastValue()).isFalse()
    }

    @Test
    fun onSearchButtonClick_success_empty() {
        // given
        val searchData = viewModel.searchData.test()
        val loadState = viewModel.loadState.test()
        val hasMore = viewModel.hasMore.test()
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } returns Unit
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(searchData.lastValue()).isEqualTo(SearchPageState.Empty)
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
        assertThat(hasMore.lastValue()).isFalse()
    }

    @Test
    fun onSearchButtonClick_failure() {
        // given
        val loadState = viewModel.loadState.test()
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } throws RuntimeException()
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.ERROR)
    }

    @Test
    fun onSearchButtonClick_already_loaded_failure() {
        // given
        val loadState = viewModel.loadState.test()
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } throws RuntimeException()
        viewModel.searchWord.value = "hoge"
        viewModel.loadState.value = LoadState.LOADED

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
    }

    @Test
    fun onSearchButtonClick_search_word_empty() {
        // given
        val isError = viewModel.isError.test()
        viewModel.searchWord.value = ""

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(isError.lastValue()).isTrue()
    }

    @Test
    fun onScrollEnd() {
        // given
        viewModel.isLoadingMore.set(false)
        coEvery {
            getSearchDataUseCase.execute(
                any(),
                any(),
                any()
            )
        } returns Unit

        // when
        viewModel.onScrollEnd()

        // then
        coVerify { getSearchDataUseCase.execute(any(), any(), any()) }
        assertThat(viewModel.isLoadingMore.get()).isFalse()
    }

    companion object {
        private val MOCK_REPO_LIST = listOf(
            Repository(
                0, "hoge", null, "https://example.com"
            ),
            Repository(
                1, "foo", null, "https://example.com"
            ),
        )

        private val MOCK_SEARCH_RESULT_HAS_MORE =
            Search(
                99,
                false,
                MOCK_REPO_LIST
            )

        private val MOCK_SEARCH_RESULT_NO_MORE =
            Search(
                2, true, MOCK_REPO_LIST
            )

        private val MOCK_SEARCH_RESULT_EMPTY =
            Search(0, true, emptyList())
    }
}
