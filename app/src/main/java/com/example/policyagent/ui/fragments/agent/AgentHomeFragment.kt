package com.example.policyagent.ui.fragments.agent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.FragmentAgentHomeBinding
import com.example.policyagent.databinding.FragmentClientHomeBinding
import com.example.policyagent.ui.activities.client.MyInsurancePortfolioActivity
import com.example.policyagent.ui.activities.client.PremiumCalendarActivity
import com.example.policyagent.ui.adapters.client.HomeTopBannerAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.viewmodels.agent.AgentHomeViewModel
import com.example.policyagent.ui.viewmodels.client.ClientHomeViewModel
import com.example.policyagent.util.launchActivity
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AgentHomeFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private lateinit var viewModel: AgentHomeViewModel
    private val factory: MainViewModelFactory by instance()
    var binding: FragmentAgentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_home, container, false)
        viewModel = ViewModelProvider(this, factory).get(AgentHomeViewModel::class.java)
        viewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding!!.tvUsername.text = "${user.firstname} ${user.lastname}"
            }
        })
        return binding!!.root
    }

}