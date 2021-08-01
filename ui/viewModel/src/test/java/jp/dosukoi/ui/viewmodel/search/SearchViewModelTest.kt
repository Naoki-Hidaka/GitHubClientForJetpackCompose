package jp.dosukoi.ui.viewmodel.search

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.search.Search
import jp.dosukoi.data.repository.search.SearchRepository
import jp.dosukoi.ui.viewmodel.common.LoadState
import jp.dosukoi.ui.viewmodel.common.assertType
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
    private lateinit var searchRepository: SearchRepository

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
        coEvery {
            searchRepository.findRepositories(
                any(),
                any()
            )
        } returns MOCK_SEARCH_RESULT_HAS_MORE
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertType<LoadState.Loaded<SearchViewModel.State.Data>>(loadState.lastValue()) {
            assertType<SearchViewModel.State.Data>(this.value) {
                assertThat(this.repositoryList).isEqualTo(MOCK_REPO_LIST)
            }
        }

        assertThat(hasMore.lastValue()).isTrue()
    }

    @Test
    fun onSearchButtonClick_success_no_more() {
        // given
        val loadState = viewModel.loadState.test()
        val hasMore = viewModel.hasMore.test()
        coEvery {
            searchRepository.findRepositories(
                any(),
                any()
            )
        } returns MOCK_SEARCH_RESULT_NO_MORE
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertType<LoadState.Loaded<SearchViewModel.State.Data>>(loadState.lastValue()) {
            assertType<SearchViewModel.State.Data>(this.value) {
                assertThat(this.repositoryList).isEqualTo(MOCK_REPO_LIST)
            }
        }

        assertThat(hasMore.lastValue()).isFalse()
    }

    @Test
    fun onSearchButtonClick_success_empty() {
        // given
        val loadState = viewModel.loadState.test()
        val hasMore = viewModel.hasMore.test()
        coEvery {
            searchRepository.findRepositories(
                any(),
                any()
            )
        } returns MOCK_SEARCH_RESULT_EMPTY
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertType<LoadState.Loaded<SearchViewModel.State.Empty>>(loadState.lastValue()) {
            assertThat(this.value).isEqualTo(SearchViewModel.State.Empty)
        }

        assertThat(hasMore.lastValue()).isFalse()
    }

    @Test
    fun onSearchButtonClick_failure() {
        // given
        val loadState = viewModel.loadState.test()
        coEvery { searchRepository.findRepositories(any(), any()) } throws RuntimeException()
        viewModel.searchWord.value = "hoge"

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.Error)
    }

    @Test
    fun onSearchButtonClick_already_loaded_failure() {
        // given
        val loadState = viewModel.loadState.test()
        coEvery { searchRepository.findRepositories(any(), any()) } throws RuntimeException()
        viewModel.searchWord.value = "hoge"
        viewModel.loadState.value = LoadState.Loaded(SearchViewModel.State.Empty)

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.Error)
    }

    @Test
    fun onSearchButtonClick_search_word_empty() {
        // given
        val isError = viewModel.isError.test()
        coEvery { searchRepository.findRepositories(any(), any()) } throws RuntimeException()
        viewModel.searchWord.value = ""

        // when
        viewModel.onSearchButtonClick()

        // then
        assertThat(isError.lastValue()).isTrue()
    }

    @Test
    fun onScrollEnd() {
        // given
        viewModel.hasMore.value = true
        viewModel.isLoadingMore.set(false)
        coEvery {
            searchRepository.findRepositories(
                any(),
                any()
            )
        } returns MOCK_SEARCH_RESULT_NO_MORE

        // when
        viewModel.onScrollEnd()

        // then
        coVerify { searchRepository.findRepositories(any(), any()) }
        assertThat(viewModel.isLoadingMore.get()).isFalse()
    }

    companion object {
        private val MOCK_REPO_LIST = listOf(
            Repository(
                0, "hoge", null, "https://example.com"
            ),
            Repository(
                1, "foo", null, "https://example.com"
            )
        )

        private val MOCK_SEARCH_RESULT_HAS_MORE =
            Search(
                2,
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