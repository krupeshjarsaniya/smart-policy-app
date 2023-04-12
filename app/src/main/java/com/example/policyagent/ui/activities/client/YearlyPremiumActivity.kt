package com.example.policyagent.ui.activities.client

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.model.MonthSelection
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.databinding.ActivityYearlyPremiumBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.client.MonthlyPremiumAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.YearlyPremiumListener
import com.example.policyagent.ui.viewmodels.client.YearlyPremiumViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class YearlyPremiumActivity : BaseActivity(), KodeinAware, YearlyPremiumListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityYearlyPremiumBinding? = null
    private var viewModel: YearlyPremiumViewModel? = null
    var monthlyAdapter: MonthlyPremiumAdapter? = null
    var months: ArrayList<MonthSelection> = ArrayList()
    var year: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yearly_premium)
        viewModel = ViewModelProvider(this, factory)[YearlyPremiumViewModel::class.java]
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        binding!!.cvPolicy.setOnClickListener {
        }
        if(intent.hasExtra(AppConstants.YEAR)){
            year = intent.getStringExtra(AppConstants.YEAR)
            binding!!.appBar.tvTitle.text = "$year ${resources.getString(R.string.year)}"
        }
        months.add(MonthSelection("JAN",false))
        months.add(MonthSelection("FEB",false))
        months.add(MonthSelection("MAR",false))
        months.add(MonthSelection("APR",false))
        months.add(MonthSelection("MAY",false))
        months.add(MonthSelection("JUN",false))
        months.add(MonthSelection("JUL",false))
        months.add(MonthSelection("AUG",false))
        months.add(MonthSelection("SEPT",false))
        months.add(MonthSelection("OCT",false))
        months.add(MonthSelection("NOV",false))
        months.add(MonthSelection("DEC",false))
        monthlyAdapter = MonthlyPremiumAdapter(this,this)
        binding!!.rvMonth.adapter = monthlyAdapter
        monthlyAdapter!!.updateList(months)
    }

    override fun onStarted() {
        
    }

    override fun onSuccess(response: CommonResponse, type: String) {
        
    }

    override fun onFailure(message: String) {
        
    }

    override fun onError(error: HashMap<String, Any>) {
        
    }

    override fun onMonthSelected(month: String) {
        
    }
}