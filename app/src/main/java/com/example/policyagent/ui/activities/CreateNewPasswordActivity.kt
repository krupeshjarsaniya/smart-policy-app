package com.example.policyagent.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.ActivityCreateNewPasswordBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.CreateNewPasswordListener
import com.example.policyagent.ui.viewmodels.CreateNewPasswordViewModel
import com.example.policyagent.util.AppConstants
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class CreateNewPasswordActivity : BaseActivity(), KodeinAware, CreateNewPasswordListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityCreateNewPasswordBinding? = null
    private var viewModel: CreateNewPasswordViewModel? = null
    var mobile: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_password)
        viewModel = ViewModelProvider(this, factory)[CreateNewPasswordViewModel::class.java]
        viewModel!!.listener = this

        binding!!.ivBack.setOnClickListener {
            finish()
        }

        if(intent.hasExtra(AppConstants.MOBILE)){
            mobile = intent.getStringExtra(AppConstants.MOBILE)
        }

        binding!!.btnContinue.setOnClickListener {
            var password = binding!!.etPassword.editableText.toString()
            var cPassword = binding!!.etConfirmPassword.editableText.toString()
            if (password.length >= 6){
                    if (password == cPassword){
                        viewModel!!.onCreateNewPassword(mobile!!,password,this)
                    }else{
                        onFailure(getString(R.string.password_does_not_match))
                    }
            }else{
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
        Log.e("onerror","error")
        hideProgress()
    }
}