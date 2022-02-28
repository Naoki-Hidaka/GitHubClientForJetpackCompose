package jp.dosukoi.ui.viewmodel.myPage

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.usecase.auth.GetAccessTokenUseCase
import jp.dosukoi.data.usecase.myPage.GetRepositoriesUseCase
import jp.dosukoi.data.usecase.myPage.GetUserStatusUseCase
import jp.dosukoi.testing.common.assertType
import jp.dosukoi.testing.common.testRule
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPageViewModelUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var getUserStatusUseCase: GetUserStatusUseCase

    @RelaxedMockK
    private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

    @RelaxedMockK
    private lateinit var getAccessTokenUseCase: GetAccessTokenUseCase

    @InjectMockKs
    private lateinit var viewModel: MyPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { getUserStatusUseCase.execute() } returns UserStatus.Authenticated(mockk())
        coEvery { getRepositoriesUseCase.execute() } returns listOf(
            mockk(),
            mockk(),
            mockk()
        )
    }

    @Test
    fun init_success_authenticated() = runBlockingTest {
        // given
        val myPageState = mutableListOf<MyPageUiState>()
        val job = viewModel.myPageState.onEach {
            myPageState.add(it)
        }.launchIn(this)

        // when
        viewModel.init()

        // then
        assertThat(myPageState[0].screenState).isEqualTo(LoadState.Loading)
        assertType<LoadState.Loaded<MyPageScreenState>>(myPageState[1].screenState) {
            assertThat(this.data.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
            assertThat(this.data.repositoryList.size).isEqualTo(3)
        }
        job.cancel()
    }

    @Test
    fun init_success_un_authenticated() = runBlockingTest {
        // given
        coEvery { getUserStatusUseCase.execute() } returns UserStatus.UnAuthenticated
        val myPageState = mutableListOf<MyPageUiState>()
        val job = viewModel.myPageState.onEach {
            myPageState.add(it)
        }.launchIn(this)

        // when
        viewModel.init()

        // then
        assertThat(myPageState[0].screenState).isEqualTo(LoadState.Loading)
        assertType<LoadState.Loaded<MyPageScreenState>>(myPageState[1].screenState) {
            assertThat(this.data.userStatus).isEqualTo(UserStatus.UnAuthenticated)
        }
        job.cancel()
    }

    @Test
    fun init_failure() = runBlockingTest {
        // given
        coEvery { getUserStatusUseCase.execute() } throws RuntimeException()
        val myPageState = mutableListOf<MyPageUiState>()
        val job = viewModel.myPageState.onEach {
            myPageState.add(it)
        }.launchIn(this)

        // when
        viewModel.init()

        // then
        assertThat(myPageState[0].screenState).isEqualTo(LoadState.Loading)
        assertThat(myPageState[1].screenState).isInstanceOf(LoadState.Error::class.java)

        job.cancel()
    }

    @Test
    fun onRetryClick() = runBlockingTest {
        // given
        val myPageState = mutableListOf<MyPageUiState>()
        val job = viewModel.myPageState.onEach {
            myPageState.add(it)
        }.launchIn(this)

        // when
        viewModel.onRetryClick()

        // then
        assertThat(myPageState[0].screenState).isEqualTo(LoadState.Loading)
        assertType<LoadState.Loaded<MyPageScreenState>>(myPageState[1].screenState) {
            assertThat(this.data.repositoryList.size).isEqualTo(3)
        }
        job.cancel()
    }

    @Test
    fun onRefresh() = runBlockingTest {
        // given
        coEvery { getRepositoriesUseCase.execute() } returnsMany listOf(
            listOf(
                mockk(),
                mockk(),
                mockk()
            ),
            listOf(
                mockk(),
                mockk(),
                mockk(),
                mockk()
            )
        )
        val myPageState = mutableListOf<MyPageUiState>()
        val job = viewModel.myPageState.onEach {
            myPageState.add(it)
        }.launchIn(this)

        // when
        viewModel.init()
        viewModel.onRefresh()

        // then
        assertThat(myPageState[0].screenState).isEqualTo(LoadState.Loading)
        assertType<LoadState.Loaded<MyPageScreenState>>(myPageState[1].screenState) {
            assertThat(this.data.repositoryList.size).isEqualTo(3)
        }
        assertThat(myPageState[2].isRefreshing).isTrue()
        myPageState[3].also {
            assertThat(it.isRefreshing).isFalse()
            assertType<LoadState.Loaded<MyPageScreenState>>(it.screenState) {
                assertThat(this.data.repositoryList.size).isEqualTo(4)
            }
        }

        job.cancel()
    }
}
