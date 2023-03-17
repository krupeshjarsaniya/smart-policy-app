package com.example.policyagent.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivitySplashBinding
import com.example.policyagent.ui.activities.agent.PolicyTypeActivity
import com.example.policyagent.ui.activities.client.ClientDashboardActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.SplashViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivitySplashBinding? = null
    private var viewModel: SplashViewModel? = null
    var isLoggedIn: Boolean? = false
    var userType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]
        isLoggedIn = viewModel!!.getPreference().getBooleanValue(AppConstants.IS_REMEMBER)
        Handler().postDelayed({
            finish()
            if(isLoggedIn!!){
                userType = viewModel!!.getPreference().getStringValue(AppConstants.USER_TYPE)
                if(userType == AppConstants.AGENT){
                    launchActivity<PolicyTypeActivity> {}
                } else{
                    launchActivity<ClientDashboardActivity> {}
                }
            } else {
                launchActivity<LoginActivity> {}
            }
        },1000)

    }
}