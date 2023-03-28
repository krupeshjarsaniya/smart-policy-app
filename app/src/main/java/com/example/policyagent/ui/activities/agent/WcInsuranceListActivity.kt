package com.example.policyagent.ui.activities.agent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceData
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceListResponse
import com.example.policyagent.databinding.ActivityLifeInsuranceListBinding
import com.example.policyagent.databinding.ActivityWcInsuranceListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.LifeInsuranceListAdapter
import com.example.policyagent.ui.adapters.agent.WcInsuranceListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.ui.listeners.WcInsuranceListListener
import com.example.policyagent.ui.viewmodels.agent.LifeInsuranceListViewModel
import com.example.policyagent.ui.viewmodels.agent.WcInsuranceListViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.example.policyagent.util.show
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WcInsuranceListActivity : BaseActivity(), KodeinAware, WcInsuranceListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityWcInsuranceListBinding? = null
    private var viewModel: WcInsuranceListViewModel? = null
    var policyAdapter: WcInsuranceListAdapter? = null
    var policyList: ArrayList<WcInsuranceData?> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wc_insurance_list)
        viewModel = ViewModelProvider(this, factory)[WcInsuranceListViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.wc_insurance)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.btnAddPolicy.show()
        binding!!.appBar.btnAddPolicy.setOnClickListener {
            launchActivity<AddWcInsuranceActivity> {

            }
        }
        policyAdapter = WcInsuranceListAdapter(this, this)
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
        viewModel!!.getWcInsurance(this)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<WcInsuranceData?> = arrayListOf()

        for (item in policyList) {
            if (item!!.client_Personal_Details!!.firstname!!.toLowerCase().contains(text.toLowerCase())
                || item.policy_number!!.toLowerCase().contains(text.toLowerCase())
                || item.plan_name!!.toLowerCase().contains(text.toLowerCase())
                || item.rsd!!.toLowerCase().contains(text.toLowerCase())
                || item.red!!.toLowerCase().contains(text.toLowerCase())
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

    override fun onSuccess(data: WcInsuranceListResponse) {
        hideProgress()
        policyList = data.data!!
        policyAdapter!!.updateList(data.data)
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
    }

    override fun onItemClick(data: WcInsuranceData) {
        launchActivity<WcInsuranceDetailsActivity> {
            this.putExtra(AppConstants.WC_INSURANCE,data)
        }
    }

    override fun onDelete(id: String,position: Int) {
        viewModel!!.deleteWcInsurance(this,id,position)
    }

    override fun onEdit(data: WcInsuranceData) {
        launchActivity<EditWcInsuranceActivity> {
            this.putExtra(AppConstants.WC_INSURANCE,data)
        }
    }

    override fun onSuccessDelete(data: CommonResponse, position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        policyList.removeAt(position)
        policyAdapter!!.updateList(policyList)
    }
}