package jp.dosukoi.ui.view.myPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoi.ui.view.R
import jp.dosukoi.ui.view.databinding.ActivityAuthBinding
import jp.dosukoi.ui.viewmodel.myPage.AuthViewModel

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, AuthActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityAuthBinding>(this, R.layout.activity_auth).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        viewModel.onEvent.observe(this, ::handleEvent)
    }

    private fun handleEvent(event: AuthViewModel.Event) {
        when (event) {
            AuthViewModel.Event.ClickedNavigation -> finish()
        }
    }
}
