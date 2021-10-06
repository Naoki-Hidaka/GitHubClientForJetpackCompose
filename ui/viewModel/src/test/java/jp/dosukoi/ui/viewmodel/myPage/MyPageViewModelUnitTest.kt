package jp.dosukoi.ui.viewmodel.myPage

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import jp.dosukoi.data.entity.common.LoadState
import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.usecase.myPage.GetMyPageUseCase
import jp.dosukoi.testing.common.assertType
import jp.dosukoi.testing.common.test
import jp.dosukoi.testing.common.testRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageViewModelUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var getMyPageUseCase: GetMyPageUseCase

    @RelaxedMockK
    private lateinit var myPageListener: MyPageListener

    @InjectMockKs
    private lateinit var viewModel: MyPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { getMyPageUseCase.execute() } returns MyPageState(
            UserStatus.Authenticated(mockk()),
            listOf(
                mockk(),
                mockk(),
                mockk()
            )
        )
    }

    @Test
    fun init_success_authenticated() {
        // given
        val loadState = viewModel.loadState.test()
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.init()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
        assertType<MyPageState>(myPageState.lastValue()) {
            assertThat(this.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
            assertThat(this.repositoryList.size).isEqualTo(3)
        }
    }

    @Test
    fun init_success_un_authenticated() {
        // given
        val loadState = viewModel.loadState.test()
        val myPageState = viewModel.myPageState.test()
        coEvery { getMyPageUseCase.execute() } returns MyPageState(
            UserStatus.UnAuthenticated,
            emptyList()
        )

        // when
        viewModel.init()

        // then
        assertThat(loadState.lastValue()).isEqualTo(LoadState.LOADED)
        assertType<MyPageState>(myPageState.lastValue()) {
            assertThat(this.userStatus).isEqualTo(UserStatus.UnAuthenticated)
        }
    }

    @Test
    fun onRetryClick() {
        // given
        val loadState = viewModel.loadState.test()
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.onRetryClick()

        // then
        assertThat(loadState.values()).isEqualTo(listOf(LoadState.LOADING, LoadState.LOADED))
        assertType<MyPageState>(myPageState.lastValue()) {
            assertThat(this.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
            assertThat(this.repositoryList.size).isEqualTo(3)
        }
    }

    @Test
    fun onRefresh() {
        // given
        val isRefreshing = viewModel.isRefreshing.test()
        val myPageState = viewModel.myPageState.test()

        // when
        viewModel.onRefresh()

        // then
        assertThat(isRefreshing.values()).isEqualTo(listOf(true, false))
        assertType<MyPageState>(myPageState.lastValue()) {
            assertThat(this.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
            assertThat(this.repositoryList.size).isEqualTo(3)
        }
    }
}
