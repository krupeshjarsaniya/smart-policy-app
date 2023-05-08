package com.example.policyagent.ui.activities.agent

import android.app.DatePickerDialog
import android.content.Intent
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
import com.example.policyagent.data.requests.addhealthinsurance.AddHealthInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.gst.GstResponse
import com.example.policyagent.databinding.ActivityAddHealthInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddHealthInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddHealthInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddHealthInsuranceListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddHealthInsuranceBinding? = null
    private var viewModel: AddHealthInsuranceViewModel? = null
    private var familyList = ArrayList<MemberModel>()
    private var familyIdList = ArrayList<String>()
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<File>()
    private var memberAdapter: MemberAdapter? = null
    private var documentAdapter: UploadDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var gson = Gson()
    var addHealthInsurance: AddHealthInsurance? = AddHealthInsurance()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var newMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()
    var selectedClient: ClientData? = null
    var currentDate = Calendar.getInstance().time
    var aYearAfter = Calendar.getInstance()
    var df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var selectedPaymentPosition : Int = 1

    var selectedStartDate: Date? = null
    var selectedEndDate: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_health_insurance)
        viewModel = ViewModelProvider(this, factory)[AddHealthInsuranceViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getClients(this)


        var formattedDate = df.format(currentDate)

        aYearAfter.add(Calendar.YEAR, 1)
        var yearFormattedDate = df.format(aYearAfter.time)

        selectedStartDate = currentDate
        selectedEndDate = aYearAfter.time

        binding!!.tvStartDate.setText(formattedDate)
        binding!!.tvEndDate.setText(yearFormattedDate)

        binding!!.appBar.tvTitle.text = resources.getString(R.string.health_insurance)
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

        val paymentMode = resources.getStringArray(R.array.payment_mode)
        val paymentAdapter = ArrayAdapter(this, R.layout.dropdown_item, paymentMode)
        binding!!.spPaymentMode.adapter = paymentAdapter

        binding!!.spClientName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addHealthInsurance!!.client_id = clients!![position]!!.id!!.toString()
                familyMemberList!!.clear();
                newMemberList!!.clear();
                families = clients!![position]!!.family_Details
                selectedClient = clients!![position]!!
                familyMemberList!!.add("Self")
                newMemberList!!.add("Select")
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                    newMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                }
                val familyAdapter = ArrayAdapter(
                    this@AddHealthInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
                binding!!.spFamilyMember.adapter = familyAdapter
                val newAdapter = ArrayAdapter(
                    this@AddHealthInsuranceActivity,
                    R.layout.dropdown_item,
                    newMemberList!!
                )
                binding!!.spNewMember.adapter = newAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spFamilyMember.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var member = MemberModel()
                if(position != 0) {
                    addHealthInsurance!!.member_id = families!![position - 1]!!.id!!.toString()
                    member = MemberModel(
                            families!![position - 1]!!.id.toString(),
                            families!![position - 1]!!.firstname,
                            families!![position - 1]!!.lastname,
                            families!![position - 1]!!.birthdate,
                            families!![position - 1]!!.gender,
                            families!![position - 1]!!.height,
                            families!![position - 1]!!.weight,
                            families!![position - 1]!!.age,
                            families!![position - 1]!!.relationship,
                            families!![position - 1]!!.pan_number,
                        )
                } else {
                    member = MemberModel(
                            selectedClient!!.id.toString(),
                            selectedClient!!.firstname,
                            selectedClient!!.lastname,
                            selectedClient!!.birthdate,
                            selectedClient!!.gender,
                            selectedClient!!.height,
                            selectedClient!!.weight,
                            selectedClient!!.age,
                            selectedClient!!.relationship,
                            selectedClient!!.pan_number,
                    )
                    addHealthInsurance!!.member_id = ""
                }
                if(addHealthInsurance!!.insurance_type != "SINGLE") {
                    if (!familyIdList.contains(member.family_id!!)) {
                        familyList.add(member)
                        familyIdList.add(member.family_id!!)
                        updateMember()
                    }
                } else{
                    familyList.clear()
                    familyIdList.clear()
                    familyList.add(member)
                    familyIdList.add(member.family_id!!)
                    updateMember()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spNewMember.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0) {
                    if (addHealthInsurance!!.insurance_type != "SINGLE") {
                        var member = MemberModel(
                            families!![position - 1]!!.id.toString(),
                            families!![position - 1]!!.firstname,
                            families!![position - 1]!!.lastname,
                            families!![position - 1]!!.birthdate,
                            families!![position - 1]!!.gender,
                            families!![position - 1]!!.height,
                            families!![position - 1]!!.weight,
                            families!![position - 1]!!.age,
                            families!![position - 1]!!.relationship,
                            families!![position - 1]!!.pan_number,
                        )
                        if (!familyIdList.contains(member.family_id!!)) {
                            familyList.add(member)
                            familyIdList.add(member.family_id!!)
                            updateMember()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spCompanyName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addHealthInsurance!!.company_id = companies!![position]!!.id!!.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spPaymentMode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addHealthInsurance!!.payment_mode = binding!!.spPaymentMode.selectedItem.toString()
                selectedPaymentPosition = position
                //calculatePreimium()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val insuranceType = resources.getStringArray(R.array.health_insurance_type)
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
                addHealthInsurance!!.insurance_type = binding!!.spInsuranceType.selectedItem.toString().toUpperCase()
                Log.e("insuranceType",addHealthInsurance!!.insurance_type.toString())
                if(addHealthInsurance!!.insurance_type != "SINGLE") {
                    binding!!.spNewMember.show()
                } else{
                    familyList.clear()
                    familyIdList.clear()
                    var member: MemberModel = MemberModel()
                    binding!!.spNewMember.show()
                    if(addHealthInsurance!!.member_id != "") {
                        member = MemberModel(
                            families!![position - 1]!!.id.toString(),
                            families!![position - 1]!!.firstname,
                            families!![position - 1]!!.lastname,
                            families!![position - 1]!!.birthdate,
                            families!![position - 1]!!.gender,
                            families!![position - 1]!!.height,
                            families!![position - 1]!!.weight,
                            families!![position - 1]!!.age,
                            families!![position - 1]!!.relationship,
                            families!![position - 1]!!.pan_number,
                        )
                    } else {
                        if (selectedClient != null) {
                            member = MemberModel(
                                selectedClient!!.id.toString(),
                                selectedClient!!.firstname,
                                selectedClient!!.lastname,
                                selectedClient!!.birthdate,
                                selectedClient!!.gender,
                                selectedClient!!.height,
                                selectedClient!!.weight,
                                selectedClient!!.age,
                                selectedClient!!.relationship,
                                selectedClient!!.pan_number,
                            )
                        }
                        familyList.add(member)
                        familyIdList.add(member.family_id!!)
                        updateMember()
                        binding!!.spNewMember.hide()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding!!.tvStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@AddHealthInsuranceActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvStartDate.setText(date)
                    calendar.set(year,monthOfYear,dayOfMonth)
                    selectedStartDate = calendar.time
                    var yearDiff = dateDifference(selectedStartDate!!,selectedEndDate!!)
                    binding!!.etPolicyTerm.setText(yearDiff.toString())
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
                this@AddHealthInsuranceActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvEndDate.setText(date)
                    calendar.set(year,monthOfYear,dayOfMonth)
                    selectedEndDate = calendar.time
                    var yearDiff = dateDifference(selectedStartDate!!,selectedEndDate!!)
                    binding!!.etPolicyTerm.setText(yearDiff.toString())
                },
                yy,
                mm,
                dd
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            binding!!.tvEndDate.setTextColor(resources.getColor(R.color.black))
            datePicker.show()
        }

        binding!!.ivPolicyFile.setOnClickListener {
            startActivityForResult(getFileChooserIntent(), FILEREQUEST)
        }
        //familyList.add(MemberModel())
        updateMember()

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
                if(addHealthInsurance!!.insurance_type == "SINGLE" && familyList.size == 1){

                } else {
                    familyList.add(MemberModel())
                    familyIdList.add("")
                    updateMember()
                }
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

        binding!!.etCommission.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etNetPremium.editableText.toString().isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()) {
                    var commission = binding!!.etNetPremium.editableText.toString()
                        .toDouble() * binding!!.etCommission.editableText.toString()
                        .toDouble() / 100
                    binding!!.etViewCommision.setText(String.format("%.2f",commission))
                } else{
                    binding!!.etViewCommision.setText("0.00")
                }
            }
        })

