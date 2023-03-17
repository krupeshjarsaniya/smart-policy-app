package com.example.policyagent.ui.activities.client

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityClientEditProfileBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.client.ClientEditProfileViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class ClientEditProfileActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityClientEditProfileBinding? = null
    private var viewModel: ClientEditProfileViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_edit_profile)
        viewModel = ViewModelProvider(this, factory)[ClientEditProfileViewModel::class.java]

        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.etUsername.onFocusChangeListener = this
        binding!!.etEmail.onFocusChangeListener = this
        binding!!.etMobile.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(hasFocus) {
            when (v) {
                binding!!.etUsername -> {
                    binding!!.ivUsename.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etEmail -> {
                    binding!!.ivEmail.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etMobile -> {
                    binding!!.ivMobile.setColorFilter(resources.getColor(R.color.primary_color))
                }
            }
        } else{
            when (v) {
                binding!!.etUsername -> {
                    binding!!.ivUsename.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etEmail -> {
                    binding!!.ivEmail.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etMobile -> {
                    binding!!.ivMobile.setColorFilter(resources.getColor(R.color.grey))
                }
            }
        }
    }
}