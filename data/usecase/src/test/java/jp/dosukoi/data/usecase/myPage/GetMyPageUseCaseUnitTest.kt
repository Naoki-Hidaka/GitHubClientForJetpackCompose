package jp.dosukoi.data.usecase.myPage

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.repository.myPage.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import jp.dosukoi.testing.common.assertType
import jp.dosukoi.testing.common.testRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMyPageUseCaseUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    @RelaxedMockK
    private lateinit var reposRepository: ReposRepository

    @InjectMockKs
    private lateinit var getMyPageUseCase: GetMyPageUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun execute_authenticated() {
        // given
        coEvery { userRepository.getUser() } returns UserStatus.Authenticated(mockk())
        coEvery { reposRepository.getRepositoryList() } returns listOf(mockk(), mockk())

        runBlocking {
            // when
            val result = getMyPageUseCase.execute()

            // then
            assertType<MyPageState>(result) {
                assertThat(this.userStatus).isInstanceOf(UserStatus.Authenticated::class.java)
                assertThat(this.repositoryList.size).isEqualTo(2)
            }
        }
    }

    @Test
    fun execute_un_authenticated() {
        // given
        coEvery { userRepository.getUser() } returns UserStatus.UnAuthenticated
        coEvery { reposRepository.getRepositoryList() } returns listOf(mockk(), mockk())

        runBlocking {
            // when
            val result = getMyPageUseCase.execute()

            // then
            assertType<MyPageState>(result) {
                assertThat(this.userStatus).isEqualTo(UserStatus.UnAuthenticated)
                assertThat(this.repositoryList.size).isEqualTo(2)
            }
        }
    }
}
