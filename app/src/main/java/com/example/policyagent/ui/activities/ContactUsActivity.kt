package com.example.policyagent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityClientEditProfileBinding
import com.example.policyagent.databinding.ActivityContactUsBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ChangePasswordListener
import com.example.policyagent.ui.viewmodels.ContactUsViewModel
import com.example.policyagent.ui.viewmodels.client.ClientEditProfileViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ContactUsActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityContactUsBinding? = null
    private var viewModel: ContactUsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        viewModel = ViewModelProvider(this, factory)[ContactUsViewModel::class.java]

        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.tvTitle.text = resources.getString(R.string.contact_us)
    }
}