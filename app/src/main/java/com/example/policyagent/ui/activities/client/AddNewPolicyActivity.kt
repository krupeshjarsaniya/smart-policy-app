package com.example.policyagent.ui.activities.client

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
import com.example.policyagent.util.launchActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_policy)
        viewModel = ViewModelProvider(this, factory)[AddNewPolicyViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.add_new_policy)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
        binding!!.appBar.btnAddPolicy.visibility = View.VISIBLE
        binding!!.appBar.btnAddPolicy.setOnClickListener{
            launchActivity<AddPolicyFormActivity> {  }
        }
        policyAdapter = SearchPolicyAdapter(this,this)
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
    }

    override fun onResume() {
        super.onResume()
        viewModel!!.getPolicyList(this)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<PolicyData?> = arrayListOf()

        for (item in policyList!!) {
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
        showProgress(true)

    }

    override fun onSuccess(data: PolicyListResponse) {
        hideProgress()
        policyList = data.data!!
        policyAdapter!!.updateList(data.data)
    }

    override fun onFailure(message: String) {
        
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