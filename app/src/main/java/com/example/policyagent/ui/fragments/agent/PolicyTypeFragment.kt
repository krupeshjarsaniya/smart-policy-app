package com.example.policyagent.ui.fragments.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.FragmentPolicyTypeBinding
import com.example.policyagent.ui.activities.agent.*
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.viewmodels.agent.PolicyTypeViewModel
import com.example.policyagent.util.hide
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PolicyTypeFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: FragmentPolicyTypeBinding? = null
    private var viewModel: PolicyTypeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_policy_type, container, false)
        viewModel = ViewModelProvider(this, factory)[PolicyTypeViewModel::class.java]

        binding!!.appBar.tvTitle.text = resources.getString(R.string.policies)
        binding!!.appBar.ivBack.hide()
        /*binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }*/

        binding!!.llLifeInsurance.setOnClickListener {
            requireActivity().launchActivity<LifeInsuranceListActivity> {
            }
        }
        binding!!.llHealthInsurance.setOnClickListener {
            requireActivity().launchActivity<HealthInsuranceListActivity> {
            }
        }
        binding!!.llCarInsurance.setOnClickListener {
            requireActivity().launchActivity<CarInsuranceListActivity> {
            }
        }
        binding!!.llWcInsurance.setOnClickListener {
            requireActivity().launchActivity<WcInsuranceListActivity> {
            }
        }
        binding!!.llFireInsurance.setOnClickListener {
            requireActivity().launchActivity<FireInsuranceListActivity> {
            }
        }

        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PolicyTypeFragment().apply {
            }
    }
}