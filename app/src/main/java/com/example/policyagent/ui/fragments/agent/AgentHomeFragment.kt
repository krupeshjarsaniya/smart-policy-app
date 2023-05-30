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
import com.example.policyagent.data.responses.agentdashboard.AgentDashBoardResponse
import com.example.policyagent.databinding.FragmentAgentHomeBinding
import com.example.policyagent.ui.activities.agent.*
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.listeners.AgentDashBoardListener
import com.example.policyagent.ui.viewmodels.agent.AgentHomeViewModel
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AgentHomeFragment : BaseFragment(), KodeinAware, AgentDashBoardListener {
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
        viewModel = ViewModelProvider(this, factory)[AgentHomeViewModel::class.java]
        viewModel.listener = this
        viewModel.getAgentDashboard(requireContext())
        viewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding!!.tvUsername.text = "${user.firstname} ${user.lastname}"
            }
        })
        binding!!.llTotalClients.setOnClickListener {
            requireActivity().launchActivity<ClientListActivity> {
            }
        }

        binding!!.llExpiredPolicy.setOnClickListener {
            requireActivity().launchActivity<ExpiredActivity> {
            }
        }

        binding!!.llUpcomingExpirePolicy.setOnClickListener {
            requireActivity().launchActivity<UpcomingExpiredActivity> {
            }
        }

        binding!!.llPaymentDuePolicy.setOnClickListener {
            requireActivity().launchActivity<PaymentDuePolicyActivity> {
            }
        }

        binding!!.llUpcomingPaymentPolicy.setOnClickListener {
            requireActivity().launchActivity<UpcomingPaymentPolicyActivity> {
            }
        }
        return binding!!.root
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: AgentDashBoardResponse) {
        hideProgress()
        if(data.status!!){
            binding!!.tvTotalClients.text = data.data!!.total_client
            binding!!.tvTotalInsurance.text = data.data.total_insurance
            binding!!.tvTotalCommission.text = "₹ "+data.data.total_commision
            binding!!.tvDuePayment.text = "₹ "+data.data.due_payment

            binding!!.tvExpiredPolicy.text = data.data.total_expired_policy
            binding!!.tvUpcomingExpirePolicy.text = data.data.total_upcoming_expire_policy
            binding!!.tvPaymentDuePolicy.text = data.data.total_payment_due_policy
            binding!!.tvUpcomingPaymentPolicy.text = data.data.total_upcoming_payment_policy
        }
    }

    override fun onFailure(message: String) {
        hideProgress()
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
    }

    override fun onLogout(message: String) {
        
    }

}