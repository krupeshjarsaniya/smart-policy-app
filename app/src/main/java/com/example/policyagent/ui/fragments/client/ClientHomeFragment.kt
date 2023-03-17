package com.example.policyagent.ui.fragments.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.FragmentClientHomeBinding
import com.example.policyagent.ui.activities.client.MyInsurancePortfolioActivity
import com.example.policyagent.ui.activities.client.PremiumCalendarActivity
import com.example.policyagent.ui.adapters.client.HomeTopBannerAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.viewmodels.client.ClientHomeViewModel
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ClientHomeFragment : BaseFragment(), KodeinAware {
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
        homeTopBannerAdapter = HomeTopBannerAdapter(requireContext())
        binding!!.topBannerSlider.scrollTimeInMillis = 2500
        binding!!.topBannerSlider.startAutoCycle()
        binding!!.topBannerSlider.setSliderAdapter(homeTopBannerAdapter)

        binding!!.llMyPortfolio.setOnClickListener {
            requireActivity().launchActivity<MyInsurancePortfolioActivity> {  }
        }

        binding!!.llPremiumCalendar.setOnClickListener {
            requireActivity().launchActivity<PremiumCalendarActivity> {  }
        }
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClientHomeFragment().apply {
            }
    }
}