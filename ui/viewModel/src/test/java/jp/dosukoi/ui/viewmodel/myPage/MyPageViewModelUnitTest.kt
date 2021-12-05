package jp.dosukoi.ui.viewmodel.myPage

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.usecase.myPage.GetMyPageUseCase
import jp.dosukoi.data.usecase.myPage.GetRepositoriesUseCase
import jp.dosukoi.testing.common.assertType
import jp.dosukoi.testing.common.test
import jp.dosukoi.testing.common.testRule
import jp.dosukoi.ui.viewmodel.common.LoadState
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel.MyPageState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageViewModelUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var getMyPageUseCase: GetMyPageUseCase

    @RelaxedMockK
    private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

    @RelaxedMockK
    private lateinit var myPageListener: MyPageListener

    @InjectMockKs
    private lateinit var viewModel: MyPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { getMyPageUseCase.execute() } returns UserStatus.Authenticated(mockk())
        coEvery { getRepositoriesUseCase.execute() } returns listOf(
            mockk(),
            mockk(),
            mockk()
        )
    }

    @Test
    fun init_success_authenticated() {
        // given
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.init()

        // then
        assertType<LoadState.Loaded<MyPageState>>(myPageState.lastValue()) {
            assertThat(this.data.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
            assertThat(this.data.repositoryList.size).isEqualTo(3)
        }
    }

    @Test
    fun init_success_un_authenticated() {
        // given
        val myPageState = viewModel.myPageState.test()
        coEvery { getMyPageUseCase.execute() } returns UserStatus.UnAuthenticated

        // when
        viewModel.init()

        // then
        assertType<LoadState.Loaded<MyPageState>>(myPageState.lastValue()) {
            assertThat(this.data.userStatus).isEqualTo(UserStatus.UnAuthenticated)
        }
    }

    @Test
    fun onRetryClick() {
        // given
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.onRetryClick()

        // then
        assertThat(myPageState.values()).isEqualTo(
            listOf(
                LoadState.Loading,
                LoadState.Loaded(mockk<MyPageState>(relaxed = true))
            )
        )
        myPageState.values().apply {
            assertThat(this[0]).isEqualTo(LoadState.Loading)
            assertType<LoadState.Loaded<MyPageState>>(this[1]) {
                assertThat(this.data.repositoryList.size).isEqualTo(3)
            }
        }
    }

    @Test
    fun onRefresh() {
        // given
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.onRefresh()

        // then
        myPageState.values().apply {

        }
    }
}
