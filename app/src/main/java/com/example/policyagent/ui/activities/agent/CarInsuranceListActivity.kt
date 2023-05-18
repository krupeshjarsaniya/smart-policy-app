package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceListResponse
import com.example.policyagent.databinding.ActivityCarInsuranceListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.CarInsuranceListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.CarInsuranceListListener
import com.example.policyagent.ui.viewmodels.agent.CarInsuranceListViewModel
import com.example.policyagent.util.*
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
    var page = 1
    var hasMore = true
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

        binding!!.nestedScrollView.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
            val view: View =
                binding!!.nestedScrollView.getChildAt(binding!!.nestedScrollView.getChildCount() - 1) as View
            val diff: Int =
                view.bottom - (binding!!.nestedScrollView.getHeight() + binding!!.nestedScrollView
                    .scrollY)
            if (diff == 0 && hasMore) {
                binding!!.loader.show()
                viewModel!!.getCarInsurance(this, page)
            }
        })

        binding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding!!.swipeRefreshLayout.isRefreshing = false
            policyList!!.clear()
            page = 1
            viewModel!!.getCarInsurance(this, page)
        })
    }

    override fun onResume() {
        super.onResume()
        page = 1
        viewModel!!.getCarInsurance(this, page)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<CarInsuranceData?> = arrayListOf()

        for (item in policyList!!) {
            if (item!!.client_Personal_Details!!.firstname!!.toLowerCase()
                    .contains(text.toLowerCase())
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
        } else {
            policyAdapter!!.updateList(policyList!!)
        }
    }

    override fun onStarted() {
        binding!!.loader.show()
        //showProgress(true)
    }

    override fun onSuccess(data: CarInsuranceListResponse) {
        page++
        hasMore = data.hasmore!!
        binding!!.loader.hide()
        //policyList!!.clear()
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

    override fun onItemClick(data: CarInsuranceData) {
        launchActivity<CarInsuranceDetailsActivity> {
            this.putExtra(AppConstants.CAR_INSURANCE, data)
        }
    }

    override fun onDelete(id: String, position: Int) {
        viewModel!!.deleteCarInsurance(this, id, position)
    }

    override fun onEdit(data: CarInsuranceData) {
        launchActivity<EditCarInsuranceActivity> {
            this.putExtra(AppConstants.CAR_INSURANCE, data)
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