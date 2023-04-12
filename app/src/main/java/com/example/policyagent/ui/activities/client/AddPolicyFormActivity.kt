package com.example.policyagent.ui.activities.client

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.requests.addpolicy.AddPolicy
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.memberlist.MemberData
import com.example.policyagent.data.responses.memberlist.MemberListResponse
import com.example.policyagent.databinding.ActivityAddPolicyFormBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddPolicyListener
import com.example.policyagent.ui.viewmodels.client.PolicyFormViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.getFileChooserIntent
import com.example.policyagent.util.getFileFromURI
import com.example.policyagent.util.launchLoginActivity
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.HashMap

class AddPolicyFormActivity : BaseActivity(), KodeinAware, AddPolicyListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddPolicyFormBinding? = null
    private var viewModel: PolicyFormViewModel? = null
    var gson = Gson()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<MemberData?>? = ArrayList()
    var addPolicy: AddPolicy? = AddPolicy()
    private val FILEREQUEST = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_policy_form)
        viewModel = ViewModelProvider(this, factory)[PolicyFormViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.add_policy)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        val insuranceType = resources.getStringArray(R.array.policy_type)
        val insuranceTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, insuranceType)
        binding!!.spInsuranceType.adapter = insuranceTypeAdapter

        binding!!.spInsuranceType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addPolicy!!.model_type =
                    binding!!.spInsuranceType.selectedItem.toString().toUpperCase()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val clientJson: String = viewModel!!.getPreference().getStringValue(AppConstants.MEMBERS)!!
        val clientObj: MemberListResponse =
            gson.fromJson(clientJson, MemberListResponse::class.java)
        clients = clientObj.data
        for (i in 0 until clients!!.size) {
            clientList!!.add(clients!![i]!!.firstname!!)
        }
        val clientAdapter = ArrayAdapter(this, R.layout.dropdown_item, clientList!!)
        binding!!.spMemberName.adapter = clientAdapter

        binding!!.spMemberName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addPolicy!!.member_id = clients!![position]!!.id!!.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.ivPolicyFile.setOnClickListener {
            startActivityForResult(getFileChooserIntent(), FILEREQUEST)
        }

        binding!!.tvStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@AddPolicyFormActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvStartDate.setText(date)
                },
                yy,
                mm,
                dd
            )
            binding!!.tvStartDate.setTextColor(resources.getColor(R.color.black))
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }

        binding!!.tvEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@AddPolicyFormActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvEndDate.setText(date)
                },
                yy,
                mm,
                dd
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            binding!!.tvEndDate.setTextColor(resources.getColor(R.color.black))
            datePicker.show()
        }

        //binding!!.etInsuranceType.onFocusChangeListener = this
        //binding!!.etUserName.onFocusChangeListener = this
        binding!!.etPolicyNumber.onFocusChangeListener = this
        binding!!.etCompanyName.onFocusChangeListener = this
        binding!!.etPlanName.onFocusChangeListener = this
        binding!!.etSa.onFocusChangeListener = this
        binding!!.etPremium.onFocusChangeListener = this

        binding!!.btnAddPolicy.setOnClickListener {
            var callApi: Int = 0

            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_policy_number)
            }
            if (binding!!.etCompanyName.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.company_name = binding!!.etCompanyName.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etCompanyName.error = resources.getString(R.string.invalid_company_name)
            }
            if (binding!!.etPlanName.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.plan_name = binding!!.etPlanName.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etPlanName.error = resources.getString(R.string.invalid_plan_name)
            }
            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.start_date = binding!!.tvStartDate.text.toString()
            } else {
                callApi -= 1
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_start_date)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.end_date = binding!!.tvEndDate.text.toString()
            } else {
                callApi -= 1
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_end_date)
            }
            if (binding!!.etSa.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.sa = binding!!.etSa.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etSa.error = resources.getString(R.string.invalid_sa)
            }
            if (binding!!.etPremium.editableText.toString().isNotEmpty()) {
                callApi += 1
                addPolicy!!.premium =
                    binding!!.etPremium.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etPremium.error = resources.getString(R.string.invalid_premium_amount)
            }
            if (callApi >= 7) {
                viewModel!!.addPolicy(addPolicy!!, this)
            } else {
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
            if (requestCode == FILEREQUEST) {
                addPolicy!!.document = getFileFromURI(imageSelected!!, this)
                if (addPolicy!!.document!!.path.contains("pdf")) {
                    binding!!.ivPolicyFile.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                } else {
                    binding!!.ivPolicyFile.setImageURI(imageSelected)
                }
            }
        } else {
            showToastMessage("Something went wrong")
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        super.onFocusChange(v, hasFocus)
        if (hasFocus)
            when (v) {
                /* binding!!.etInsuranceType -> {
                     binding!!.ivInsuranceType.setColorFilter(resources.getColor(R.color.primary_color))
                 }
                 binding!!.etUserName -> {
                     binding!!.ivUserName.setColorFilter(resources.getColor(R.color.primary_color))
                 }*/
                binding!!.etPolicyNumber -> {
                    binding!!.ivPolicyNumber.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etCompanyName -> {
                    binding!!.ivCompanyName.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etPlanName -> {
                    binding!!.ivProduct.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etSa -> {
                    binding!!.ivSa.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etPremium -> {
                    binding!!.ivPremium.setColorFilter(resources.getColor(R.color.primary_color))
                }
            }
        else
            when (v) {
                /*binding!!.etInsuranceType -> {
                    binding!!.ivInsuranceType.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etUserName -> {
                    binding!!.ivUserName.setColorFilter(resources.getColor(R.color.grey))
                }*/
                binding!!.etPolicyNumber -> {
                    binding!!.ivPolicyNumber.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etCompanyName -> {
                    binding!!.ivCompanyName.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etPlanName -> {
                    binding!!.ivProduct.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etSa -> {
                    binding!!.ivSa.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etPremium -> {
                    binding!!.ivPremium.setColorFilter(resources.getColor(R.color.grey))
                }
            }
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: CommonResponse) {
        hideProgress()
        showToastMessage(data.message!!)
        finish()
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
        if(message.contains("Unauthenticated")){
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
            launchLoginActivity<LoginActivity> {  }
        }
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
        if(errors.containsKey("policy_number")){
            binding!!.etPolicyNumber.error = errors["policy_number"].toString()
        }
    }

    override fun onLogout(message: String) {

    }
}