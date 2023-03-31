package com.example.policyagent.ui.fragments.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.FragmentClientDocumentBinding
import com.example.policyagent.databinding.FragmentClientHomeBinding
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.viewmodels.client.ClientDocumentViewModel
import com.example.policyagent.ui.viewmodels.client.ClientHomeViewModel
import com.example.policyagent.util.hide
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ClientDocumentFragment : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private lateinit var viewModel: ClientDocumentViewModel
    private val factory: MainViewModelFactory by instance()
    var binding: FragmentClientDocumentBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_document, container, false)
        viewModel = ViewModelProvider(this, factory).get(ClientDocumentViewModel::class.java)
        binding!!.appBar.tvTitle.text = resources.getString(R.string.policies)
        binding!!.appBar.ivBack.hide()
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClientDocumentFragment().apply {

            }
    }
}