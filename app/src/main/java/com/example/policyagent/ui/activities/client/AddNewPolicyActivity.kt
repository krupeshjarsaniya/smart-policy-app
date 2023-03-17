package com.example.policyagent.ui.activities.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.databinding.ActivityAddNewPolicyBinding
import com.example.policyagent.databinding.ActivityMyInsurancePortfolioBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.client.SearchPolicyAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.viewmodels.client.AddNewPolicyViewModel
import com.example.policyagent.ui.viewmodels.client.MyInsurancePortfolioViewModel
import com.example.policyagent.util.launchActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AddNewPolicyActivity : BaseActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddNewPolicyBinding? = null
    private var viewModel: AddNewPolicyViewModel? = null
    var searchPolicyAdapter: SearchPolicyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_policy)
        viewModel = ViewModelProvider(this, factory)[AddNewPolicyViewModel::class.java]
        binding!!.appBar.tvTitle.text = resources.getString(R.string.add_new_policy)
        binding!!.appBar.ivBack.setOnClickListener{
            finish()
        }
        binding!!.appBar.btnAddPolicy.visibility = View.VISIBLE
        binding!!.appBar.btnAddPolicy.setOnClickListener{
            launchActivity<AddPolicyFormActivity> {  }
        }
        searchPolicyAdapter = SearchPolicyAdapter(this)
        binding!!.rvPolicies.adapter = searchPolicyAdapter
    }
}