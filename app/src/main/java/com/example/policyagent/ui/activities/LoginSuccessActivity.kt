package com.example.policyagent.ui.activities

import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.memberlist.MemberListResponse
import com.example.policyagent.databinding.ActivityLoginSuccessBinding
import com.example.policyagent.ui.activities.agent.AgentDashboardActivity
import com.example.policyagent.ui.activities.client.ClientDashboardActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.LoginSuccessListener
import com.example.policyagent.ui.viewmodels.LoginSuccessViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginSuccessActivity : BaseActivity(), KodeinAware, LoginSuccessListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityLoginSuccessBinding? = null
    private var viewModel: LoginSuccessViewModel? = null
    var userType: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_success)
        viewModel = ViewModelProvider(this, factory)[LoginSuccessViewModel::class.java]
        viewModel!!.listener = this
        if (intent.hasExtra(AppConstants.USER_TYPE)) {
            userType = intent.getStringExtra(AppConstants.USER_TYPE)
        }
        /*Handler().postDelayed({
            finish()
            if(userType == "AGENT"){
                launchActivity<PolicyTypeActivity> {}
            } else {
                launchActivity<ClientDashboardActivity> {}
            }
        },1000)*/
        if (userType == "AGENT") {
            viewModel!!.getClients(this)
        } else {
            viewModel!!.getMembers(this)
        }
    }

    override fun onStarted() {

    }

    override fun onSuccessClient(client: ClientListResponse) {
        val gson = Gson()
        val json = gson.toJson(client)
        viewModel!!.getPreference().setStringValue(AppConstants.CLIENTS, json)
        AppConstants.clients = client.data!!
        viewModel!!.getCompanies(this)
    }

    override fun onSuccessMember(member: MemberListResponse) {
        val gson = Gson()
        val json = gson.toJson(member)
        viewModel!!.getPreference().setStringValue(AppConstants.MEMBERS, json)
        AppConstants.members = member.data!!
            finish()
            launchActivity<ClientDashboardActivity> {}
    }

    override fun onSuccessCompany(company: CompanyListResponse) {
        val gson = Gson()
        val json = gson.toJson(company)
        viewModel!!.getPreference().setStringValue(AppConstants.COMPANIES, json)
        AppConstants.companies = company.data!!
        finish()
        if (userType == "AGENT") {
            launchActivity<AgentDashboardActivity> {}
        } else {
            launchActivity<ClientDashboardActivity> {}
        }
    }

    override fun onFailure(message: String) {

    }

    override fun onError(errors: HashMap<String, Any>) {

    }
}