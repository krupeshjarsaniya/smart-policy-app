package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.policyagent.R
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse
import com.example.policyagent.databinding.ActivityLifeInsuranceListBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.LifeInsuranceListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.ui.viewmodels.agent.LifeInsuranceListViewModel
import com.example.policyagent.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LifeInsuranceListActivity : BaseActivity(), KodeinAware, LifeInsuranceListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityLifeInsuranceListBinding? = null
    private var viewModel: LifeInsuranceListViewModel? = null
    var policyAdapter: LifeInsuranceListAdapter? = null
    var policyList: ArrayList<LifeInsuranceData?>? = ArrayList()
    var page = 1
    var hasMore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_life_insurance_list)
        viewModel = ViewModelProvider(this, factory)[LifeInsuranceListViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.life_insurance)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.appBar.btnAddPolicy.show()
        binding!!.appBar.btnAddPolicy.setOnClickListener {
            launchActivity<AddLifeInsuranceActivity> { }
        }
        policyAdapter = LifeInsuranceListAdapter(this, this)
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
            OnScrollChangedListener {
                val view: View =
                    binding!!.nestedScrollView.getChildAt(binding!!.nestedScrollView.childCount - 1) as View
                val diff: Int =
                    view.bottom - (binding!!.nestedScrollView.getHeight() + binding!!.nestedScrollView
                        .scrollY)
                if (diff == 0 && hasMore) {

                    viewModel!!.getLifeInsurance(this, page)
                }
            })

        binding!!.swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            binding!!.swipeRefreshLayout.isRefreshing = false
            policyList!!.clear()
            page = 1
            viewModel!!.getLifeInsurance(this, page)
        })

    }

    override fun onResume() {
        super.onResume()
        Log.e("resume", "resume")
        page = 1
        viewModel!!.getLifeInsurance(this, page)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<LifeInsuranceData?> = arrayListOf()

        for (item in policyList!!) {
            if (item!!.client_Personal_Details!!.firstname!!.toLowerCase()
                    .contains(text.toLowerCase())
                || item.policy_number!!.toLowerCase().contains(text.toLowerCase())
                || item.plan_name!!.toLowerCase().contains(text.toLowerCase())
                || item.psd!!.toLowerCase().contains(text.toLowerCase())
                || item.ped!!.toLowerCase().contains(text.toLowerCase())
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

    override fun onSuccess(data: LifeInsuranceListResponse) {
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

    override fun onDelete(id: String, position: Int) {
        deleteAlert(this
        ) { dialog, which ->
            viewModel!!.deleteLifeInsurance(this, id, position)
        }
    }

    override fun onEdit(data: LifeInsuranceData) {
        launchActivity<EditLifeInsuranceActivity> {
            this.putExtra(AppConstants.LIFE_INSURANCE, data)
        }
    }

    override fun onSuccessDelete(data: CommonResponse, position: Int) {
        hideProgress()
        showToastMessage(data.message!!)
        policyList!!.removeAt(position)
        policyAdapter!!.updateList(policyList!!)
    }

    override fun onItemClick(data: LifeInsuranceData) {
        launchActivity<LifeInsuranceDetailsActivity> {
            this.putExtra(AppConstants.LIFE_INSURANCE, data)
        }
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER, false)
        if (message.contains("Unauthenticated")) {
            launchLoginActivity<LoginActivity> { }
        }
    }
}