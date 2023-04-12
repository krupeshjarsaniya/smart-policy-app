package com.example.policyagent.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.database.entities.User
import com.example.policyagent.databinding.ActivityLoginBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.LoginListener
import com.example.policyagent.ui.viewmodels.LoginViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.isValidPassword
import com.example.policyagent.util.isValidPhone
import com.example.policyagent.util.launchActivity
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity(), KodeinAware, LoginListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityLoginBinding? = null
    private var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        viewModel!!.authListener = this

        binding!!.tvForgot.setOnClickListener {
            launchActivity<ForgotPasswordActivity> { }
        }
        binding!!.btnSignIn.setOnClickListener {
            var mobile = binding!!.etMobile.editableText.toString()
            var password = binding!!.etPassword.editableText.toString()
            if (mobile.isEmpty()) {
                binding!!.etMobile.requestFocus()
                onFailure(getString(R.string.please_enter_valid_id))
            } else if (!isValidPassword(password)) {
                binding!!.etPassword.requestFocus()
                onFailure(getString(R.string.please_enter_valid_password))
            } else {
                viewModel!!.onLogin(mobile, password, this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(user: User) {
        hideProgress()
        finish()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,true)
        launchActivity<LoginSuccessActivity> {
            this.putExtra(AppConstants.USER_TYPE, user.user_type)
        }
    }

    override fun onFailure(message: String) {
        Log.e("fail", "failure")
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
        if (errors.containsKey("password")) {
        }
    }
}