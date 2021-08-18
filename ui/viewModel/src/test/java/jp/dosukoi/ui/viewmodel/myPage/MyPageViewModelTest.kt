package jp.dosukoi.ui.viewmodel.myPage

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.usecase.myPage.GetMyPageUseCase
import jp.dosukoi.testing.common.test
import jp.dosukoi.testing.common.testRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyPageViewModelTest {
    @get:Rule
    val rule = jp.dosukoi.testing.common.testRule()

    @RelaxedMockK
    private lateinit var getMyPageUseCase: GetMyPageUseCase

    @RelaxedMockK
    private lateinit var myPageListener: MyPageListener

    @InjectMockKs
    private lateinit var viewModel: MyPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun refresh_success() {
        // given
        val loadState = viewModel.loadState.test()
//        coEvery { userRepository.getUser() } returns MOCK_USER
//        coEvery { reposRepository.getRepositoryList() } returns MOCK_REPO_LIST

        // when
        viewModel.refresh()

        // then
//        assertType<LoadState.Loaded<UserStatus.Authenticated>>(loadState.lastValue()) {
//            assertType<UserStatus.Authenticated>(this.value) {
//                assertThat(this.item.user).isEqualTo(MOCK_USER)
//                assertThat(this.item.repositoryList).isEqualTo(MOCK_REPO_LIST)
//            }
//        }
    }

    @Test
    fun refresh_failure_unauthorized() {
        // given
        val loadState = viewModel.loadState.test()
        // coEvery { userRepository.getUser() } throws UnAuthorizeException("")

        // when
        viewModel.refresh()

        // then
//        assertType<LoadState.Loaded<UserStatus.UnAuthenticated>>(loadState.lastValue()) {
//            assertThat(this.value).isInstanceOf(UserStatus.UnAuthenticated::class.java)
//        }
    }

    @Test
    fun refresh_failure() {
        // given
        val loadState = viewModel.loadState.test()
//        coEvery { userRepository.getUser() } throws RuntimeException()

        // when
        viewModel.refresh()

        // then
//        assertThat(loadState.lastValue()).isInstanceOf(LoadState.Error::class.java)
    }

    @Test
    fun onRefresh() {
        // given
        val isRefreshing = viewModel.isRefreshing.test()
//        coEvery { userRepository.getUser() } returns MOCK_USER
//        coEvery { reposRepository.getRepositoryList() } returns MOCK_REPO_LIST

        // when
        viewModel.onRefresh()

        // then
        assertThat(isRefreshing.values()).isEqualTo(listOf(false, true, false))
    }

    companion object {
        private val MOCK_USER = User(
            "mock_user",
            0,
            "https://example.com",
            "https://example.com",
            "https://example.com",
            "Hoge Company",
            "example@exapmle.com",
            null
        )

        private val MOCK_REPO_LIST = listOf(
            Repository(
                0, "hoge", null, "https://example.com"
            ),
            Repository(
                1, "foo", null, "https://example.com"
            )
        )
    }
}
