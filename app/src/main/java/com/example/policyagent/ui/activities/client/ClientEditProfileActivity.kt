package com.example.policyagent.ui.activities.client

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.R
import com.example.policyagent.data.requests.editClientProfile.EditClientProfile
import com.example.policyagent.data.requests.editclient.EditClient
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.data.responses.statelist.StateData
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.databinding.ActivityClientEditProfileBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.EditClientProfileListener
import com.example.policyagent.ui.viewmodels.client.ClientEditProfileViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.isValidMail
import com.example.policyagent.util.isValidMobile
import com.example.policyagent.util.launchLoginActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class ClientEditProfileActivity : BaseActivity(), KodeinAware, EditClientProfileListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityClientEditProfileBinding? = null
    private var viewModel: ClientEditProfileViewModel? = null
    var addClient: EditClientProfile? = EditClientProfile()
    var stateList: ArrayList<String>? = ArrayList()
    var states: ArrayList<StateData?>? = ArrayList()
    var selectedState: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_edit_profile)
        viewModel = ViewModelProvider(this, factory)[ClientEditProfileViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getState(this)
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        binding!!.spState.isEnabled = false
        binding!!.spGender.isEnabled = false
        binding!!.spMaritalStatus.isEnabled = false

        val maritalStatus = resources.getStringArray(R.array.marital_status_list)
        val maritalStatusAdapter = ArrayAdapter(this, R.layout.dropdown_item, maritalStatus)
        binding!!.spMaritalStatus.adapter = maritalStatusAdapter

        val gender = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding!!.spGender.adapter = genderAdapter
        viewModel!!.getLoggedInUser().observe(this, Observer { user ->
            if (user != null) {
                binding!!.etFirstName.setText(user.firstname)
                binding!!.etMiddleName.setText(user.middlename)
                binding!!.etLastName.setText(user.lastname)
                binding!!.etEmail.setText(user.email)
                binding!!.etMobile.setText(user.mobile)
                binding!!.etAddress.setText(user.address)
                binding!!.etCity.setText(user.city)
                binding!!.etAge.setText(user.age)
                binding!!.tvBirthDate.setText(user.birthdate)
                binding!!.etGstNumber.setText(user.gst)
                binding!!.etPanNumber.setText(user.pan_number)
                selectedState = user.state

                if(user.gender!!.isNotEmpty()) {
                    val selectedGender: String = user.gender.substring(0, 1).toUpperCase() + user.gender.substring(1).toLowerCase()
                    val genderPosition: Int = genderAdapter.getPosition(selectedGender)
                    binding!!.spGender.setSelection(genderPosition)
                }

                if(user.marital_status!!.isNotEmpty()) {
                    val selectedMaritalStatus: String = user.marital_status.substring(0, 1)
                        .toUpperCase() + user.marital_status!!.substring(1).toLowerCase()
                    Log.e("selectedmaritalstatus",selectedMaritalStatus)
                    val maritalStatusPosition: Int = genderAdapter.getPosition(selectedMaritalStatus)
                    binding!!.spMaritalStatus.setSelection(maritalStatusPosition)
                }


            }
        })

        binding!!.spState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addClient!!.state = states!![position]!!.id!!.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spMaritalStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addClient!!.marital_status = binding!!.spMaritalStatus.selectedItem.toString().toUpperCase()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding!!.spGender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addClient!!.gender = binding!!.spGender.selectedItem.toString().toUpperCase()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        /*binding!!.tvBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@ClientEditProfileActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvBirthDate.setText(date)
                },
                yy,
                mm,
                dd
            )
            binding!!.tvBirthDate.setTextColor(resources.getColor(R.color.black))
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }*/

        binding!!.btnSaveProfile.setOnClickListener {

            var callApi: Int = 0
            if (binding!!.etFirstName.editableText.toString().isNotEmpty()) {
                callApi+=1
                addClient!!.firstname = binding!!.etFirstName.editableText.toString()
            } else {
                callApi-=1
                binding!!.etFirstName.error = resources.getString(R.string.invalid_first_name)
            }
            if (binding!!.etLastName.editableText.toString().isNotEmpty()) {
                callApi+=1
                addClient!!.lastname = binding!!.etLastName.text.toString()
            } else {
                callApi-=1
                binding!!.etLastName.error = resources.getString(R.string.invalid_last_name)
            }
            if (isValidMobile(binding!!.etMobile.editableText.toString())) {
                callApi+=1
                addClient!!.mobile = binding!!.etMobile.text.toString()
            } else {
                callApi-=1
                binding!!.etMobile.error = resources.getString(R.string.invalid_mobile)
            }
            if (isValidMail(binding!!.etEmail.editableText.toString())) {
                callApi+=1
                addClient!!.email = binding!!.etEmail.editableText.toString()
            } else {
                callApi-=1
                binding!!.etEmail.error = resources.getString(R.string.invalid_email)
            }
            addClient!!.middlename = binding!!.etMiddleName.text.toString()
            addClient!!.address = binding!!.etAddress.editableText.toString()
            addClient!!.city = binding!!.etCity.editableText.toString()
            addClient!!.birthdate = binding!!.tvBirthDate.editableText.toString()
            addClient!!.age = binding!!.etAge.editableText.toString()
            addClient!!.c_pan_number = binding!!.etPanNumber.editableText.toString()
            addClient!!.gst_number = binding!!.etGstNumber.editableText.toString()


            Log.e("callApi",callApi.toString())
            if(callApi >= 4) {
                viewModel!!.editClientProfile(addClient!!,this)
            } else{
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }

//        binding!!.etUsername.onFocusChangeListener = this
//        binding!!.etEmail.onFocusChangeListener = this
//        binding!!.etMobile.onFocusChangeListener = this
    }

    override fun onSuccessState(state: StateListResponse) {
        hideProgress()
        states = state.data!!
        for (i in 0 until states!!.size) {
            stateList!!.add(states!![i]!!.name!!)
        }
        val stateAdapter = ArrayAdapter(this, R.layout.dropdown_item, stateList!!)
        binding!!.spState.adapter = stateAdapter


        val statePosition: Int = stateAdapter.getPosition(selectedState)
        binding!!.spState.setSelection(statePosition)
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: LoginResponse) {
        hideProgress()
        showToastMessage(data.message!!)
        finish()
    }


    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }


    override fun onError(errors: java.util.HashMap<String, Any>) {
        hideProgress()
        if(errors.containsKey("mobile")){
            binding!!.etMobile.error = errors["mobile"].toString()
        }
        if(errors.containsKey("email")){
            binding!!.etEmail.error = errors["email"].toString()
        }
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
        if(message.contains("Unauthenticated")){
            launchLoginActivity<LoginActivity> {  }
        }
    }

    /*override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(hasFocus) {
            when (v) {
                binding!!.etUsername -> {
                    binding!!.ivUsename.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etEmail -> {
                    binding!!.ivEmail.setColorFilter(resources.getColor(R.color.primary_color))
                }
                binding!!.etMobile -> {
                    binding!!.ivMobile.setColorFilter(resources.getColor(R.color.primary_color))
                }
            }
        } else{
            when (v) {
                binding!!.etUsername -> {
                    binding!!.ivUsename.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etEmail -> {
                    binding!!.ivEmail.setColorFilter(resources.getColor(R.color.grey))
                }
                binding!!.etMobile -> {
                    binding!!.ivMobile.setColorFilter(resources.getColor(R.color.grey))
                }
            }
        }
    }*/
}