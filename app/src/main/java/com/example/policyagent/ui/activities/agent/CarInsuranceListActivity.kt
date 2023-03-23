package com.example.policyagent.ui.activities.agent


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceListResponse
import com.example.policyagent.databinding.ActivityCarInsuranceListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.CarInsuranceListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.CarInsuranceListListener
import com.example.policyagent.ui.viewmodels.agent.CarInsuranceListViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.example.policyagent.util.show
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class CarInsuranceListActivity : BaseActivity(), KodeinAware, CarInsuranceListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityCarInsuranceListBinding? = null
    private var viewModel: CarInsuranceListViewModel? = null
    var policyAdapter: CarInsuranceListAdapter? = null
    var policyList: ArrayList<CarInsuranceData?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_insurance_list)
        viewModel = ViewModelProvider(this, factory)[CarInsuranceListViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.car_insurance)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.btnAddPolicy.show()
        binding!!.appBar.btnAddPolicy.setOnClickListener {
            launchActivity<AddCarInsuranceActivity> {

            }
        }
        policyAdapter = CarInsuranceListAdapter(this, this)
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
        viewModel!!.getCarInsurance(this)
    }

private fun filter(text: String) {
    val filteredList: ArrayList<CarInsuranceData?> = arrayListOf()

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

    override fun onSuccess(data: CarInsuranceListResponse) {
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

    override fun onItemClick(data: CarInsuranceData) {
        launchActivity<CarInsuranceDetailsActivity> {
            this.putExtra(AppConstants.CAR_INSURANCE,data)
        }
    }

    override fun onDelete(id: String,position: Int) {
        viewModel!!.deleteCarInsurance(this,id,position)
    }

    override fun onSuccessDelete(data: CommonResponse,position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        policyList!!.removeAt(position)
        policyAdapter!!.updateList(policyList!!)
    }
}