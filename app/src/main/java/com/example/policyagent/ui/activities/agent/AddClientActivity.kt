package com.example.policyagent.ui.activities.agent

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.policyagent.R
import com.example.policyagent.data.requests.addclient.AddClient
import com.example.policyagent.data.requests.addlifeinsurance.AddLifeInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.statelist.StateData
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.databinding.ActivityAddClientBinding
import com.example.policyagent.databinding.ActivityAddLifeInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddClientListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddClientViewModel
import com.example.policyagent.ui.viewmodels.agent.AddLifeInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddClientActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddClientListener{
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddClientBinding? = null
    private var viewModel: AddClientViewModel? = null
    private var familyList = ArrayList<MemberModel>()
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<File>()
    private var memberAdapter: MemberAdapter? = null
    private var documentAdapter: UploadDocumentAdapter? = null
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var gson = Gson()
    var addClient: AddClient? = AddClient()
    var stateList: ArrayList<String>? = ArrayList()
    var states: ArrayList<StateData?>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_client)
        viewModel = ViewModelProvider(this, factory)[AddClientViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getState(this)

        binding!!.appBar.tvTitle.text = resources.getString(R.string.client)
        memberAdapter = MemberAdapter(this,this)
        documentAdapter = UploadDocumentAdapter(this, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding!!.rvFamily.layoutManager = layoutManager
        binding!!.rvFamily.adapter = memberAdapter
        binding!!.rvDocument.adapter = documentAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }

        val maritalStatus = resources.getStringArray(R.array.marital_status_list)
        val maritalStatusAdapter = ArrayAdapter(this, R.layout.dropdown_item, maritalStatus)
        binding!!.spMaritalStatus.adapter = maritalStatusAdapter

        val gender = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding!!.spGender.adapter = genderAdapter


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


        binding!!.tvBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@AddClientActivity,
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
        }

        //familyList.add(MemberModel())
        memberAdapter!!.updateList(familyList)

        //documentList.add(DocumentModel())
        //fileList.add(File(""))
        documentAdapter!!.updateList(documentList, fileList)

        binding!!.tvAddMember.setOnClickListener {
            var addData: Boolean? = true
            for (i in 0 until familyList.size) {
                if (familyList[i].first_name == "") {
                    Toast.makeText(
                        this,
                        "Please Add Fname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].last_name == "") {
                    Toast.makeText(
                        this,
                        "Please Add Lname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].birth_date == "") {
                    Toast.makeText(
                        this,
                        "Please Add Birth date For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_height == "") {
                    Toast.makeText(
                        this,
                        "Please Add Height For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_weight == "") {
                    Toast.makeText(
                        this,
                        "Please Add Weight For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_age == "") {
                    Toast.makeText(
                        this,
                        "Please Add Age For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].pan_number == "") {
                    Toast.makeText(
                        this,
                        "Please Add Pan No. For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                }
                else {
                    addData = true
                }
            }
            if (addData!!) {
                //binding!!.rvViewFamily.visibility = View.GONE
                familyList.add(MemberModel())
                memberAdapter!!.updateList(familyList)
            }
        }

        binding!!.tvAddDocument.setOnClickListener {
            var addData: Boolean? = true
            for (i in 0 until documentList.size) {
                if (!fileList[i].isAbsolute) {
                    Toast.makeText(
                        this,
                        "Please Add File For ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else {
                    addData = true
                }
            }
            if (addData!!) {
                documentList.add(DocumentModel())
                fileList.add(File(""))
                documentAdapter!!.updateList(documentList, fileList)
            }
        }

        binding!!.btnSave.setOnClickListener {
            familyJson!!.clear()
            docJson!!.clear()
            var callApi: Int = 0
            for (i in 0 until familyList.size) {
                if (familyList[i].first_name == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Fname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].last_name == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Lname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].birth_date == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Birth date For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_height == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Height For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_weight == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Weight For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_age == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Age For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].pan_number == "") {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add Pan No. For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                }
                else {
                }
            }
            for (i in 0 until documentList.size) {
                if (!fileList[i].isAbsolute) {
                    callApi-=1
                    Toast.makeText(
                        this,
                        "Please Add File For ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else {
                }
            }
            for (family in familyList) {
                familyJson!!.add(gson.toJson(family))
            }
            for (doc in documentList) {
                docJson!!.add(gson.toJson(doc))
            }
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
            addClient!!.city = binding!!.etCity.editableText.toString()
            addClient!!.birthdate = binding!!.tvBirthDate.editableText.toString()
            addClient!!.age = binding!!.etAge.editableText.toString()
            addClient!!.height = binding!!.etHeight.editableText.toString()
            addClient!!.weight = binding!!.etWeight.editableText.toString()
            addClient!!.c_pan_number = binding!!.tcPanNumber.editableText.toString()
            addClient!!.gst_number = binding!!.etGstNumber.editableText.toString()
            addClient!!.address = binding!!.etAddress.editableText.toString()


            addClient!!.family = familyJson.toString()
            addClient!!.document = docJson.toString()
            addClient!!.file = fileList
            Log.e("callApi",callApi.toString())
            if(callApi >= 4) {
                viewModel!!.addClient(addClient!!, this)
            } else{
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
                fileList[pos] = getFileFromURI(imageSelected!!, this)!!
                documentAdapter!!.updateList(documentList, fileList)
        } else {
            showToastMessage("Something went wrong")
        }
    }

    override fun onLoadImage(image: String, imageview: ImageView) {

    }

    override fun onLoadPdf(url: String) {

    }


    override fun onFileSelect(position: Int) {
        pos = position
        startActivityForResult(getFileChooserIntent(), 111)
    }

    override fun onremoveFile(position: Int) {
        documentList.removeAt(position)
        fileList.removeAt(position)
        documentAdapter!!.updateList(documentList, fileList)
    }

    override fun onStarted() {
        showProgress(true)
    }

    override fun onSuccess(data: CommonResponse) {
        hideProgress()
        showToastMessage(data.message!!)
        finish()
    }

    override fun onSuccessState(state: StateListResponse) {
        hideProgress()
        states = state.data!!
        for (i in 0 until state.data.size) {
            stateList!!.add(state.data[i]!!.name!!)
        }
        val stateAdapter = ArrayAdapter(this, R.layout.dropdown_item, stateList!!)
        binding!!.spState.adapter = stateAdapter
    }

    override fun onFailure(message: String) {
        hideProgress()
        showToastMessage(message)
    }

    override fun onRemoveFamily(position: Int) {
        familyList.removeAt(position)
        memberAdapter!!.updateList(familyList)
    }

    override fun onError(errors: HashMap<String, Any>) {
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
}