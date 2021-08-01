package jp.dosukoi.ui.viewmodel.top

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import jp.dosukoi.data.repository.auth.AuthRepository
import jp.dosukoi.ui.viewmodel.common.assertType
import jp.dosukoi.ui.viewmodel.common.test
import jp.dosukoi.ui.viewmodel.common.testRule
import jp.dosukoi.ui.viewmodel.top.MainViewModel.Event
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var authRepository: AuthRepository

    @InjectMockKs
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onLoginButtonClick() {
        // given
        val event = viewModel.onEvent.test()

        // when
        viewModel.onLoginButtonClick()

        // then
        assertThat(event.lastValue()).isInstanceOf(Event.NavigateToChrome::class.java)
    }

    @Test
    fun onCardClick() {
        // given
        val event = viewModel.onEvent.test()

        // when
        viewModel.onCardClick(MOCK_URL)

        // then
        assertType<Event.NavigateToChrome>(event.lastValue()) {
            assertThat(this.url).isEqualTo(MOCK_URL)
        }
    }

    @Test
    fun onRepositoryItemClick() {
        // given
        val event = viewModel.onEvent.test()

        // when
        viewModel.onRepositoryItemClick(MOCK_URL)

        // then
        assertType<Event.NavigateToChrome>(event.lastValue()) {
            assertThat(this.url).isEqualTo(MOCK_URL)
        }
    }

    @Test
    fun onSearchedItemClick() {
        // given
        val event = viewModel.onEvent.test()

        // when
        viewModel.onSearchedItemClick(MOCK_URL)

        // then
        assertType<Event.NavigateToChrome>(event.lastValue()) {
            assertThat(this.url).isEqualTo(MOCK_URL)
        }
    }

    @Test
    fun onGetCode_success() {
        // given
        val event = viewModel.onEvent.test()
        coEvery { authRepository.getAccessToken(any()) } returns Unit

        // when
        viewModel.onGetCode("")

        // then
        assertThat(event.lastValue()).isEqualTo(Event.CompleteGetAccessToken)
    }

    @Test
    fun onGetCode_failure() {
        // given
        val event = viewModel.onEvent.test()
        coEvery { authRepository.getAccessToken(any()) } throws RuntimeException()

        // when
        viewModel.onGetCode("")

        // then
        assertThat(event.lastValue()).isInstanceOf(Event.FailedFetch::class.java)
    }

    companion object {
        private const val MOCK_URL = "https://example.com"
    }
}