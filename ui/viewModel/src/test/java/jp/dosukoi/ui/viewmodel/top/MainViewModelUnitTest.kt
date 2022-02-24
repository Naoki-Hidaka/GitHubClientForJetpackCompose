package jp.dosukoi.ui.viewmodel.top

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import jp.dosukoi.data.usecase.auth.GetAccessTokenUseCase
import jp.dosukoi.testing.common.test
import jp.dosukoi.testing.common.testRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelUnitTest {

    @get:Rule
    val rule = testRule()

    @RelaxedMockK
    private lateinit var getAccessTokenUseCase: GetAccessTokenUseCase

    @InjectMockKs
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { getAccessTokenUseCase.execute(any()) } returns Unit
    }

    @Test
    fun onGetCode_success() {
        // given
        val onEvent = viewModel.onEvent.test()

        // when
        viewModel.onGetCode("")

        // then
        assertThat(onEvent.lastValue()).isEqualTo(MainViewModel.Event.CompleteGetAccessToken)
    }

    @Test
    fun onGetCode_failure() {
        // given
        val onEvent = viewModel.onEvent.test()
        coEvery { getAccessTokenUseCase.execute(any()) } throws RuntimeException()

        // when
        viewModel.onGetCode("")

        // then
        assertThat(onEvent.lastValue()).isInstanceOf(MainViewModel.Event.FailedFetch::class.java)
    }
}
