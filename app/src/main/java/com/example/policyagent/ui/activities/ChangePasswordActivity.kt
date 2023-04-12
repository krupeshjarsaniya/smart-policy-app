package com.example.policyagent.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.ActivityChangePasswordBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ChangePasswordListener
import com.example.policyagent.ui.viewmodels.ChangePasswordViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchLoginActivity
import com.google.gson.Gson
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ChangePasswordActivity : BaseActivity(), KodeinAware, ChangePasswordListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityChangePasswordBinding? = null
    private var viewModel: ChangePasswordViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        viewModel = ViewModelProvider(this, factory)[ChangePasswordViewModel::class.java]
        viewModel!!.listener = this
        binding!!.ivBack.setOnClickListener {
            finish()
        }

        binding!!.btnChange.setOnClickListener {
            var currentPassword = binding!!.etCurrentPassword.editableText.toString()
            var password = binding!!.etNewPassword.editableText.toString()
            var cPassword = binding!!.etConfirmPassword.editableText.toString()
            if (password.length >= 6 && currentPassword.length >= 6) {
                if (password == cPassword) {
                    viewModel!!.onChangePassword(currentPassword, password, this)
                } else {
                    onFailure(getString(R.string.password_does_not_match))
                }
            } else {
                onFailure(getString(R.string.please_enter_atleast_six_character))
            }
        }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: CommonResponse) {
        hideProgress()
        showToastMessage(response.message!!)
        finish()
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(error: HashMap<String,Any>) {
        hideProgress()
        if (error.containsKey("current_password")) {
            //binding!!.etCurrentPassword.error = error["current_password"].toString()
            Log.d("current_password",error["current_password"].toString())
            //binding!!.tiCurrentPassword.setErrorEnabled(true)
            //binding!!.tiCurrentPassword.setError(error["current_password"].toString())
            //binding!!.etCurrentPassword.setBackground(resources.getDrawable(R.drawable.bg_edit_text_error))
        } else if (error.containsKey("password")) {
//            binding!!.etNewPassword.error = error["password"].toString()
//            binding!!.etNewPassword.setBackground(resources.getDrawable(R.drawable.bg_edit_text_error))
        } else {
            showToastMessage(resources.getString(R.string.invalid_data))
        }
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
        if(message.contains("Unauthenticated")){
            launchLoginActivity<LoginActivity> {  }
        }
    }
}