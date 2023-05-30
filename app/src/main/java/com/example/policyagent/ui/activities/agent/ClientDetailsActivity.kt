package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.databinding.ActivityClientDetailsBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.DocumentAdapter
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.util.*

class ClientDetailsActivity : BaseActivity(), LoadDocumentListener {
    private var binding: ActivityClientDetailsBinding? = null
    var clientDetails: ClientData? = ClientData()
    var clientDocumentAdapter: DocumentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_details)
        if(intent.hasExtra(AppConstants.CLIENT)){
            clientDetails = intent.getSerializableExtra(AppConstants.CLIENT) as ClientData
        }
        binding!!.appBar.tvTitle.text = clientDetails!!.firstname
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        if (clientDetails!!.firstname!!.isEmpty()) {
            binding!!.tvName.text = "-"
        } else {
            binding!!.tvName.text = clientDetails!!.firstname
        }
        if (clientDetails!!.email!!.isEmpty()) {
            binding!!.tvEmail.text = "-"
        } else {
            binding!!.tvEmail.text = clientDetails!!.email
        }
        if (clientDetails!!.mobile!!.isEmpty()) {
            binding!!.tvMobile.text = "-"
        } else {
            binding!!.tvMobile.text = clientDetails!!.mobile
        }
        if (clientDetails!!.birthdate!!.isEmpty()) {
            binding!!.tvBirthDate.text = "-"
        } else {
            binding!!.tvBirthDate.text = clientDetails!!.birthdate
        }
        if (clientDetails!!.marital_status!!.isEmpty()) {
            binding!!.tvMaritalStatus.text = "-"
        } else {
            binding!!.tvMaritalStatus.text = clientDetails!!.marital_status
        }
        if (clientDetails!!.gender!!.isEmpty()) {
            binding!!.tvGender.text = "-"
        } else {
            binding!!.tvGender.text = clientDetails!!.gender
        }
        if (clientDetails!!.age!!.isEmpty()) {
            binding!!.tvAge.text = "-"
        } else {
            binding!!.tvAge.text = clientDetails!!.age
        }
        if (clientDetails!!.status!!.isEmpty()) {
            binding!!.tvStatus.text = "-"
        } else {
            binding!!.tvStatus.text = clientDetails!!.status
        }
        if (clientDetails!!.height!!.isEmpty()) {
            binding!!.tvHeight.text = "-"
        } else {
            binding!!.tvHeight.text = clientDetails!!.height
        }
        if (clientDetails!!.weight!!.isEmpty()) {
            binding!!.tvWeight.text = "-"
        } else {
            binding!!.tvWeight.text = clientDetails!!.weight
        }
        if (clientDetails!!.city!!.isEmpty()) {
            binding!!.tvCity.text = "-"
        } else {
            binding!!.tvCity.text = clientDetails!!.city
        }
        if (clientDetails!!.state!!.isEmpty()) {
            binding!!.tvState.text = "-"
        } else {
            binding!!.tvState.text = clientDetails!!.state
        }
        if (clientDetails!!.address!!.isEmpty()) {
            binding!!.tvAddress.text = "-"
        } else {
            binding!!.tvAddress.text = clientDetails!!.address
        }
        if (clientDetails!!.pan_number!!.isEmpty()) {
            binding!!.tvPanNumber.text = "-"
        } else {
            binding!!.tvPanNumber.text = clientDetails!!.pan_number
        }
        if (clientDetails!!.gst!!.isEmpty()) {
            binding!!.tvGst.text = "-"
        } else {
            binding!!.tvGst.text = clientDetails!!.gst
        }
        if (clientDetails!!.password!!.isEmpty()) {
            binding!!.tvPassword.text = "-"
        } else {
            binding!!.tvPassword.text = clientDetails!!.password
        }

        clientDocumentAdapter = DocumentAdapter(this, this)
        binding!!.rvClientDocument.adapter = clientDocumentAdapter
        clientDocumentAdapter!!.updateList(clientDetails!!.client_Documents!!)

        if (clientDetails!!.client_Documents!!.isEmpty()) {
            binding!!.llClientDocument.hide()
        }
    }

    override fun onLoadImage(image: String, imageview: ImageView) {
        Glide.with(this).load(image).placeholder(getGlideProgress(this))
            .error(R.drawable.ic_warning).into(imageview)
    }

    override fun onLoadPdf(url: String) {
        loadPdf(this, url)
    }
    override fun onDownload(url: String) {
        downloadFile(this,url,this)
    }
}