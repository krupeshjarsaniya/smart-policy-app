package com.example.policyagent.ui.activities.agent

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.requests.editclient.EditClient
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.ImageModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.statelist.StateData
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.databinding.ActivityEditClientBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.EditDocumentAdapter
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddClientListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.EditClientViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*

class EditClientActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddClientListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityEditClientBinding? = null
    private var viewModel: EditClientViewModel? = null
    private var familyList = ArrayList<MemberModel>()
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<ImageModel>()
    private var memberAdapter: MemberAdapter? = null
    private var documentAdapter: EditDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var gson = Gson()
    var addClient: EditClient? = EditClient()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var stateList: ArrayList<String>? = ArrayList()
    var states: ArrayList<StateData?>? = ArrayList()
    var policy: ClientData? = ClientData()
    var familyAdapter: ArrayAdapter<String>? = null
    var removeFamily: ArrayList<String>? = ArrayList()
    var removeDocument: ArrayList<String>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_client)
        viewModel = ViewModelProvider(this, factory)[EditClientViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getState(this)
        binding!!.appBar.tvTitle.text = resources.getString(R.string.client)
        memberAdapter = MemberAdapter(this, this)
        documentAdapter = EditDocumentAdapter(this, this,this)
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

        if (intent.hasExtra(AppConstants.CLIENT)) {
            policy = intent.getSerializableExtra(AppConstants.CLIENT) as ClientData
            Log.e("policyjson",gson.toJson(policy))
        }

        binding!!.etFirstName.setText(policy!!.firstname)

        binding!!.etMiddleName.setText(policy!!.middlename)

        binding!!.etLastName.setText(policy!!.lastname)

        binding!!.etMobile.setText(policy!!.mobile)

        binding!!.etEmail.setText(policy!!.email)

        binding!!.etCity.setText(policy!!.city)

        if(policy!!.gender!!.isNotEmpty()) {
            val selectedGender: String = policy!!.gender!!.substring(0, 1).toUpperCase() + policy!!.gender!!.substring(1).toLowerCase()
            val genderPosition: Int = genderAdapter.getPosition(selectedGender)
            binding!!.spGender.setSelection(genderPosition)
        }

        binding!!.tvBirthDate.setText(policy!!.birthdate)

        binding!!.etAge.setText(policy!!.age)

        binding!!.etHeight.setText(policy!!.height)

        binding!!.etWeight.setText(policy!!.weight)

        binding!!.etPassword.setText(policy!!.password)

        if(policy!!.marital_status!!.isNotEmpty()) {
            val selectedMaritalStatus: String = policy!!.marital_status!!.substring(0, 1)
                .toUpperCase() + policy!!.marital_status!!.substring(1).toLowerCase()
            val maritalStatusPosition: Int = genderAdapter.getPosition(selectedMaritalStatus)
            binding!!.spMaritalStatus.setSelection(maritalStatusPosition)
        }

        binding!!.tcPanNumber.setText(policy!!.pan_number)

        binding!!.etGstNumber.setText(policy!!.gst)

        binding!!.etAddress.setText(policy!!.address)

        for (i in 0 until policy!!.family_Details!!.size){
            var detail = policy!!.family_Details!![i]
            var family = MemberModel(
                detail!!.id.toString(),
                detail.firstname,
                detail.lastname,
                detail.birthdate,
                detail.gender,
                detail.height,
                detail.weight,
                detail.age,
                detail.relationship,
                detail.pan_number
            )
            familyList.add(family)
        }

        for (i in 0 until policy!!.client_Documents!!.size){
            var clientDoc = policy!!.client_Documents!![i]
            var doc = DocumentModel(
                clientDoc!!.document_type,
                "",
                clientDoc.id!!.toString()
            )
            var file = ImageModel(
                clientDoc.url,
                null
            )
            documentList.add(doc)
            fileList.add(file)
        }


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
                this@EditClientActivity,
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
                if (familyList[i].first_name == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Fname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].last_name == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Lname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].birth_date == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Birth date For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_height == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Height For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_weight == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Weight For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].f_age == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Age For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else if (familyList[i].pan_number == "" && familyList[i].family_id!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Please Add Pan No. For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    addData = false
                    break
                } else {
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
                if (fileList[i].url!!.isEmpty() && !fileList[i].file!!.isAbsolute) {
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
                fileList.add(ImageModel("", File("")))
                documentAdapter!!.updateList(documentList, fileList)
            }
        }


        binding!!.btnSave.setOnClickListener {
            var sendFiles: ArrayList<File>? = ArrayList()
            familyJson!!.clear()
            docJson!!.clear()
            var callApi: Int = 0
            for (i in 0 until familyList.size) {
                if (familyList[i].first_name == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Fname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].last_name == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Lname For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].birth_date == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Birth date For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_height == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Height For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_weight == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Weight For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].f_age == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Age For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else if (familyList[i].pan_number == "") {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add Pan No. For Member ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else {
                    familyJson!!.add(gson.toJson(familyList[i]))
                }
            }
            for (i in 0 until documentList.size) {
                if (fileList[i].url!!.isEmpty() && !fileList[i].file!!.isAbsolute) {
                    callApi -= 1
                    Toast.makeText(
                        this,
                        "Please Add File For ${i + 1}",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else {
                    if(fileList[i].url!!.isEmpty()) {
                        sendFiles!!.add(fileList[i].file!!)
                        docJson!!.add(gson.toJson(documentList[i]))
                    }
                }
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
                addClient!!.password = binding!!.etPassword.editableText.toString()



            val strF = StringBuilder("")
            for (eachstring in removeFamily!!) {
                strF.append(eachstring).append(",")
            }

            var commaseparatedlistF = strF.toString()
            if (commaseparatedlistF.length > 0) commaseparatedlistF = commaseparatedlistF.substring(
                0, commaseparatedlistF.length - 1
            )
            val strD = StringBuilder("")
            for (eachstring in removeDocument!!) {
                strD.append(eachstring).append(",")
            }

            var commaseparatedlistD = strD.toString()
            if (commaseparatedlistD.length > 0) commaseparatedlistD = commaseparatedlistD.substring(
                0, commaseparatedlistD.length - 1
            )
            var removeF = commaseparatedlistF
            var removeD = commaseparatedlistD
            addClient!!.familayRemove = removeF
            addClient!!.documentsRemoveDataArray = removeD
            addClient!!.family = familyJson.toString()
            addClient!!.document = docJson.toString()
            addClient!!.file = sendFiles!!
            Log.e("callApi",callApi.toString())
            if(callApi >= 4) {
                viewModel!!.editClient(addClient!!, policy!!.id!!.toString(),this)
            } else{
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
                fileList[pos].file = getFileFromURI(imageSelected!!, this)!!
                documentAdapter!!.updateList(documentList, fileList)
        } else {
            showToastMessage("Something went wrong")
        }
    }

    override fun onLoadImage(image: String, imageview: ImageView) {
        Glide.with(this).load(image).placeholder(getGlideProgress(this))
            .error(R.drawable.ic_warning).into(imageview)
    }

    override fun onLoadPdf(url: String) {
        loadPdf(this, url)
    }

    override fun onFileSelect(position: Int) {
        pos = position
        startActivityForResult(getFileChooserIntent(), 111)
    }

    override fun onremoveFile(position: Int) {
        if(documentList[position].hidden_id!!.isNotEmpty()) {
            removeDocument!!.add(documentList[position].hidden_id!!)
        }
        documentList.removeAt(position)
        fileList.removeAt(position)
        documentAdapter!!.updateList(documentList, fileList)
    }

    override fun onRemoveFamily(position: Int) {
        if(familyList[position].family_id!!.isNotEmpty()) {
            removeFamily!!.add(familyList[position].family_id!!)
        }
        Log.e("position",position.toString())
        familyList.removeAt(position)
        memberAdapter!!.updateList(familyList)
    }

    override fun onSuccessState(state: StateListResponse) {
        hideProgress()
        states = state.data!!
        for (i in 0 until states!!.size) {
            stateList!!.add(states!![i]!!.name!!)
        }
        val stateAdapter = ArrayAdapter(this, R.layout.dropdown_item, stateList!!)
        binding!!.spState.adapter = stateAdapter


        val statePosition: Int = stateAdapter.getPosition(policy!!.state)
        binding!!.spState.setSelection(statePosition)
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