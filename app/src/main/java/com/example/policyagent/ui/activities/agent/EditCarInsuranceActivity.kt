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
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.requests.addcarinsurance.AddCarInsurance
import com.example.policyagent.data.requests.editcarinsurance.EditCarInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.ImageModel
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.databinding.ActivityAddCarInsuranceBinding
import com.example.policyagent.databinding.ActivityEditCarInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.EditDocumentAdapter
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddCarInsuranceListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddCarInsuranceViewModel
import com.example.policyagent.ui.viewmodels.agent.EditCarInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditCarInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddCarInsuranceListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityEditCarInsuranceBinding? = null
    private var viewModel: EditCarInsuranceViewModel? = null
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = java.util.ArrayList<ImageModel>()
    private var documentAdapter: EditDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var policy: CarInsuranceData? = CarInsuranceData()
    var clientDetails: ClientPersonalDetails? = ClientPersonalDetails()
    var gson = Gson()
    var addCarInsurance: EditCarInsurance? = EditCarInsurance()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()
    var familyAdapter: ArrayAdapter<String>? = null
    var removeDocument: java.util.ArrayList<String>? = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_car_insurance)
        viewModel = ViewModelProvider(this, factory)[EditCarInsuranceViewModel::class.java]
        viewModel!!.listener = this
        binding!!.appBar.tvTitle.text = resources.getString(R.string.car_insurance)
        documentAdapter = EditDocumentAdapter(this, this,this)
        binding!!.rvDocument.adapter = documentAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        val clientJson: String = viewModel!!.getPreference().getStringValue(AppConstants.CLIENTS)!!
        val clientObj: ClientListResponse =
            gson.fromJson(clientJson, ClientListResponse::class.java)
        clients = clientObj.data
        resources.getStringArray(R.array.clients)
        for (i in 0 until clients!!.size) {
            clientList!!.add(clients!![i]!!.firstname!!)
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

        val insuranceType = resources.getStringArray(R.array.car_insurance_type)
        val insuranceTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, insuranceType)


        val insuranceSubType = resources.getStringArray(R.array.car_insurance_sub_type)
        val insuranceSubTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, insuranceSubType)

        val commissionType = resources.getStringArray(R.array.commission_type)
        val commissionTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, commissionType)


        if (intent.hasExtra(AppConstants.CAR_INSURANCE)) {
            policy = intent.getSerializableExtra(AppConstants.CAR_INSURANCE) as CarInsuranceData
            clientDetails = policy!!.client_Personal_Details
            Log.e("policyjson", gson.toJson(policy))
        }



        binding!!.etPolicyNumber.setText(policy!!.policy_number)

        if (clientDetails!!.firstname!!.isNotEmpty()) {
            val clientPosition: Int = clientAdapter.getPosition(clientDetails!!.firstname)
            binding!!.spClientName.setSelection(clientPosition)

            if (clientPosition >= 0) {
                familyMemberList!!.clear();
                families = clients!![clientPosition]!!.family_Details
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!!)
                }
                familyAdapter = ArrayAdapter(
                    this@EditCarInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
            }

            if (policy!!.member_name != "") {
                val memberPosition: Int = familyAdapter!!.getPosition(policy!!.member_name)
                binding!!.spFamilyMember.setSelection(memberPosition)
            }
        }
        binding!!.spFamilyMember.adapter = familyAdapter

        binding!!.tvStartDate.setText(policy!!.rsd)

        binding!!.tvEndDate.setText(policy!!.red)

        binding!!.etRto.setText(policy!!.registration_no)

        val insurancePosition: Int = insuranceTypeAdapter.getPosition(policy!!.insurance_type)
        binding!!.spInsuranceType.setSelection(insurancePosition)

        val insuranceSubPosition: Int = insuranceSubTypeAdapter.getPosition(policy!!.insurance_sub_type)
        binding!!.spInsuranceSubType.setSelection(insuranceSubPosition)

        binding!!.etIdv.setText(policy!!.idv_vehical_value)

        binding!!.etNcb.setText(policy!!.no_claim_bonus)

        binding!!.etDtc.setText(policy!!.discount)

        binding!!.etPlanName.setText(policy!!.plan_name)

        binding!!.etPremiumAmount.setText(policy!!.premium_amount)

        if(policy!!.seating_capacity!!.isNotEmpty()) {
            binding!!.rlSeatingCapacity.show()
            binding!!.etSeatingCapacity.setText(policy!!.seating_capacity)
        } else{
            binding!!.rlSeatingCapacity.hide()
        }

        if(policy!!.gvw!!.isNotEmpty()) {
            binding!!.rlGvw.show()
            binding!!.etGvw.setText(policy!!.gvw)
        } else{
            binding!!.rlGvw.hide()
        }

        binding!!.etClaimDetails.setText(policy!!.claim_details)

        binding!!.etOwnDamagePremium.setText(policy!!.own_damage_premium)

        binding!!.etTpPremium.setText(policy!!.tp_premium)

        binding!!.etNetPremium.setText(policy!!.net_preminum)

        binding!!.etGst.setText(policy!!.gst)

        binding!!.etTotalPremium.setText(policy!!.total_premium)

        if(policy!!.premium_type!!.isNotEmpty()) {
            val commissionPosition: Int = commissionTypeAdapter.getPosition(policy!!.premium_type)
            binding!!.spCalculateCommission.setSelection(commissionPosition)
        }

        val companyPosition: Int = companyAdapter.getPosition(policy!!.company_name)
        binding!!.spCompanyName.setSelection(companyPosition)


        binding!!.etCommission.setText(policy!!.commision)

        if (policy!!.commision!!.isNotEmpty() && policy!!.premium_amount!!.isNotEmpty()) {
            var commission =
                policy!!.commision!!.toDouble() * policy!!.premium_amount!!.toDouble() / 100
            binding!!.etViewCommision.setText(String.format("%.2f", commission))
        }


        for (i in 0 until policy!!.insurance_Documents!!.size) {
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

        if (policy!!.policy_file != null && policy!!.policy_file!!.isNotEmpty()) {
            onLoadImage(policy!!.policy_file!!, binding!!.ivPolicyFile)
        }

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
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!!)
                }
                familyAdapter = ArrayAdapter(
                    this@EditCarInsuranceActivity,
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
                addCarInsurance!!.member_id = families!![position]!!.id!!.toString()
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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


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
                this@EditCarInsuranceActivity,
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
                this@EditCarInsuranceActivity,
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
                Log.e("documenttype2",documentList[i].documentype!!)
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
                checkCommision()
            }
        })

        binding!!.etTpPremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkCommision()
            }
        })

        binding!!.etNetPremium.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkCommision()
            }
        })

        binding!!.btnSave.setOnClickListener {
            var sendFiles: java.util.ArrayList<File>? = java.util.ArrayList()
            familyJson!!.clear()
            docJson!!.clear()
            var callApi: Int = 0
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
                    if (fileList[i].url!!.isEmpty()) {
                        sendFiles!!.add(fileList[i].file!!)
                        docJson!!.add(gson.toJson(documentList[i]))
                    }
                }
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
            if (binding!!.etIdv.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.idv_vehical_value = binding!!.etIdv.text.toString()
            } else {
                callApi-=1
                binding!!.etIdv.error = resources.getString(R.string.invalid_idv)
            }
            if (binding!!.etNcb.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.no_claim_bonus = binding!!.etNcb.text.toString()
            } else {
                callApi-=1
                binding!!.etNcb.error = resources.getString(R.string.invalid_ncb)
            }
            if (binding!!.etDtc.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.discount = binding!!.etDtc.text.toString()
            } else {
                callApi-=1
                binding!!.etDtc.error = resources.getString(R.string.invalid_discount)
            }
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
            if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()
            } else {
                callApi-=1
                binding!!.etPremiumAmount.error = resources.getString(R.string.invalid_premium_amount)
            }
            if (binding!!.etClaimDetails.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.claim_details =
                    binding!!.etClaimDetails.editableText.toString()
            } else {
                callApi-=1
                binding!!.etClaimDetails.error = resources.getString(R.string.invalid_claim_details)
            }
            if (binding!!.etOwnDamagePremium.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.own_damage_premium =
                    binding!!.etOwnDamagePremium.editableText.toString()
            } else {
                callApi-=1
                binding!!.etOwnDamagePremium.error = resources.getString(R.string.invalid_own_damage_premium)
            }
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
            if (binding!!.etGst.editableText.toString().isNotEmpty()) {
                callApi+=1
                addCarInsurance!!.gst =
                    binding!!.etGst.editableText.toString()
            } else {
                callApi-=1
                binding!!.etGst.error = resources.getString(R.string.invalid_gst)
            }
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
            val strD = StringBuilder("")
            for (eachstring in removeDocument!!) {
                strD.append(eachstring).append(",")
            }

            var commaseparatedlistD = strD.toString()
            if (commaseparatedlistD.length > 0) commaseparatedlistD = commaseparatedlistD.substring(
                0, commaseparatedlistD.length - 1
            )

            var removeD = commaseparatedlistD
            addCarInsurance!!.documentsRemoveDataArray = removeD
            addCarInsurance!!.document = docJson.toString()
            addCarInsurance!!.file = sendFiles!!
            if(callApi >= 16) {
                viewModel!!.editCarInsurance(addCarInsurance!!,policy!!.id!!.toString(), this)
            } else{
                showToastMessage(resources.getString(R.string.invalid_data))
            }
        }
    }

    fun checkCommision() {
        if (addCarInsurance!!.premium_type == "Own-Damage-Premium".toUpperCase()) {
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
        } else if (addCarInsurance!!.premium_type == "TP-Premium".toUpperCase()) {
            if (binding!!.etTpPremium.editableText.toString()
                    .isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()
            ) {
                var commission = binding!!.etTpPremium.editableText.toString()
                    .toDouble() * binding!!.etCommission.editableText.toString()
                    .toDouble() / 100
                binding!!.etViewCommision.setText(String.format("%.2f", commission))
            } else {
                binding!!.etViewCommision.setText("0.00")
            }
        } else if (addCarInsurance!!.premium_type == "Net-Premium".toUpperCase()) {
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
                    if (addCarInsurance!!.policy_file!!.path.contains("pdf")) {
                        binding!!.ivPolicyFile.setImageDrawable(resources.getDrawable(R.drawable.ic_pdf))
                    } else {
                        binding!!.ivPolicyFile.setImageURI(imageSelected)
                    }
                } else {
                    for(i in 0 until documentList.size){
                        Log.e("documenttype1",documentList[i].documentype!!)
                    }
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
            for(i in 0 until documentList.size){
                Log.e("documentlist22",documentList[i].documentype!!)
            }
            if (documentList[position].hidden_id!!.isNotEmpty()) {
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

        override fun onError(errors: HashMap<String, Any>) {
            hideProgress()
            if (errors.containsKey("policy_number")) {
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
}