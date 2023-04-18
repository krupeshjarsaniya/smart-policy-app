package com.example.policyagent.ui.activities.client

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.model.MonthSelection
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.monthlydue.MonthlyData
import com.example.policyagent.data.responses.monthlydue.MonthlyDueResponse
import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceData
import com.example.policyagent.databinding.ActivityYearlyPremiumBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.agent.*
import com.example.policyagent.ui.adapters.client.MonthlyDueAdapter
import com.example.policyagent.ui.adapters.client.MonthlyPremiumAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.YearlyPremiumListener
import com.example.policyagent.ui.viewmodels.client.YearlyPremiumViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class YearlyPremiumActivity : BaseActivity(), KodeinAware, YearlyPremiumListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityYearlyPremiumBinding? = null
    private var viewModel: YearlyPremiumViewModel? = null
    var monthlyAdapter: MonthlyPremiumAdapter? = null
    var monthlyDueAdapter: MonthlyDueAdapter? = null
    var months: ArrayList<MonthSelection> = ArrayList()
    var policy: ArrayList<MonthlyData?> = ArrayList()
    var year: String? = ""
    var memberId: String? = ""
    var type: String? = ""
    var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yearly_premium)
        viewModel = ViewModelProvider(this, factory)[YearlyPremiumViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        viewModel!!.getMonthlyDue(memberId!!, type!!, year!!)

        if (intent.hasExtra(AppConstants.YEAR)) {
            year = intent.getStringExtra(AppConstants.YEAR)
            binding!!.appBar.tvTitle.text = "$year ${resources.getString(R.string.year)}"
        }
        if (intent.hasExtra(AppConstants.MEMBER_ID)) {
            memberId = intent.getStringExtra(AppConstants.MEMBER_ID)
        }
        if (intent.hasExtra(AppConstants.INSURANCE_TYPE)) {
            type = intent.getStringExtra(AppConstants.INSURANCE_TYPE)
        }
        months.add(MonthSelection(1, "JAN", true))
        months.add(MonthSelection(2, "FEB", false))
        months.add(MonthSelection(3, "MAR", false))
        months.add(MonthSelection(4, "APR", false))
        months.add(MonthSelection(5, "MAY", false))
        months.add(MonthSelection(6, "JUN", false))
        months.add(MonthSelection(7, "JUL", false))
        months.add(MonthSelection(8, "AUG", false))
        months.add(MonthSelection(9, "SEP", false))
        months.add(MonthSelection(10, "OCT", false))
        months.add(MonthSelection(11, "NOV", false))
        months.add(MonthSelection(12, "DEC", false))
        monthlyAdapter = MonthlyPremiumAdapter(this, this)
        binding!!.rvMonth.adapter = monthlyAdapter
        monthlyAdapter!!.updateList(months)
        monthlyDueAdapter = MonthlyDueAdapter(this, this)
        binding!!.rvDetails.adapter = monthlyDueAdapter
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(response: MonthlyDueResponse) {
        hideProgress()
        policy.addAll(response.data!!)
        monthlyDueAdapter!!.updateList(policy[0]!!.value!!)
        //monthlyDueAdapter!!.updateList(response.data!!)
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onError(error: HashMap<String, Any>) {
        hideProgress()
    }

    override fun onMonthSelected(position: Int) {
        for (i in 0 until policy.size) {
            if (policy[i]!!.month == months[position].month_no) {
                monthlyDueAdapter!!.updateList(policy[i]!!.value!!)
            }
        }
    }

    override fun onItemClick(portfolio: Portfolio) {
        var json = gson.toJson(portfolio)
        when (portfolio.insuranceType) {
            "car_moter_insurances" -> {
                launchActivity<CarInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, CarInsuranceData::class.java)
                    this.putExtra(AppConstants.CAR_INSURANCE, data)
                    //this.putExtra(AppConstants.PERSONAL_DETAILS, personalDetails)
                }
            }
            "fire_insurances" -> {
                launchActivity<FireInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, FireInsuranceData::class.java)
                    this.putExtra(AppConstants.FIRE_INSURANCE, data)
                    //this.putExtra(AppConstants.PERSONAL_DETAILS, personalDetails)
                }
            }
            "wc_insurances" -> {
                launchActivity<WcInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, WcInsuranceData::class.java)
                    this.putExtra(AppConstants.WC_INSURANCE, data)
                    //this.putExtra(AppConstants.PERSONAL_DETAILS, personalDetails)
                }
            }
            "life_insurances" -> {
                launchActivity<LifeInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, LifeInsuranceData::class.java)
                    this.putExtra(AppConstants.LIFE_INSURANCE, data)
                    //this.putExtra(AppConstants.PERSONAL_DETAILS, personalDetails)
                }
            }
            "health_insurances" -> {
                launchActivity<HealthInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, HealthInsuranceData::class.java)
                    this.putExtra(AppConstants.HEALTH_INSURANCE, data)
                    //this.putExtra(AppConstants.PERSONAL_DETAILS, personalDetails)
                }
            }
        }
    }
}