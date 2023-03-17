package com.example.policyagent.ui.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.ActivityConfirmOtpBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.VerifyOtpListener
import com.example.policyagent.ui.viewmodels.ConfirmOtpViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ConfirmOtpActivity : BaseActivity(), KodeinAware, VerifyOtpListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityConfirmOtpBinding? = null
    private var viewModel: ConfirmOtpViewModel? = null
    private var countDownTimer: CountDownTimer? = null
    var mobile: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_otp)
        viewModel = ViewModelProvider(this, factory)[ConfirmOtpViewModel::class.java]
        viewModel!!.listener = this
        if(intent.hasExtra(AppConstants.MOBILE)){
            mobile = intent.getStringExtra(AppConstants.MOBILE)
        }
            binding!!.tvMobile.text = mobile
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        initTimer()
        startTimer()

        binding!!.btnContinue.setOnClickListener {
            val stredtOTP = binding!!.otpView.text.toString().trim()
            if (stredtOTP.length == 4) {
                    viewModel!!.onVerifyOTP(stredtOTP, this)
            } else {
                onFailure(getString(R.string.please_enter_valid_otp))
            }

        }
        binding!!.tvResend.setOnClickListener {
            viewModel!!.onForgotPassword(mobile!!,this)
        }
    }

    private fun initTimer() {
        countDownTimer = object : CountDownTimer(AppConstants.TIMER_TIME, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60

                val elapsedSeconds = diff / secondsInMilli
                var secondValue = elapsedSeconds.toString()
                if (secondValue.toString().length == 1) {
                    secondValue = "0"+secondValue
                }
                binding!!.tvResendTimer.text = "${secondValue}"
            }

            override fun onFinish() {
                binding!!.llCounter.visibility = View.GONE
                binding!!.tvResend.visibility = View.VISIBLE
            }
        }
    }


    fun startTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.start()
        }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: CommonResponse,type: String) {
        hideProgress()
        showToastMessage(response.message!!)
        if(type == AppConstants.RESEND){
            binding!!.tvResend.visibility = View.GONE
            binding!!.llCounter.visibility = View.VISIBLE
            initTimer()
            startTimer()
        } else{
            launchActivity<CreateNewPasswordActivity> {
                this.putExtra(AppConstants.MOBILE,mobile)
                finish()
            }
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