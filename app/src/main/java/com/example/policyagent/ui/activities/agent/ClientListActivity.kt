package com.example.policyagent.ui.activities.agent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.databinding.ActivityClientListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.ClientListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ClientListListener
import com.example.policyagent.ui.viewmodels.agent.ClientListViewModel
import com.example.policyagent.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.net.URLEncoder


class ClientListActivity : BaseActivity(), KodeinAware, ClientListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityClientListBinding? = null
    private var viewModel: ClientListViewModel? = null
    var policyAdapter: ClientListAdapter? = null
    var policyList: ArrayList<ClientData?>? = ArrayList()
    var page = 1
    var hasMore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_list)
        viewModel = ViewModelProvider(this, factory)[ClientListViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.client)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.btnAddPolicy.show()
        binding!!.appBar.btnAddPolicy.setOnClickListener {
            launchActivity<AddClientActivity> { }
        }
        policyAdapter = ClientListAdapter(this, this)
        binding!!.rvPolicies.adapter = policyAdapter


        binding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        binding!!.nestedScrollView.viewTreeObserver.addOnScrollChangedListener(
            ViewTreeObserver.OnScrollChangedListener {
                val view: View =
                    binding!!.nestedScrollView.getChildAt(binding!!.nestedScrollView.childCount - 1) as View
                val diff: Int =
                    view.bottom - (binding!!.nestedScrollView.getHeight() + binding!!.nestedScrollView
                        .scrollY)
                if (diff == 0 && hasMore) {

                    viewModel!!.getClientList(this, page)
                }
            })

        binding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding!!.swipeRefreshLayout.isRefreshing = false
            policyList!!.clear()
            page = 1
            viewModel!!.getClientList(this, page)
        })

    }

    override fun onResume() {
        super.onResume()
        policyList!!.clear()
        Log.e("resume", "resume")
        page = 1
        viewModel!!.getClientList(this, page)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<ClientData?> = arrayListOf()

        for (item in policyList!!) {
                if (item!!.firstname!!.toLowerCase()
                    .contains(text.toLowerCase())
                || item.client_id!!.toLowerCase().contains(text.toLowerCase())
                || item.pan_number!!.toLowerCase().contains(text.toLowerCase())
                || item.gst!!.toLowerCase().contains(text.toLowerCase())
                || item.password!!.toLowerCase().contains(text.toLowerCase())
                || item.mobile!!.toLowerCase().contains(text.toLowerCase())
                || item.status!!.toLowerCase().contains(text.toLowerCase())
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.isNotEmpty()) {
            policyAdapter!!.updateList(filteredList)
        } else {
            policyAdapter!!.updateList(policyList!!)
        }
    }

    override fun onStarted() {
        binding!!.loader.show()
    }

    override fun onSuccess(data: ClientListResponse) {
        page++
        hasMore = data.hasmore!!
        binding!!.loader.hide()
        policyList!!.addAll(data.data!!)
        policyAdapter!!.updateList(policyList!!)
        hideProgress()
    }

    override fun onFailure(message: String) {
        hideProgress()
        if (message.contains("Unauthenticated.")) {
            viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER, false)
            launchLoginActivity<LoginActivity> { }
        }
        showToastMessage(message)
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
    }

    override fun onItemClick(data: ClientData) {
        launchActivity<ClientDetailsActivity> {
            this.putExtra(AppConstants.CLIENT, data)
        }
    }

    override fun onEdit(data: ClientData) {
        launchActivity<EditClientActivity> {
            this.putExtra(AppConstants.CLIENT, data)
        }
    }

    override fun onWhatsApp(data: ClientData) {
        var sendData = "Username:${data.client_id}\nPassword:${data.password}"
        val url = "https://api.whatsapp.com/send?phone=${data.mobile}&text=${URLEncoder.encode(sendData, "UTF-8")}"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onDelete(id: String, position: Int) {
        deleteAlert(this
        ) { dialog, which ->
            viewModel!!.deleteClient(this, id, position)
        }

    }

    override fun onSuccessDelete(data: CommonResponse, position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        policyList!!.removeAt(position)
        policyAdapter!!.updateList(policyList!!)
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER, false)
        if (message.contains("Unauthenticated")) {
            launchLoginActivity<LoginActivity> { }
        }
    }
}