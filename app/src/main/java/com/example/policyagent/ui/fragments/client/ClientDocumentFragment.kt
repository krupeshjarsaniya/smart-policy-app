package com.example.policyagent.ui.fragments.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientdocumentlist.ClientDocumentListData
import com.example.policyagent.data.responses.clientdocumentlist.ClientDocumentListResponse
import com.example.policyagent.databinding.FragmentClientDocumentBinding
import com.example.policyagent.ui.adapters.client.ClientDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.fragments.BaseFragment
import com.example.policyagent.ui.listeners.ClientDocumentListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.client.ClientDocumentViewModel
import com.example.policyagent.util.downloadFile
import com.example.policyagent.util.getGlideProgress
import com.example.policyagent.util.hide
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ClientDocumentFragment : BaseFragment(), KodeinAware, ClientDocumentListener, LoadDocumentListener {
    override val kodein by kodein()

    private lateinit var viewModel: ClientDocumentViewModel
    private val factory: MainViewModelFactory by instance()
    var binding: FragmentClientDocumentBinding? = null
    var clientDocumentAdapter: ClientDocumentAdapter? = null
    var documentList: ArrayList<ClientDocumentListData?>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_document, container, false)
        clientDocumentAdapter = ClientDocumentAdapter(requireContext(),this,this)
        binding!!.rvDocuments.adapter = clientDocumentAdapter
        viewModel = ViewModelProvider(this, factory)[ClientDocumentViewModel::class.java]
        viewModel.listener = this
        viewModel.getClientDocuments(requireContext())
        binding!!.appBar.tvTitle.text = resources.getString(R.string.documents)
        binding!!.appBar.ivBack.hide()
        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ClientDocumentFragment().apply {
            }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: ClientDocumentListResponse) {
        hideProgress()
        documentList!!.clear()
        documentList!!.addAll(data.data!!)
        clientDocumentAdapter!!.updateList(documentList!!)
    }

    override fun onDownload(url: String, position: Int) {
        downloadFile(requireContext(),url, requireActivity())
    }

    override fun onSuccessDelete(data: CommonResponse, position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        documentList!!.removeAt(position)
        clientDocumentAdapter!!.updateList(documentList!!)
    }

    override fun onFailure(message: String) {
        hideProgress()
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
    }

    override fun onLogout(message: String) {
        hideProgress()
    }

    override fun onLoadImage(image: String, imageview: ImageView) {
        Glide.with(this).load(image).placeholder(getGlideProgress(requireContext()))
            .error(R.drawable.ic_warning).into(imageview)
    }

    override fun onLoadPdf(url: String) {
    }

    override fun onDownload(url: String) {
        downloadFile(requireContext(),url,requireActivity())
    }
}