package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityPolicyTypeBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.agent.PolicyTypeViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class PolicyTypeActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityPolicyTypeBinding? = null
    private var viewModel: PolicyTypeViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy_type)
        viewModel = ViewModelProvider(this, factory)[PolicyTypeViewModel::class.java]

        binding!!.appBar.tvTitle.text = resources.getString(R.string.policies)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        binding!!.llLifeInsurance.setOnClickListener {
            launchActivity<LifeInsuranceListActivity> {
            }
        }
        binding!!.llHealthInsurance.setOnClickListener {
            launchActivity<HealthInsuranceListActivity> {
            }
        }
        binding!!.llCarInsurance.setOnClickListener {
            launchActivity<CarInsuranceListActivity> {
            }
        }
        binding!!.llWcInsurance.setOnClickListener {
            launchActivity<WcInsuranceListActivity> {
            }
        }
        binding!!.llFireInsurance.setOnClickListener {
            launchActivity<FireInsuranceListActivity> {
            }
        }
    }
}