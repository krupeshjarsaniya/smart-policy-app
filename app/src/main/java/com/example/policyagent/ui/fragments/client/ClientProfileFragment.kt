package com.example.policyagent.ui.fragments.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.FragmentClientProfileBinding
import com.example.policyagent.ui.activities.ChangePasswordActivity
import com.example.policyagent.ui.activities.ContactUsActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.activities.agent.AgentEditProfileActivity
import com.example.policyagent.ui.activities.client.ClientEditProfileActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.listeners.ClientProfileListener
import com.example.policyagent.ui.viewmodels.client.ClientProfileViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.hide
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ClientProfileFragment : BaseFragment(), KodeinAware, ClientProfileListener {
    override val kodein by kodein()
    private lateinit var viewModel: ClientProfileViewModel
    private val factory: MainViewModelFactory by instance()
    var binding: FragmentClientProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_profile, container, false)
        viewModel = ViewModelProvider(this, factory).get(ClientProfileViewModel::class.java)
        viewModel.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.profile)
        binding!!.appBar.ivBack.hide()
        viewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                binding!!.tvUsername.text = "${user.firstname} ${user.lastname}"
                binding!!.itvEmail.text = "${user.email}"
            }
        })
        var userType = viewModel.getPreference().getStringValue(AppConstants.USER_TYPE)
        if(userType == AppConstants.AGENT){
            binding!!.llEditProfile.hide()
        }
        binding!!.llChangePassword.setOnClickListener {
            requireActivity().launchActivity<ChangePasswordActivity> {
            }
        }
        binding!!.llEditProfile.setOnClickListener {
            var userType = viewModel.getPreference().getStringValue(AppConstants.USER_TYPE)
            if(userType == AppConstants.AGENT) {
                requireActivity().launchActivity<AgentEditProfileActivity> {
                }
            } else{
                requireActivity().launchActivity<ClientEditProfileActivity> {
                }
            }
        }
        binding!!.llContactUs.setOnClickListener {
            requireActivity().launchActivity<ContactUsActivity> {
            }
        }
        binding!!.llTermsCondition.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://technocometsolutions.in/developers/smart-policy/termandcondition"))
            startActivity(browserIntent)
        }
        binding!!.btnLogout.setOnClickListener {
            viewModel.onLogout(requireContext())
        }
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClientProfileFragment().apply {
            }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: CommonResponse) {
        hideProgress()
        showToastMessage(response.message!!)
        viewModel.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
        requireActivity().launchActivity<LoginActivity> {
            requireActivity().finish()
        }
    }

    override fun onFailure(message: String) {
        showToastMessage(message)
    }
}