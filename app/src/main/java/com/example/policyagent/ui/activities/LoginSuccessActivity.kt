package com.example.policyagent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.policyagent.R
import com.example.policyagent.ui.activities.agent.PolicyTypeActivity
import com.example.policyagent.ui.activities.client.ClientDashboardActivity
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity

class LoginSuccessActivity : AppCompatActivity() {
    var userType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)
        if(intent.hasExtra(AppConstants.USER_TYPE)){
            userType = intent.getStringExtra(AppConstants.USER_TYPE)
        }
        Handler().postDelayed({
            finish()
            if(userType == "AGENT"){
                launchActivity<PolicyTypeActivity> {}
            } else {
                launchActivity<ClientDashboardActivity> {}
            }
        },1000)

    }
}