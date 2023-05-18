package com.example.policyagent.ui.activities.agent

import androidx.appcompat.app.AppCompatActivity
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
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.expiredpolicylist.ExpiredPolicyResponse
import com.example.policyagent.data.responses.expiredpolicylist.Insurance
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceData
import com.example.policyagent.databinding.ActivityExpiredBinding
import com.example.policyagent.databinding.ActivityUpcomingExpiredBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.ExpiredPolicyListAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.ExpiredListListener
import com.example.policyagent.ui.viewmodels.agent.ExpiredPolicyListViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class UpcomingExpiredActivity : BaseActivity(), KodeinAware, ExpiredListListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityUpcomingExpiredBinding? = null
    private var viewModel: ExpiredPolicyListViewModel? = null
    var policyAdapter: ExpiredPolicyListAdapter? = null
    var policyList: ArrayList<Insurance?>? = ArrayList()
    var page = 1
    var hasMore = true
    var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming_expired)
        viewModel = ViewModelProvider(this, factory)[ExpiredPolicyListViewModel::class.java]
        viewModel!!.listener = this
        policyAdapter = ExpiredPolicyListAdapter(this,this)
        binding!!.rvPolicies.adapter = policyAdapter
        binding!!.appBar.tvTitle.text = resources.getString(R.string.upcoming_expire_policy)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
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
                    viewModel!!.getUpcomingInsurance(this, page)
                }
            })

        binding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding!!.swipeRefreshLayout.isRefreshing = false
            policyList!!.clear()
            page = 1
            viewModel!!.getUpcomingInsurance(this, page)
        })
    }

    override fun onResume() {
        super.onResume()
        Log.e("resume", "resume")
        page = 1
        viewModel!!.getUpcomingInsurance(this, page)
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Insurance?> = arrayListOf()

        for (item in policyList!!) {
            if (item!!.client_Personal_Details!!.firstname!!.toLowerCase().contains(text.toLowerCase())
                || item.policy_number!!.toLowerCase().contains(text.toLowerCase())
                || item.psd!!.toLowerCase().contains(text.toLowerCase())
                || item.ped!!.toLowerCase().contains(text.toLowerCase())
                || item.rsd!!.toLowerCase().contains(text.toLowerCase())
                || item.red!!.toLowerCase().contains(text.toLowerCase())
                || item.company_name!!.toLowerCase().contains(text.toLowerCase())
                || item.premium_amount!!.toLowerCase().contains(text.toLowerCase())
                || item.insuranceType!!.toLowerCase().contains(text.toLowerCase())
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

    override fun onSuccess(data: ExpiredPolicyResponse) {
        page++
        hasMore = data.hasmore!!
        binding!!.loader.hide()
        //policyList!!.clear()
        policyList!!.addAll(data.insurance!!)
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

    override fun onItemClick(data: Insurance) {
        var json = gson.toJson(data)
        when (data.insuranceType) {
            "CarInsurance" -> {
                launchActivity<CarInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, CarInsuranceData::class.java)
                    this.putExtra(AppConstants.CAR_INSURANCE,data)
                }
            }
            "OtherInsurance" -> {
                launchActivity<FireInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, FireInsuranceData::class.java)
                    this.putExtra(AppConstants.FIRE_INSURANCE,data)
                }
            }
            "WcInsurance" -> {
                launchActivity<WcInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, WcInsuranceData::class.java)
                    this.putExtra(AppConstants.WC_INSURANCE,data)
                }
            }
            "LifeInsurance" -> {
                launchActivity<LifeInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, LifeInsuranceData::class.java)
                    this.putExtra(AppConstants.LIFE_INSURANCE,data)
                }
            }
            "HealthInsurance" -> {
                launchActivity<HealthInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, HealthInsuranceData::class.java)
                    this.putExtra(AppConstants.HEALTH_INSURANCE,data)
                }
            }
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