package com.example.policyagent.ui.fragments.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.responses.agentdashboard.AgentDashBoardResponse
import com.example.policyagent.data.responses.clienthome.ClientHomeResponse
import com.example.policyagent.databinding.FragmentClientHomeBinding
import com.example.policyagent.ui.activities.client.AddNewPolicyActivity
import com.example.policyagent.ui.activities.client.AddPolicyFormActivity
import com.example.policyagent.ui.activities.client.MyInsurancePortfolioActivity
import com.example.policyagent.ui.activities.client.PremiumCalendarActivity
import com.example.policyagent.ui.adapters.client.HomeTopBannerAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.listeners.ClientHomeListener
import com.example.policyagent.ui.viewmodels.client.ClientHomeViewModel
import com.example.policyagent.util.getGlideProgress
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ClientHomeFragment : BaseFragment(), KodeinAware, ClientHomeListener {
    override val kodein by kodein()
    private lateinit var viewModel: ClientHomeViewModel
    private val factory: MainViewModelFactory by instance()
    var binding: FragmentClientHomeBinding? = null
    private lateinit var homeTopBannerAdapter: HomeTopBannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_home, container, false)
        viewModel = ViewModelProvider(this, factory).get(ClientHomeViewModel::class.java)
        viewModel.listener = this
        viewModel.getClientDashboard(requireContext())
        homeTopBannerAdapter = HomeTopBannerAdapter(requireContext(),this)
        binding!!.topBannerSlider.scrollTimeInMillis = 2500
        binding!!.topBannerSlider.startAutoCycle()
        binding!!.topBannerSlider.setSliderAdapter(homeTopBannerAdapter)
        viewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding!!.tvUsername.text = "${user.firstname} ${user.lastname}"
            }
        })
        binding!!.llMyPortfolio.setOnClickListener {
            requireActivity().launchActivity<MyInsurancePortfolioActivity> {  }
        }

        binding!!.llPremiumCalendar.setOnClickListener {
            requireActivity().launchActivity<PremiumCalendarActivity> {  }
        }
        binding!!.llAddPolicy.setOnClickListener {
            requireActivity().launchActivity<AddPolicyFormActivity> {  }
        }
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClientHomeFragment().apply {
            }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: ClientHomeResponse) {
        hideProgress()
        if(data.status!!){
            homeTopBannerAdapter.updateList(data.data!!)
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

    override fun onLoadImage(image: String, imageview: ImageView) {
        Glide.with(this).load(image).placeholder(getGlideProgress(requireContext()))
            .error(R.drawable.ic_warning).into(imageview)
    }

}