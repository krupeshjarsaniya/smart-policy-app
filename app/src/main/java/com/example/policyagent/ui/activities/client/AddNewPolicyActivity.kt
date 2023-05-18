package com.example.policyagent.ui.activities.client

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.policylist.PolicyListResponse
import com.example.policyagent.data.responses.policylist.PolicyData
import com.example.policyagent.databinding.ActivityAddNewPolicyBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.client.SearchPolicyAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.PolicyListListener
import com.example.policyagent.ui.viewmodels.client.AddNewPolicyViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.hide
import com.example.policyagent.util.launchActivity
import com.example.policyagent.util.show
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AddNewPolicyActivity : BaseActivity(), KodeinAware,PolicyListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddNewPolicyBinding? = null
    private var viewModel: AddNewPolicyViewModel? = null
    //var searchPolicyAdapter: SearchPolicyAdapter? = null
    var policyAdapter: SearchPolicyAdapter? = null
    var policyList: ArrayList<PolicyData?> = arrayListOf()
    var page = 1
    var hasMore = true
    var userName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_policy)
        viewModel = ViewModelProvider(this, factory)[AddNewPolicyViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.add_new_policy)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
        viewModel!!.getLoggedInUser().observe(this, Observer { user ->
            if (user != null) {
                userName = "${user.firstname} ${user.lastname}"
            }
        })
        binding!!.appBar.btnAddPolicy.visibility = View.VISIBLE
        binding!!.appBar.btnAddPolicy.setOnClickListener{
            launchActivity<AddPolicyFormActivity> {  }
        }
        policyAdapter = SearchPolicyAdapter(this,this,userName)
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

        binding!!.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val view: View =
                binding!!.nestedScrollView.getChildAt(binding!!.nestedScrollView.childCount - 1) as View
            val diff: Int =
                view.bottom - (binding!!.nestedScrollView.height + binding!!.nestedScrollView
                    .scrollY)
            if (diff == 0 && hasMore) {

                viewModel!!.getPolicyList(this, page)
            }
        }

        binding!!.swipeRefreshLayout.setOnRefreshListener {
            binding!!.swipeRefreshLayout.isRefreshing = false
            policyList.clear()
            page = 1
            viewModel!!.getPolicyList(this, page)
        }
    }

    override fun onResume() {
        super.onResume()
        policyList.clear()
        page = 1
        viewModel!!.getPolicyList(this, page)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<PolicyData?> = arrayListOf()

        for (item in policyList) {
            if (item!!.memberDetails!!.firstname!!.toLowerCase().contains(text.toLowerCase())
                || item.policy_number!!.toLowerCase().contains(text.toLowerCase())
                || item.company_name!!.toLowerCase().contains(text.toLowerCase())
                || item.model_type!!.toLowerCase().contains(text.toLowerCase())
                || item.start_date!!.toLowerCase().contains(text.toLowerCase())
                || item.end_date!!.toLowerCase().contains(text.toLowerCase())
                || item.sa!!.toLowerCase().contains(text.toLowerCase())
                || item.premium!!.toLowerCase().contains(text.toLowerCase())
            ) {
                filteredList.add(item)
            }
        }

        if (filteredList.isNotEmpty()) {
            policyAdapter!!.updateList(filteredList)
        } else{
            policyAdapter!!.updateList(policyList)
        }
    }

    override fun onStarted() {
        binding!!.loader.show()
    }

    override fun onSuccess(data: PolicyListResponse) {
        page++
        hasMore = data.hasmore!!
        binding!!.loader.hide()
        policyList.addAll(data.data!!)
        policyAdapter!!.updateList(policyList)
        hideProgress()
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(errors: HashMap<String, Any>) {
        
    }

    override fun onLogout(message: String) {
        
    }

    override fun onItemClick(data: PolicyData) {
        /*launchActivity<FireInsuranceDetailsActivity> {
            this.putExtra(AppConstants.FIRE_INSURANCE,data)
        }*/
    }

    override fun onDelete(id: String,position: Int) {
        viewModel!!.deletePolicy(this,id,position)
    }

    override fun onEdit(data: PolicyData) {
        launchActivity<EditPolicyFormActivity> {
            this.putExtra(AppConstants.POLICY,data)
        }
    }

    override fun onSuccessDelete(data: CommonResponse, position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        policyList.removeAt(position)
        policyAdapter!!.updateList(policyList)
    }
}