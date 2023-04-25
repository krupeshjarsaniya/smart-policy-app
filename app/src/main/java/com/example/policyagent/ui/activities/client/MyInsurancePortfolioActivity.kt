package com.example.policyagent.ui.activities.client


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.memberlist.MemberData
import com.example.policyagent.data.responses.memberlist.MemberListResponse
import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.data.responses.portfolio.PortfolioResponse
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceData
import com.example.policyagent.databinding.ActivityMyInsurancePortfolioBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.activities.agent.*
import com.example.policyagent.ui.adapters.client.PolicyAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.PortfolioListener
import com.example.policyagent.ui.viewmodels.client.MyInsurancePortfolioViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.example.policyagent.util.launchLoginActivity
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MyInsurancePortfolioActivity : BaseActivity(), KodeinAware, PortfolioListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityMyInsurancePortfolioBinding? = null
    private var viewModel: MyInsurancePortfolioViewModel? = null
    var policyAdapter: PolicyAdapter? = null
    private var portfolioList: ArrayList<Portfolio?>? = ArrayList()
    private var policyList: ArrayList<String?>? = ArrayList()
    var gson = Gson()
    var clientList: java.util.ArrayList<String>? = java.util.ArrayList()
    var clients: java.util.ArrayList<MemberData?>? = java.util.ArrayList()
    var selectedId = ""
    var selectedMember = ""
    var selectedType = ""
    var personalDetails : ClientPersonalDetails? = null
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_insurance_portfolio)
        viewModel = ViewModelProvider(this, factory)[MyInsurancePortfolioViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getPortFolio(page,true)
        val memberJson: String = viewModel!!.getPreference().getStringValue(AppConstants.MEMBERS)!!
        val memberObj: MemberListResponse =
            gson.fromJson(memberJson, MemberListResponse::class.java)
        clients = memberObj.data
        clientList!!.add("All")
        for (i in 0 until clients!!.size) {
            clientList!!.add(clients!![i]!!.firstname!!)
        }
        val memberAdapter = ArrayAdapter(this, R.layout.dropdown_item, clientList!!)
        binding!!.tvFamilyMembers.adapter = memberAdapter
        binding!!.tvFamilyMembers.onItemSelectedListener = object:
            OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                selectedMember = binding!!.tvFamilyMembers.selectedItem.toString()
                filter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val insurances = resources.getStringArray(R.array.insurance_type)
        val insuranceAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, insurances)
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.tvType.adapter = insuranceAdapter
        binding!!.tvType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                selectedType = binding!!.tvType.selectedItem.toString()
                filter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        policyAdapter = PolicyAdapter(this,this)
        binding!!.rvPolicies.adapter = policyAdapter
        binding!!.appBar.tvTitle.text = resources.getString(R.string.my_portfolio)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
    }


    fun filter(){
        val filteredList: ArrayList<Portfolio?> = arrayListOf()
        when (selectedType) {
            "Life Insurance" -> {
               selectedType = "life_insurances"
            }
            "Health Insurance" -> {
                selectedType = "health_insurances"
            }
            "Car/Motor Insurance" -> {
                selectedType = "car_moter_insurances"
            }
            "Wc Insurance" -> {
                selectedType = "wc_insurances"
            }
            "Fire Insurance" -> {
                selectedType = "fire_insurances"
            }
        }
        for (item in portfolioList!!) {
            if(selectedMember == "All" && selectedId == "All" && selectedType == "All"){
                filteredList.add(item)
            } else if(selectedMember == "All" && selectedId == "All"){
                if(item!!.insuranceType.toString() == selectedType){
                    filteredList.add(item)
                }
            } else if(selectedMember == "All" && selectedType == "All"){
                if(item!!.id.toString() == selectedId){
                    filteredList.add(item)
                }
            } else if(selectedId == "All" && selectedType == "All"){
                if(item!!.member_name.toString() == selectedMember){
                    filteredList.add(item)
                }
            }
            else if(selectedId == "All"){
                if(item!!.member_name.toString() == selectedMember && item.insuranceType.toString() == selectedType){
                    filteredList.add(item)
                }
            } else if(selectedMember == "All"){
                if(item!!.id.toString() == selectedId && item.insuranceType.toString() == selectedType){
                    filteredList.add(item)
                }
            } else if(selectedType == "All"){
                if(item!!.id.toString() == selectedId && item.member_name.toString() == selectedMember){
                    filteredList.add(item)
                }
            } else if (item!!.member_name.toString() == selectedMember && item.id.toString() == selectedId && item.insuranceType.toString() == selectedType) {
                filteredList.add(item)
            }
        }
            policyAdapter!!.updateList(filteredList)
        binding!!.tvTotalPolicy.text = " ${filteredList!!.size} "
    }

    override fun onStarted() {
        showProgress(true)
    }

    private fun setPolicySpinnerData(){
        policyList!!.add(0,"All")
        for(portfolio in portfolioList!!){
            policyList!!.add("${resources.getString(R.string.policy)} #${portfolio!!.id}")
        }
        val policySpAdapter: ArrayAdapter<*> = ArrayAdapter<String?>(this, R.layout.dropdown_item, policyList!!)
        binding!!.spPolicy.adapter = policySpAdapter
        binding!!.spPolicy.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.white))
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                selectedId = if(position != 0) {
                    binding!!.spPolicy.selectedItem.toString().substring(8, binding!!.spPolicy.selectedItem.toString().length)
                }else {
                    binding!!.spPolicy.selectedItem.toString()
                }
                filter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: PortfolioResponse) {
        portfolioList!!.addAll(data.data!!.portfolio!!)
        policyAdapter!!.updateList(portfolioList!!)
        if(data.data.client!!.isNotEmpty()) {
            personalDetails = data.data.client[0]
        }
        hideProgress()

        binding!!.tvTotalPolicy.text = " ${portfolioList!!.size} "
        page++
        if(!data.hasmore!!) {
            setPolicySpinnerData()
        }
        if(data.hasmore){
            viewModel!!.getPortFolio(page,false)
        }
    }

    override fun onFailure(message: String) {
        hideProgress()
        if(message.contains("Unauthenticated.")){
            viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
            launchLoginActivity<LoginActivity> {  }
        }
        showToastMessage(message)
    }

    override fun onItemClick(portfolio: Portfolio) {
        var json = gson.toJson(portfolio)
        when (portfolio.insuranceType) {
            "car_moter_insurances" -> {
                launchActivity<CarInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, CarInsuranceData::class.java)
                    this.putExtra(AppConstants.CAR_INSURANCE,data)
                    this.putExtra(AppConstants.PERSONAL_DETAILS,personalDetails)
                }
            }
            "fire_insurances" -> {
                launchActivity<FireInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, FireInsuranceData::class.java)
                    this.putExtra(AppConstants.FIRE_INSURANCE,data)
                    this.putExtra(AppConstants.PERSONAL_DETAILS,personalDetails)
                }
            }
            "wc_insurances" -> {
                launchActivity<WcInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, WcInsuranceData::class.java)
                    this.putExtra(AppConstants.WC_INSURANCE,data)
                    this.putExtra(AppConstants.PERSONAL_DETAILS,personalDetails)
                }
            }
            "life_insurances" -> {
                launchActivity<LifeInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, LifeInsuranceData::class.java)
                    this.putExtra(AppConstants.LIFE_INSURANCE,data)
                    this.putExtra(AppConstants.PERSONAL_DETAILS,personalDetails)
                }
            }
            "health_insurances" -> {
                launchActivity<HealthInsuranceDetailsActivity> {
                    var data = gson.fromJson(json, HealthInsuranceData::class.java)
                    this.putExtra(AppConstants.HEALTH_INSURANCE,data)
                    this.putExtra(AppConstants.PERSONAL_DETAILS,personalDetails)
                }
            }
        }
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
    }
}