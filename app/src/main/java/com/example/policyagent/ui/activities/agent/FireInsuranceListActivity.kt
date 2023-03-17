package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceListResponse
import com.example.policyagent.databinding.ActivityFireInsuranceListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.FireInsuranceListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.FireInsuranceListListener
import com.example.policyagent.ui.viewmodels.agent.FireInsuranceListViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FireInsuranceListActivity : BaseActivity(), KodeinAware, FireInsuranceListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityFireInsuranceListBinding? = null
    private var viewModel: FireInsuranceListViewModel? = null
    var policyAdapter: FireInsuranceListAdapter? = null
    var policyList: ArrayList<FireInsuranceData?> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire_insurance_list)
        viewModel = ViewModelProvider(this, factory)[FireInsuranceListViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.fire_insurance)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        policyAdapter = FireInsuranceListAdapter(this, this)
        binding!!.rvPolicies.adapter = policyAdapter

        viewModel!!.getFireInsurance(this)

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

    private fun filter(text: String) {
        val filteredList: ArrayList<FireInsuranceData?> = arrayListOf()

        for (item in policyList!!) {
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
            policyAdapter!!.updateList(policyList!!)
        }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: FireInsuranceListResponse) {
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

    override fun onItemClick(data: FireInsuranceData) {
        launchActivity<FireInsuranceDetailsActivity> {
            this.putExtra(AppConstants.FIRE_INSURANCE,data)
        }
    }
}