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
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.requests.editlifeinsurance.EditLifeInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.ImageModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.gst.GstResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ActivityEditLifeInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.EditDocumentAdapter
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddLifeInsuranceListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.EditLifeInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditLifeInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddLifeInsuranceListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityEditLifeInsuranceBinding? = null
    private var viewModel: EditLifeInsuranceViewModel? = null
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
    var addLifeInsurance: EditLifeInsurance? = EditLifeInsurance()
    var familyMemberList: ArrayList<String>? = ArrayList()
    private var familyIdList = ArrayList<String>()
    var newMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()
    var policy: LifeInsuranceData? = LifeInsuranceData()
    var clientDetails: ClientPersonalDetails? = ClientPersonalDetails()
    var familyAdapter: ArrayAdapter<String>? = null
    var removeFamily: ArrayList<String>? = ArrayList()
    var removeDocument: ArrayList<String>? = ArrayList()
    var selectedClient: ClientData? = null
    var currentDate = Calendar.getInstance().time
    var aYearAfter = Calendar.getInstance()
    var df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var selectedStartDate: Date? = null
    var selectedEndDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_life_insurance)
        viewModel = ViewModelProvider(this, factory)[EditLifeInsuranceViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getClients(this)
        binding!!.appBar.tvTitle.text = resources.getString(R.string.life_insurance)
        memberAdapter = MemberAdapter(this, this)
        documentAdapter = EditDocumentAdapter(this, this,this)
        //familyList.add(MemberModel())


        var formattedDate = df.format(currentDate)

        aYearAfter.add(Calendar.YEAR, 1)
        var yearFormattedDate = df.format(aYearAfter.time)

        binding!!.tvStartDate.setText(formattedDate)
        binding!!.tvEndDate.setText(yearFormattedDate)

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

        if (intent.hasExtra(AppConstants.LIFE_INSURANCE)) {
            policy = intent.getSerializableExtra(AppConstants.LIFE_INSURANCE) as LifeInsuranceData
            clientDetails = policy!!.client_Personal_Details
            Log.e("policyjson",gson.toJson(policy))
        }



        binding!!.etPolicyNumber.setText(policy!!.policy_number)

        binding!!.spFamilyMember.adapter = familyAdapter

        binding!!.tvStartDate.setText(policy!!.psd)

        binding!!.tvEndDate.setText(policy!!.ped)

        selectedStartDate = df.parse(policy!!.psd!!)

        selectedEndDate = df.parse(policy!!.ped!!)

        binding!!.etPlanName.setText(policy!!.plan_name)

        val paymentPosition: Int = paymentAdapter.getPosition(policy!!.payment_mode)
        binding!!.spPaymentMode.setSelection(paymentPosition)

        binding!!.etPremiumAmount.setText(policy!!.premium_amount)

        binding!!.etNetPremium.setText(policy!!.net_premium)

        binding!!.etGst.setText(policy!!.gst+"%")

        addLifeInsurance!!.gst = policy!!.gst.toString()

        binding!!.etMaturityAmount.setText(policy!!.maturity_amount)

        binding!!.etPolicyTerm.setText(policy!!.policy_term)

        binding!!.etSumInsured.setText(policy!!.sum_insured)

        binding!!.etPremiumPaymentTerm.setText(policy!!.preminum_payment_term)



        binding!!.etYearlyBonusAmount.setText(policy!!.yearly_bonus_amount)

        binding!!.etNomineeDetails.setText(policy!!.nominee_details)

        binding!!.etAdditionalRider.setText(policy!!.additional_rider)

        binding!!.etMaturityTerm.setText(policy!!.maturity_term)

        binding!!.etPed.setText(policy!!.pre_exist_decease)

        binding!!.etCommission.setText(policy!!.commision)

        if(policy!!.commision!!.isNotEmpty() && policy!!.net_premium!!.isNotEmpty()){
            var commission = policy!!.commision!!.toDouble() * policy!!.net_premium!!.toDouble() / 100
            binding!!.etViewCommision.setText(String.format("%.2f",commission))
        }

        if(policy!!.gst!!.isNotEmpty() && policy!!.net_premium!!.isNotEmpty()){
            var commission = policy!!.net_premium!!.toDouble() + (policy!!.gst!!.toDouble() * policy!!.net_premium!!.toDouble() / 100)
            binding!!.etTotalNetPremium.setText(String.format("%.2f",commission))
        }


        for (i in 0 until policy!!.family!!.size){
            var detail = policy!!.family!![i]
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
            familyIdList.add(family.family_id!!)
        }

        for (i in 0 until policy!!.insurance_Documents!!.size){
            var clientDoc = policy!!.insurance_Documents!![i]
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

        if(policy!!.policy_file != null && policy!!.policy_file!!.isNotEmpty()){
            onLoadImage(policy!!.policy_file!!,binding!!.ivPolicyFile)
        }

        binding!!.spClientName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addLifeInsurance!!.client_id = clients!![position]!!.id!!.toString()
                familyMemberList!!.clear();
                newMemberList!!.clear();
                families = clients!![position]!!.family_Details
                selectedClient = clients!![position]!!
                familyMemberList!!.add("Self")
                newMemberList!!.add("Select")
                familyAdapter = ArrayAdapter(
                    this@EditLifeInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
                binding!!.spFamilyMember.adapter = familyAdapter
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                    newMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                    if (policy!!.member_name == families!![i]!!.firstname) {
                        val memberPosition: Int = familyAdapter!!.getPosition(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                        binding!!.spFamilyMember.setSelection(memberPosition)
                    }
                }
                val newAdapter = ArrayAdapter(
                    this@EditLifeInsuranceActivity,
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
                    addLifeInsurance!!.member_id = families!![position - 1]!!.id!!.toString()
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
                    addLifeInsurance!!.member_id = ""
                }
                if (!familyIdList.contains(member.family_id!!)) {
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
                addLifeInsurance!!.company_name = companies!![position]!!.id!!.toString()
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
                addLifeInsurance!!.payment_mode = binding!!.spPaymentMode.selectedItem.toString()
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
                this@EditLifeInsuranceActivity,
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
                this@EditLifeInsuranceActivity,
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
                familyIdList.add("")
                updateMember()
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
                fileList.add(ImageModel("",File("")))
                documentAdapter!!.updateList(documentList, fileList)
            }
        }

        binding!!.etCommission.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding!!.etNetPremium.editableText.toString()
                        .isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()
                ) {
                    var commission = binding!!.etNetPremium.editableText.toString()
                        .toDouble() * binding!!.etCommission.editableText.toString()
                        .toDouble() / 100
                    binding!!.etViewCommision.setText(String.format("%.2f", commission))
                } else {
                    binding!!.etViewCommision.setText("0.00")
                }
//                var commission = binding!!.etPremiumAmount.editableText.toString().toDouble() * binding!!.etCommission.editableText.toString().toDouble() / 100
//                binding!!.etViewCommision.setText(String.format("%.2f",commission))
            }

        })

        binding!!.etNetPremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var netPremium: Double? = 0.0
                var totalPremium: Double? = 0.0
                var gst: Double? = 0.0
                var commission: Double? = 0.0
                var viewCommission: Double? = 0.0
                gst = if(binding!!.etGst.editableText.toString().isNotEmpty()){
                    addLifeInsurance!!.gst!!.toDouble()
                } else{
                    0.0
                }
                netPremium = if(binding!!.etNetPremium.editableText.toString().isNotEmpty()){
                    binding!!.etNetPremium.editableText.toString().toDouble()
                } else{
                    0.0
                }
                commission = if(binding!!.etCommission.editableText.toString().isNotEmpty()){
                    binding!!.etCommission.editableText.toString().toDouble()
                } else{
                    0.0
                }
                viewCommission = netPremium
                    .toDouble() * commission
                    .toDouble() / 100
                binding!!.etViewCommision.setText(String.format("%.2f",viewCommission))
                totalPremium = netPremium + (netPremium * gst / 100)
                binding!!.etTotalNetPremium.setText(totalPremium.toString())
            }
        })

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
            /*if (binding!!.etSumInsured.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.sum_insured = binding!!.etSumInsured.editableText.toString()
            } else {
                callApi-=1
                binding!!.etSumInsured.error = resources.getString(R.string.invalid_sum_insured)
            }*/
            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.policy_start_date = binding!!.tvStartDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_policy_start_date)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.policy_end_date = binding!!.tvEndDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_policy_end_date)
            }
            /*if (binding!!.etPed.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.pre_existing_decease = binding!!.etPed.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPed.error = resources.getString(R.string.invalid_pre_existing_decease)
            }*/
            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_policy_number)
            }
            /*if (binding!!.etPlanName.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPlanName.error = resources.getString(R.string.invalid_plan_name)
            }*/
            if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()

            } else {
                callApi-=1
                binding!!.etPremiumAmount.error = resources.getString(R.string.invalid_premium_amount)
            }
            if (binding!!.etNetPremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.net_premium =
                    binding!!.etNetPremium.editableText.toString()

            } else {
                callApi-=1
                binding!!.etNetPremium.error = resources.getString(R.string.invalid_net_premium)
            }
            if (binding!!.etMaturityAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.maturity_amount =
                    binding!!.etMaturityAmount.editableText.toString()
            } else {
                callApi-=1
                binding!!.etMaturityAmount.error = resources.getString(R.string.invalid_maturity_amount)
            }
            if (binding!!.etPolicyTerm.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.policy_term = binding!!.etPolicyTerm.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPolicyTerm.error = resources.getString(R.string.invalid_policy_term)
            }
            if (binding!!.etPremiumPaymentTerm.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.preminum_payment_term =
                    binding!!.etPremiumPaymentTerm.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPremiumPaymentTerm.error = resources.getString(R.string.invalid_premium_payment_term)
            }
            /*if (binding!!.etMaturityTerm.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.maturity_term = binding!!.etMaturityTerm.editableText.toString()
            } else {
                callApi-=1
                binding!!.etMaturityTerm.error = resources.getString(R.string.invalid_maturity_term)
            }*/
            /*if (binding!!.etYearlyBonusAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.yearly_bonus_amount =
                    binding!!.etYearlyBonusAmount.editableText.toString()
            } else {
                callApi-=1
                binding!!.etYearlyBonusAmount.error = resources.getString(R.string.invalid_yearly_bonus_amount)
            }*/
            if (binding!!.etCommission.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.commision = binding!!.etCommission.editableText.toString()
            } else {
                callApi-=1
                binding!!.etCommission.error = resources.getString(R.string.invalid_commission)
            }
            /*if (binding!!.etNomineeDetails.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.nominee_details =
                    binding!!.etNomineeDetails.editableText.toString()
            } else {
                callApi-=1
                binding!!.etNomineeDetails.error = resources.getString(R.string.invalid_nominee_details)
            }*/
            /*if (binding!!.etAdditionalRider.editableText.toString().isNotEmpty()) {
                callApi+=1
                addLifeInsurance!!.additional_rider =
                    binding!!.etAdditionalRider.editableText.toString()
            } else {
                callApi-=1
                binding!!.etAdditionalRider.error = resources.getString(R.string.invalid_additional_rider)
            }*/
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
            addLifeInsurance!!.familayRemove = removeF
            addLifeInsurance!!.documentsRemoveDataArray = removeD
            addLifeInsurance!!.family = familyJson.toString()
            addLifeInsurance!!.document = docJson.toString()
            addLifeInsurance!!.sum_insured = binding!!.etSumInsured.editableText.toString()
            addLifeInsurance!!.pre_existing_decease = binding!!.etPed.editableText.toString()
            addLifeInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            addLifeInsurance!!.maturity_term = binding!!.etMaturityTerm.editableText.toString()
            addLifeInsurance!!.yearly_bonus_amount = binding!!.etYearlyBonusAmount.editableText.toString()
            addLifeInsurance!!.nominee_details = binding!!.etNomineeDetails.editableText.toString()
            addLifeInsurance!!.additional_rider = binding!!.etAdditionalRider.editableText.toString()
            addLifeInsurance!!.file = sendFiles!!
            if (callApi >= 9) {
                viewModel!!.editLifeInsurance(addLifeInsurance!!, policy!!.id!!.toString(),this )
            } else {
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    fun updateMember(){
        memberAdapter!!.updateList(familyList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
            if (requestCode == FILEREQUEST) {
                addLifeInsurance!!.policy_file = getFileFromURI(imageSelected!!, this)
                if (addLifeInsurance!!.policy_file!!.path.contains("pdf")) {
                    binding!!.ivPolicyFile.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                } else {
                    binding!!.ivPolicyFile.setImageURI(imageSelected)
                }
            } else {
                fileList[pos].file = getFileFromURI(imageSelected!!, this)!!
                documentAdapter!!.updateList(documentList, fileList)
            }
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

    override fun onRemoveFamily(position: Int) {
        if(position != 0) {
            if (familyList[position].family_id!!.isNotEmpty()) {
                removeFamily!!.add(familyList[position].family_id!!)
            }
            familyList.removeAt(position)
            familyIdList.removeAt(position)
            updateMember()
        }
    }

    override fun onError(errors: HashMap<String, Any>) {
        hideProgress()
        if (errors.containsKey("policy_number")) {
            binding!!.etPolicyNumber.error = errors["policy_number"].toString()
        } else if(errors.containsKey("preminum_payment_term")){
            binding!!.etPremiumPaymentTerm.error = errors["preminum_payment_term"].toString()
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

    }

    override fun onSuccessClient(client: ClientListResponse) {
        val gson = Gson()
        val json = gson.toJson(client)
        viewModel!!.getPreference().setStringValue(AppConstants.CLIENTS, json)
        AppConstants.clients = client.data!!
        viewModel!!.getCompanies(this)
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

        if (clientDetails!!.firstname!!.isNotEmpty()) {
            val clientPosition: Int = clientAdapter.getPosition(clientDetails!!.firstname!! + " " + clientDetails!!.lastname!!)
            binding!!.spClientName.setSelection(clientPosition)

            if (clientPosition >= 0) {
                familyMemberList!!.clear();
                families = clients!![clientPosition]!!.family_Details
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!!)
                }
                familyAdapter = ArrayAdapter(
                    this@EditLifeInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
            }
        }
        val companyPosition: Int = companyAdapter.getPosition(policy!!.company_name)
        binding!!.spCompanyName.setSelection(companyPosition)
    }
}