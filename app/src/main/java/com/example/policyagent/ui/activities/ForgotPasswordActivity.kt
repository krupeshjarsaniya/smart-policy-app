package com.example.policyagent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.ActivityForgotPasswordBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ForgotPasswordListener
import com.example.policyagent.ui.viewmodels.ForgotPasswordViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.isValidPassword
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ForgotPasswordActivity : BaseActivity(), KodeinAware, ForgotPasswordListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityForgotPasswordBinding? = null
    private var viewModel: ForgotPasswordViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]
        viewModel!!.listener = this
        binding!!.ivBack.setOnClickListener {
            finish()
        }

        binding!!.btnContinue.setOnClickListener {
            var mobile = binding!!.etMobile.editableText.toString()
            if (mobile.isEmpty()){
                binding!!.etMobile.requestFocus()
                onFailure(getString(R.string.please_enter_valid_id))
            } else{
                viewModel!!.onForgotPassword(mobile,this)
            }
            /*launchActivity<ConfirmOtpActivity> {
                finish()
            }*/
        }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: CommonResponse) {
        hideProgress()
        showToastMessage(response.message!!)
        launchActivity<ConfirmOtpActivity> {
            this.putExtra(AppConstants.MOBILE,binding!!.etMobile.editableText.toString())
            finish()
        }
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(error: HashMap<String,Any>) {
        hideProgress()
    }
}