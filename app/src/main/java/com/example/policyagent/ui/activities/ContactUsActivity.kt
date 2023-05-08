package com.example.policyagent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.contactus.ContactUsResponse
import com.example.policyagent.databinding.ActivityClientEditProfileBinding
import com.example.policyagent.databinding.ActivityContactUsBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ChangePasswordListener
import com.example.policyagent.ui.listeners.ContactUsListener
import com.example.policyagent.ui.viewmodels.ContactUsViewModel
import com.example.policyagent.ui.viewmodels.client.ClientEditProfileViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchLoginActivity
import com.example.policyagent.util.openDieler
import com.example.policyagent.util.openMail
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ContactUsActivity : BaseActivity(), KodeinAware, ContactUsListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityContactUsBinding? = null
    private var viewModel: ContactUsViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        viewModel = ViewModelProvider(this, factory)[ContactUsViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getContactUs(this)

        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.tvTitle.text = resources.getString(R.string.contact_us)

        binding!!.llMobile.setOnClickListener {
            openDieler(binding!!.tvMobile.text.toString(),this)
        }
        binding!!.llMail.setOnClickListener {
            openMail(binding!!.tvEmail.text.toString(),this)
        }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: ContactUsResponse) {
        hideProgress()
        binding!!.tvMobile.text = data.data!!.mobile!!
        binding!!.tvEmail.text = data.data.email!!
        binding!!.tvAddress.text = data.data.address
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(errors: HashMap<String, Any>) {
        
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
        if(message.contains("Unauthenticated")){
            launchLoginActivity<LoginActivity> {  }
        }
    }
}