package com.example.policyagent.ui.activities.agent


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
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
import com.example.policyagent.data.requests.addcarinsurance.AddCarInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.gst.GstResponse
import com.example.policyagent.databinding.ActivityAddCarInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.activities.client.ClientDashboardActivity
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddCarInsuranceListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddCarInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddCarInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddCarInsuranceListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddCarInsuranceBinding? = null
    private var viewModel: AddCarInsuranceViewModel? = null
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<File>()
    private var documentAdapter: UploadDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var gson = Gson()
    var addCarInsurance: AddCarInsurance? = AddCarInsurance()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()
    var currentDate = Calendar.getInstance().time
    var aYearAfter = Calendar.getInstance()
    var df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car_insurance)
        viewModel = ViewModelProvider(this, factory)[AddCarInsuranceViewModel::class.java]
        viewModel!!.listener = this

        var formattedDate = df.format(currentDate)

        aYearAfter.add(Calendar.YEAR, 1)
        var yearFormattedDate = df.format(aYearAfter.time)

        binding!!.tvStartDate.setText(formattedDate)
        binding!!.tvEndDate.setText(yearFormattedDate)

        binding!!.appBar.tvTitle.text = resources.getString(R.string.car_insurance)
        documentAdapter = UploadDocumentAdapter(this, this)
        binding!!.rvDocument.adapter = documentAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        viewModel!!.getClients(this)


        binding!!.spClientName.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addCarInsurance!!.client_id = clients!![position]!!.id!!.toString()
                familyMemberList!!.clear();
                families = clients!![position]!!.family_Details
                familyMemberList!!.add("Self")
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                }
                val familyAdapter = ArrayAdapter(
                    this@AddCarInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
                binding!!.spFamilyMember.adapter = familyAdapter
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
                if(position != 0) {
                    addCarInsurance!!.member_id = families!![position - 1]!!.id!!.toString()
                } else{
                    addCarInsurance!!.member_id = ""
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
                addCarInsurance!!.company_id = companies!![position]!!.id!!.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        val insuranceType = resources.getStringArray(R.array.car_insurance_type)
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
                addCarInsurance!!.insurance_type = binding!!.spInsuranceType.selectedItem.toString().toUpperCase()
                if(addCarInsurance!!.insurance_type == "LIABILITY"){
                    binding!!.etIdv.setText("")
                    binding!!.etNcb.setText("")
                    binding!!.etDtc.setText("")
                    binding!!.etClaimDetails.setText("")
                    binding!!.etOwnDamagePremium.setText("")
                    binding!!.rlIdv.hide()
                    binding!!.rlNcb.hide()
                    binding!!.rlDiscount.hide()
                    binding!!.rlClaimDetails.hide()
                    binding!!.rlOwnDamagePremium.hide()
                    binding!!.spCalculateCommission.hide()
                    addCarInsurance!!.premium_type = "NET-PREMIUM"
                    binding!!.etNetPremium.setText(binding!!.etTpPremium.editableText.toString())
                    checkCommision()
                } else{
                    binding!!.rlIdv.show()
                    binding!!.rlNcb.show()
                    binding!!.rlDiscount.show()
                    binding!!.rlClaimDetails.show()
                    binding!!.rlOwnDamagePremium.show()
                    binding!!.spCalculateCommission.show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val insuranceSubType = resources.getStringArray(R.array.car_insurance_sub_type)
        val insuranceSubTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, insuranceSubType)
        binding!!.spInsuranceSubType.adapter = insuranceSubTypeAdapter
        binding!!.spInsuranceSubType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("position",position.toString())
                when (position) {
                    0 -> {
                        binding!!.rlSeatingCapacity.show()
                        binding!!.rlGvw.hide()
                        binding!!.etGvw.setText("")
                    }
                    1 -> {
                        binding!!.rlGvw.show()
                        binding!!.rlSeatingCapacity.hide()
                        binding!!.etSeatingCapacity.setText("")
                    }
                    else -> {
                        binding!!.rlGvw.hide()
                        binding!!.rlSeatingCapacity.hide()
                        binding!!.etGvw.setText("")
                        binding!!.etSeatingCapacity.setText("")
                    }
                }
                addCarInsurance!!.insurance_sub_type = binding!!.spInsuranceSubType.selectedItem.toString().toUpperCase()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val commissionType = resources.getStringArray(R.array.commission_type)
        val commissionTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, commissionType)
        binding!!.spCalculateCommission.adapter = commissionTypeAdapter
        binding!!.spCalculateCommission.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addCarInsurance!!.premium_type = binding!!.spCalculateCommission.selectedItem.toString().toUpperCase()
                Log.e("premiumtype",addCarInsurance!!.premium_type.toString())
                checkCommision()
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
                this@AddCarInsuranceActivity,
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
                this@AddCarInsuranceActivity,
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

        binding!!.ivPolicyFile.setOnClickListener {
            startActivityForResult(getFileChooserIntent(), FILEREQUEST)
        }

        documentAdapter!!.updateList(documentList, fileList)


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
                checkCommision()
            }
        })

        binding!!.etOwnDamagePremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etOwnDamagePremium.editableText.toString().isNotEmpty() && binding!!.etTpPremium.editableText.toString().isNotEmpty()) {
                    var netPremium = binding!!.etOwnDamagePremium.editableText.toString()
                        .toDouble() + binding!!.etTpPremium.editableText.toString().toDouble()
                    binding!!.etNetPremium.setText(netPremium.toString())
                }
                checkCommision()
            }
        })

        binding!!.etTpPremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etOwnDamagePremium.editableText.toString().isNotEmpty() && binding!!.etTpPremium.editableText.toString().isNotEmpty()) {
                    var netPremium = binding!!.etOwnDamagePremium.editableText.toString()
                        .toDouble() + binding!!.etTpPremium.editableText.toString().toDouble()
                    binding!!.etNetPremium.setText(netPremium.toString())
                }
               checkCommision()
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
                gst = if(binding!!.etGst.editableText.toString().isNotEmpty()){
                    addCarInsurance!!.gst!!.toDouble()
                } else{
                    0.0
                }
                netPremium = if(binding!!.etNetPremium.editableText.toString().isNotEmpty()){
                    binding!!.etNetPremium.editableText.toString().toDouble()
                } else{
                    0.0
                }
                totalPremium = netPremium + (netPremium * gst / 100)
                binding!!.etTotalPremium.setText(totalPremium.toString())
                checkCommision()
            }
        })

        binding!!.btnSave.setOnClickListener {
            familyJson!!.clear()
            docJson!!.clear()
            var callApi: Int = 0
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
            for (doc in documentList) {
                docJson!!.add(gson.toJson(doc))
            }
            if (binding!!.etRto.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.registration_number_rto = binding!!.etRto.editableText.toString()
            } else {
                callApi-=1
                binding!!.etRto.error = resources.getString(R.string.invalid_rto)
            }
            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.risk_start_date = binding!!.tvStartDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_risk_start_date)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.risk_end_date = binding!!.tvEndDate.text.toString()
            } else {
                callApi-=1
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_risk_end_date)
            }
            /*if (binding!!.etIdv.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.idv_vehical_value = binding!!.etIdv.text.toString()
            } else {
                callApi-=1
                binding!!.etIdv.error = resources.getString(R.string.invalid_idv)
            }*/
            /*if (binding!!.etNcb.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.no_claim_bonus = binding!!.etNcb.text.toString()
            } else {
                callApi-=1
                binding!!.etNcb.error = resources.getString(R.string.invalid_ncb)
            }*/
            /*if (binding!!.etDtc.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.discount = binding!!.etDtc.text.toString()
            } else {
                callApi-=1
                binding!!.etDtc.error = resources.getString(R.string.invalid_discount)
            }*/
            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_policy_number)
            }
            if (binding!!.etPlanName.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPlanName.error = resources.getString(R.string.invalid_plan_name)
            }
            /*if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPremiumAmount.error = resources.getString(R.string.invalid_premium_amount)
            }*/
            /*if (binding!!.etClaimDetails.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.claim_details =
                    binding!!.etClaimDetails.editableText.toString()
            } else {
                callApi-=1
                binding!!.etClaimDetails.error = resources.getString(R.string.invalid_claim_details)
            }*/
            /*if (binding!!.etOwnDamagePremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.own_damage_premium = binding!!.etOwnDamagePremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etOwnDamagePremium.error = resources.getString(R.string.invalid_own_damage_premium)
            }*/
            if (binding!!.etTpPremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.tp_premium =
                    binding!!.etTpPremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etTpPremium.error = resources.getString(R.string.invalid_tp_premium)
            }
            if (binding!!.etNetPremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.net_preminum =
                    binding!!.etNetPremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etNetPremium.error = resources.getString(R.string.invalid_net_premium)
            }
            /*if (binding!!.etGst.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.gst =
                    binding!!.etGst.editableText.toString()
            } else {
                callApi-=1
                binding!!.etGst.error = resources.getString(R.string.invalid_gst)
            }*/
            if (binding!!.etTotalPremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.total_premium =
                    binding!!.etTotalPremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etTotalPremium.error = resources.getString(R.string.invalid_total_premium)
            }
            if (binding!!.etCommission.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.commision =
                    binding!!.etCommission.editableText.toString()
            } else {
                callApi-=1
                binding!!.etCommission.error = resources.getString(R.string.invalid_commission)
            }
            addCarInsurance!!.seating_capacity = binding!!.etSeatingCapacity.editableText.toString()
            addCarInsurance!!.gvw = binding!!.etGvw.editableText.toString()

            addCarInsurance!!.idv_vehical_value = binding!!.etIdv.editableText.toString()
            addCarInsurance!!.no_claim_bonus = binding!!.etNcb.editableText.toString()
            addCarInsurance!!.claim_details = binding!!.etClaimDetails.editableText.toString()
            addCarInsurance!!.own_damage_premium = binding!!.etOwnDamagePremium.editableText.toString()
            addCarInsurance!!.discount = binding!!.etDtc.text.toString()

            addCarInsurance!!.document = docJson.toString()
            addCarInsurance!!.file = fileList
            if(callApi >= 9) {
                viewModel!!.addCarInsurance(addCarInsurance!!, this)
            } else{
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    fun checkCommision(){
        if(addCarInsurance!!.premium_type == "Own-Damage-Premium".toUpperCase()) {
            if (binding!!.etOwnDamagePremium.editableText.toString()
                    .isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()
            ) {
                var commission = binding!!.etOwnDamagePremium.editableText.toString()
                    .toDouble() * binding!!.etCommission.editableText.toString()
                    .toDouble() / 100
                binding!!.etViewCommision.setText(String.format("%.2f", commission))
            } else {
                binding!!.etViewCommision.setText("0.00")
            }
        }
//        else if(addCarInsurance!!.premium_type == "TP-Premium".toUpperCase()){
//            if(binding!!.etTpPremium.editableText.toString().isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()) {
//                var commission = binding!!.etTpPremium.editableText.toString()
//                    .toDouble() * binding!!.etCommission.editableText.toString()
//                    .toDouble() / 100
//                binding!!.etViewCommision.setText(String.format("%.2f",commission))
//            } else{
//                binding!!.etViewCommision.setText("0.00")
//            }
//        }
            else if (addCarInsurance!!.premium_type == "Net-Premium".toUpperCase()) {
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
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
            if (requestCode == FILEREQUEST) {
                addCarInsurance!!.policy_file = getFileFromURI(imageSelected!!, this)
                if(addCarInsurance!!.policy_file!!.path.contains("pdf")){
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
            addCarInsurance!!.gst = gst.data!!.gst.toString()
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