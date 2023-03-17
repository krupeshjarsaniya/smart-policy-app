package com.example.policyagent.ui.activities.client

import android.R.attr.country
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityMyInsurancePortfolioBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.client.PolicyAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.client.MyInsurancePortfolioViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MyInsurancePortfolioActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityMyInsurancePortfolioBinding? = null
    private var viewModel: MyInsurancePortfolioViewModel? = null
    var policyAdapter: PolicyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_insurance_portfolio)
        viewModel = ViewModelProvider(this, factory)[MyInsurancePortfolioViewModel::class.java]
        /*val members = resources.getStringArray(R.array.family_members)
        val memberAdapter = ArrayAdapter(this, R.layout.dropdown_item, members)
        binding!!.tvFamilyMembers.setAdapter(memberAdapter)
        val insurances = resources.getStringArray(R.array.insurance_type)
        val insuranceAdapter = ArrayAdapter(this, R.layout.dropdown_item, insurances)
        binding!!.tvType.setAdapter(insuranceAdapter)*/
        val members = resources.getStringArray(R.array.family_members)
        val memberAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, members)
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.tvFamilyMembers.adapter = memberAdapter
        binding!!.tvFamilyMembers.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.primary_color))
                (parent!!.getChildAt(0) as TextView).gravity = Gravity.CENTER
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
                (parent!!.getChildAt(0) as TextView).gravity = Gravity.CENTER
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        policyAdapter = PolicyAdapter(this)
        binding!!.rvPolicies.adapter = policyAdapter
        binding!!.appBar.tvTitle.text = resources.getString(R.string.my_portfolio)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
    }
}