//        binding!!.etPolicyTerm.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if(binding!!.etPolicyTerm.editableText.toString() == "1"){
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_one)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                } else if(binding!!.etPolicyTerm.editableText.toString() == "2"){
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_two)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                }
//                else if(binding!!.etPolicyTerm.editableText.toString() == "3"){
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_three)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                }
//                else if(binding!!.etPolicyTerm.editableText.toString() == "4"){
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_four)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                }
//                else if(binding!!.etPolicyTerm.editableText.toString() == "5"){
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_five)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                } else{
//                    val paymentMode = resources.getStringArray(R.array.payment_mode_five)
//                    val paymentAdapter = ArrayAdapter(this@AddHealthInsuranceActivity, R.layout.dropdown_item, paymentMode)
//                    binding!!.spPaymentMode.adapter = paymentAdapter
//                }
//            }
//        })


        binding!!.etPremiumAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                //calculatePreimium()
//                if(binding!!.etNetPremium.editableText.toString().isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()) {
//                    if(addHealthInsurance!!.payment_mode)
//                } else{
//
//                }
            }

        })

        binding!!.etNetPremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var netPremium: Int? = 0
                var totalPremium: Int? = 0
                var gst: Int? = 0
                var commission: Int? = 0
                var viewCommission: Double? = 0.0
                gst = if(binding!!.etGst.editableText.toString().isNotEmpty()){
                    addHealthInsurance!!.gst.toInt()
                } else{
                    0
                }
                netPremium = if(binding!!.etNetPremium.editableText.toString().isNotEmpty()){
                    binding!!.etNetPremium.editableText.toString().toInt()
                } else{
                    0
                }
                commission = if(binding!!.etCommission.editableText.toString().isNotEmpty()){
                    binding!!.etCommission.editableText.toString().toInt()
                } else{
                    0
                }
                viewCommission = netPremium
                    .toDouble() * commission
                    .toDouble() / 100
                binding!!.etViewCommision.setText(String.format("%.2f",viewCommission))
                totalPremium = netPremium + (netPremium * gst / 100)
                binding!!.etTotalNetPremium.setText(totalPremium.toString())
            }
        })
        
        binding!!.etSumInsured.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                
            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etSumInsured.editableText.toString().isNotEmpty() && binding!!.etBonus.editableText.toString().isNotEmpty()) {
                    var total = binding!!.etSumInsured.editableText.toString().toDouble() + binding!!.etBonus.editableText.toString().toDouble()
                    binding!!.etTotalSumInsured.setText(String.format("%.2f",total))
                } else{
                    binding!!.etTotalSumInsured.setText("0.00")
                }
            }
        })
        
        binding!!.etBonus.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                
            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etSumInsured.editableText.toString().isNotEmpty() && binding!!.etBonus.editableText.toString().isNotEmpty()) {
                    var total = binding!!.etSumInsured.editableText.toString().toDouble() + binding!!.etBonus.editableText.toString().toDouble()
                    binding!!.etTotalSumInsured.setText(String.format("%.2f",total))
                } else{
                    binding!!.etTotalSumInsured.setText("0.00")
                }
            }

        })

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
//            if (binding!!.etSt.editableText.toString().isNotEmpty()) {
//                callApi+=1
//                addHealthInsurance!!.st = binding!!.etSt.editableText.toString()
//            } else {
//                callApi-=1
//                binding!!.etSt.error = resources.getString(R.string.invalid_st)
//            }
            /*if (binding!!.etPed.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.pre_existing_decease = binding!!.etPed.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPed.error = resources.getString(R.string.invalid_pre_existing_decease)
            }*/
            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.risk_start_date = binding!!.tvStartDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_risk_start_date)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.risk_end_date = binding!!.tvEndDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_risk_end_date)
            }
            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_policy_number)
            }
            /*if (binding!!.etPlanName.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPlanName.error = resources.getString(R.string.invalid_plan_name)
            }*/
            if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPremiumAmount.error = resources.getString(R.string.invalid_premium_amount)
            }
            if (binding!!.etNetPremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.net_premium =
                    binding!!.etNetPremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etNetPremium.error = resources.getString(R.string.invalid_net_premium)
            }
            /*if (binding!!.etPolicyTerm.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.policy_term = binding!!.etPolicyTerm.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPolicyTerm.error = resources.getString(R.string.invalid_policy_term)
            }*/
            /*if (binding!!.etWaitingPeriod.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.waiting =
                    binding!!.etWaitingPeriod.editableText.toString()
            } else {
                callApi-=1
                binding!!.etWaitingPeriod.error = resources.getString(R.string.invalid_waiting_period)
            }*/
            /*if (binding!!.etSumInsured.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.sum_insured = binding!!.etSumInsured.editableText.toString()
            } else {
                callApi-=1
                binding!!.etSumInsured.error = resources.getString(R.string.invalid_sum_insured)
            }*/
            /*if (binding!!.etBonus.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.bonus = binding!!.etBonus.editableText.toString()
            } else {
                callApi-=1
                binding!!.etBonus.error = resources.getString(R.string.invalid_bonus)
            }*/
            /*if (binding!!.etTotalSumInsured.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.total_sum_insured = binding!!.etTotalSumInsured.editableText.toString()
            } else {
                callApi-=1
                binding!!.etTotalSumInsured.error = resources.getString(R.string.invalid_total_sum_insured)
            }*/
            if (binding!!.etCommission.editableText.toString().isNotEmpty()) {
                callApi+=1
                addHealthInsurance!!.commision =
                    binding!!.etCommission.editableText.toString()
            } else {
                callApi-=1
                binding!!.etCommission.error = resources.getString(R.string.invalid_commission)
            }
            addHealthInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            addHealthInsurance!!.policy_term = binding!!.etPolicyTerm.editableText.toString()
            addHealthInsurance!!.sum_insured = binding!!.etSumInsured.editableText.toString()
            addHealthInsurance!!.total_sum_insured = binding!!.etTotalSumInsured.editableText.toString()
            addHealthInsurance!!.pre_existing_decease = binding!!.etPed.editableText.toString()
            addHealthInsurance!!.waiting = binding!!.etWaitingPeriod.editableText.toString()
            addHealthInsurance!!.bonus = binding!!.etBonus.editableText.toString()
            addHealthInsurance!!.family = familyJson.toString()
            addHealthInsurance!!.document = docJson.toString()
            addHealthInsurance!!.file = fileList
            if(callApi >= 6) {
                viewModel!!.addHealthInsurance(addHealthInsurance!!, this)
            } else {
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    fun updateMember() {
        memberAdapter!!.updateList(familyList)
    }

    /*fun calculatePreimium() {
        var netPremium: Int? = 0
        var totalPremium: Int? = 0
        var premiumAmount: Int? = 0
        var gst: Int? = 0
        var years: Int? = 0

        premiumAmount = if(binding!!.etPremiumAmount.editableText.toString().isNotEmpty()){
            binding!!.etPremiumAmount.editableText.toString().toInt()
        } else{
            0
        }

        gst = if(binding!!.etGst.editableText.toString().isNotEmpty()){
            addHealthInsurance!!.gst.toInt()
        } else{
            0
        }

        years = if(binding!!.etPolicyTerm.editableText.toString().isNotEmpty()){
            binding!!.etPolicyTerm.editableText.toString().toInt()
        } else{
            0
        }
        Log.e("selectedposition",selectedPaymentPosition.toString())
        Log.e("premiumamount",premiumAmount.toString())

        when (selectedPaymentPosition) {
            0 -> {
                netPremium = premiumAmount * 1 * years
            }
            1 -> {
                netPremium = premiumAmount * 2 * years
            }
            2 -> {
                netPremium = premiumAmount * 4 * years
            }
            3 -> {
                netPremium = premiumAmount * 12 * years
            }
            4 -> {
                netPremium = premiumAmount
            }
            5 -> {
                netPremium = if(years >= 2){
                    premiumAmount
                } else{
                    premiumAmount * years
                }
            }
            6 -> {
                netPremium = if(years >= 3){
                    premiumAmount * 1
                } else{
                    premiumAmount * years
                }
            }
            7 -> {
                netPremium = if(years >= 4){
                    premiumAmount * 1
                } else{
                    premiumAmount * years
                }
            }
            8 -> {
                netPremium = if(years >= 5){
                    premiumAmount * 1
                } else{
                    premiumAmount * years
                }
            }
        }
        totalPremium = netPremium!! + (netPremium * gst / 100)
        Log.e("netpremium",netPremium.toString())
        binding!!.etNetPremium.setText(netPremium.toString())
        binding!!.etTotalNetPremium.setText(totalPremium.toString())
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
            if (requestCode == FILEREQUEST) {
                addHealthInsurance!!.policy_file = getFileFromURI(imageSelected!!, this)
                if(addHealthInsurance!!.policy_file!!.path.contains("pdf")){
                    binding!!.ivPolicyFile.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                } else {
                    binding!!.ivPolicyFile.setImageURI(imageSelected)
                }
            } else {
                fileList[pos] = getFileFromURI(imageSelected!!, this)!!
                documentAdapter!!.updateList(documentList, fileList)
            }
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

    override fun onRemoveFamily(position: Int) {
        //if(addHealthInsurance!!.insurance_type != "SINGLE") {
        if(position != 0) {
            familyList.removeAt(position)
            familyIdList.removeAt(position)
            updateMember()
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
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
        if(errors.containsKey("policy_number")){
            binding!!.etPolicyNumber.error = errors["policy_number"].toString()
        } else if(errors.containsKey("policy_file")){
            showToastMessage(errors["policy_file"].toString())
        } else if(errors.containsKey("file")){
            showToastMessage(errors["file"].toString())
        }
    }

    override fun onLogout(message: String) {
        hideProgress()
        viewModel!!.getPreference().setBooleanValue(AppConstants.IS_REMEMBER,false)
        if(message.contains("Unauthenticated")){
            launchLoginActivity<LoginActivity> {  }
        }
    }

    override fun onSuccessGst(gst: GstResponse) {
        if(gst.status!!) {
            addHealthInsurance!!.gst = gst.data!!.gst.toString()
            binding!!.etGst.setText("${gst.data!!.gst} %")
        } else{
            binding!!.etGst.setText("0%")
        }
        viewModel!!.getCompanies(this)
    }

    override fun onSuccessClient(client: ClientListResponse) {
        val gson = Gson()
        val json = gson.toJson(client)
        viewModel!!.getPreference().setStringValue(AppConstants.CLIENTS, json)
        AppConstants.clients = client.data!!
        viewModel!!.getGst(this)
    }

    override fun onSuccessCompany(company: CompanyListResponse) {
        hideProgress()
        val gson = Gson()
        val json = gson.toJson(company)
        viewModel!!.getPreference().setStringValue(AppConstants.COMPANIES, json)
        AppConstants.companies = company.data!!
        val clientJson: String = viewModel!!.getPreference().getStringValue(AppConstants.CLIENTS)!!
        val clientObj: ClientListResponse =
            gson.fromJson(clientJson, ClientListResponse::class.java)
        clients = clientObj.data
        resources.getStringArray(R.array.clients)
        for (i in 0 until clients!!.size) {
            clientList!!.add(clients!![i]!!.firstname!! + " "+ clients!![i]!!.lastname!!)
        }
        val clientAdapter = ArrayAdapter(this, R.layout.dropdown_item, clientList!!)
        binding!!.spClientName.setAdapter(clientAdapter)


        val companyJson: String =
            viewModel!!.getPreference().getStringValue(AppConstants.COMPANIES)!!
        val companyObj: CompanyListResponse =
            gson.fromJson(companyJson, CompanyListResponse::class.java)

        companies = companyObj.data//resources.getStringArray(R.array.companies)
        for (i in 0 until companies!!.size) {
            companyList!!.add(companies!![i]!!.name!!)
        }
        val companyAdapter = ArrayAdapter(this, R.layout.dropdown_item, companyList!!)
        binding!!.spCompanyName.adapter = companyAdapter
    }
}