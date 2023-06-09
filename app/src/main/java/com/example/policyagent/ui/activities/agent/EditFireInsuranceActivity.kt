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
import com.example.policyagent.data.requests.addfireinsurance.AddFireInsurance
import com.example.policyagent.data.requests.editfireinsurance.EditFireInsurance
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
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.gst.GstResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ActivityAddFireInsuranceBinding
import com.example.policyagent.databinding.ActivityEditFireInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.adapters.agent.EditDocumentAdapter
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.AddFireInsuranceListener
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddFireInsuranceViewModel
import com.example.policyagent.ui.viewmodels.agent.EditFireInsuranceViewModel
import com.example.policyagent.util.*
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditFireInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener, AddFireInsuranceListener {

    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityEditFireInsuranceBinding? = null
    private var viewModel: EditFireInsuranceViewModel? = null
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = java.util.ArrayList<ImageModel>()
    private var documentAdapter: EditDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var policy: FireInsuranceData? = FireInsuranceData()
    var clientDetails: ClientPersonalDetails? = ClientPersonalDetails()
    var gson = Gson()
    var addFireInsurance: EditFireInsurance? = EditFireInsurance()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()
    var familyAdapter: ArrayAdapter<String>? = null
    var removeDocument: java.util.ArrayList<String>? = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_fire_insurance)
        viewModel = ViewModelProvider(this, factory)[EditFireInsuranceViewModel::class.java]
        viewModel!!.listener = this
        viewModel!!.getClients(this)
        binding!!.appBar.tvTitle.text = resources.getString(R.string.fire_insurance)
        documentAdapter = EditDocumentAdapter(this, this, this)
        binding!!.rvDocument.adapter = documentAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        val insuranceType = resources.getStringArray(R.array.fire_insurance_type)
        val insuranceTypeAdapter = ArrayAdapter(this, R.layout.dropdown_item, insuranceType)
        binding!!.spPolicyType.adapter = insuranceTypeAdapter


        if (intent.hasExtra(AppConstants.FIRE_INSURANCE)) {
            policy = intent.getSerializableExtra(AppConstants.FIRE_INSURANCE) as FireInsuranceData
            clientDetails = policy!!.client_Personal_Details
            Log.e("policyjson", gson.toJson(policy))
        }



        binding!!.etPolicyNumber.setText(policy!!.policy_number)

        binding!!.spFamilyMember.adapter = familyAdapter

        binding!!.tvStartDate.setText(policy!!.rsd)

        binding!!.tvEndDate.setText(policy!!.red)

        binding!!.etSt.setText(policy!!.st)

        val paymentPosition: Int = insuranceTypeAdapter.getPosition(policy!!.fire_policy_type)
        binding!!.spPolicyType.setSelection(paymentPosition)

        //binding!!.etPremiumAmount.setText(policy!!.premium_amount)

        binding!!.etNetAmount.setText(policy!!.premium_amount)

        addFireInsurance!!.gst = policy!!.gst!!

        binding!!.etGst.setText(policy!!.gst+"%")

        binding!!.etTotalPremium.setText(policy!!.total_premium)




        binding!!.etCommission.setText(policy!!.commision)

        if (policy!!.commision!!.isNotEmpty() && policy!!.premium_amount!!.isNotEmpty()) {
            var commission =
                policy!!.commision!!.toDouble() * policy!!.premium_amount!!.toDouble() / 100
            binding!!.etViewCommision.setText(String.format("%.2f", commission))
        }

        if(policy!!.gst!!.isNotEmpty() && policy!!.premium_amount!!.isNotEmpty()){
            var commission = policy!!.premium_amount!!.toDouble() + (policy!!.gst!!.toDouble() * policy!!.premium_amount!!.toDouble() / 100)
            binding!!.etTotalPremium.setText(String.format("%.2f",commission))
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
                addFireInsurance!!.client_id = clients!![position]!!.id!!.toString()
                familyMemberList!!.clear();
                families = clients!![position]!!.family_Details
                familyMemberList!!.add("Self")
                familyAdapter = ArrayAdapter(
                    this@EditFireInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
                binding!!.spFamilyMember.adapter = familyAdapter
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                    if (policy!!.member_name == families!![i]!!.firstname) {
                        val memberPosition: Int = familyAdapter!!.getPosition(families!![i]!!.firstname!! + " " + families!![i]!!.lastname!! + " - " + families!![i]!!.relationship)
                        binding!!.spFamilyMember.setSelection(memberPosition)
                    }
                }
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
                    addFireInsurance!!.member_id = families!![position - 1]!!.id!!.toString()
                } else{
                    addFireInsurance!!.member_id = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding!!.spPolicyType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addFireInsurance!!.fire_policy_type = binding!!.spPolicyType.selectedItem.toString()
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
                addFireInsurance!!.company_id = companies!![position]!!.id!!.toString()
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
                this@EditFireInsuranceActivity,
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
                this@EditFireInsuranceActivity,
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
        //familyList.add(MemberModel())

        //documentList.add(DocumentModel())
        //fileList.add(File(""))
        documentAdapter!!.updateList(documentList, fileList)


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

        binding!!.etCommission.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(binding!!.etNetAmount.editableText.toString().isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()) {
                    var commission = binding!!.etNetAmount.editableText.toString()
                        .toDouble() * binding!!.etCommission.editableText.toString()
                        .toDouble() / 100
                    binding!!.etViewCommision.setText(String.format("%.2f",commission))
                } else{
                    binding!!.etViewCommision.setText("0.00")
                }
            }
        })

        binding!!.etNetAmount.addTextChangedListener(object : TextWatcher {
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
                    addFireInsurance!!.gst!!.toDouble()
                } else{
                    0.0
                }
                netPremium = if(binding!!.etNetAmount.editableText.toString().isNotEmpty()){
                    binding!!.etNetAmount.editableText.toString().toDouble()
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
                binding!!.etTotalPremium.setText(totalPremium.toString())
            }
        })

        /*binding!!.etPremiumAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding!!.etPremiumAmount.editableText.toString()
                        .isNotEmpty() && binding!!.etCommission.editableText.toString().isNotEmpty()
                ) {
                    var commission = binding!!.etPremiumAmount.editableText.toString()
                        .toDouble() * binding!!.etCommission.editableText.toString()
                        .toDouble() / 100
                    binding!!.etViewCommision.setText(String.format("%.2f", commission))
                } else {
                    binding!!.etViewCommision.setText("0.00")
                }
            }

        })*/

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

            /*if (binding!!.etSt.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.st = binding!!.etSt.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etSt.error = resources.getString(R.string.invalid_st)
            }*/

            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.risk_start_date = binding!!.tvStartDate.text.toString()
            } else {
                callApi -= 1
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_risk_start_date)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.risk_end_date = binding!!.tvEndDate.text.toString()
            } else {
                callApi -= 1
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_risk_end_date)
            }
            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_policy_number)
            }
            /*if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etPremiumAmount.error =
                    resources.getString(R.string.invalid_premium_amount)
            }*/
            if (binding!!.etNetAmount.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.net_preminum = binding!!.etNetAmount.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etNetAmount.error = resources.getString(R.string.invalid_net_amount)
            }
            /*if (binding!!.etGst.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.gst = binding!!.etGst.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etGst.error = resources.getString(R.string.invalid_gst)
            }*/
            if (binding!!.etCommission.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.commision = binding!!.etCommission.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etCommission.error = resources.getString(R.string.invalid_commission)
            }
            if (binding!!.etTotalPremium.editableText.toString().isNotEmpty()) {
                callApi += 1
                addFireInsurance!!.total_premium =
                    binding!!.etTotalPremium.editableText.toString()
            } else {
                callApi -= 1
                binding!!.etTotalPremium.error = resources.getString(R.string.invalid_total_premium)
            }

            val strD = StringBuilder("")
            for (eachstring in removeDocument!!) {
                strD.append(eachstring).append(",")
            }

            var commaseparatedlistD = strD.toString()
            if (commaseparatedlistD.isNotEmpty()) commaseparatedlistD = commaseparatedlistD.substring(
                0, commaseparatedlistD.length - 1
            )
            addFireInsurance!!.st = binding!!.etSt.editableText.toString()

            var removeD = commaseparatedlistD
            addFireInsurance!!.documentsRemoveDataArray = removeD
            addFireInsurance!!.document = docJson.toString()
            addFireInsurance!!.file = sendFiles!!
            if (callApi >= 6) {
                viewModel!!.editFireInsurance(addFireInsurance!!, policy!!.id!!.toString(), this)
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
                addFireInsurance!!.policy_file = getFileFromURI(imageSelected!!, this)
                if (addFireInsurance!!.policy_file!!.path.contains("pdf")) {
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

    override fun onDownload(url: String) {

    }


    override fun onFileSelect(position: Int) {
        pos = position
        startActivityForResult(getFileChooserIntent(), 111)
    }

    override fun onremoveFile(position: Int) {
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
            clientList!!.add(clients!![i]!!.firstname!! + " " + clients!![i]!!.lastname!!)
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
            val clientPosition: Int = clientAdapter.getPosition(clientDetails!!.firstname + " " + clientDetails!!.lastname)
            binding!!.spClientName.setSelection(clientPosition)

            if (clientPosition >= 0) {
                familyMemberList!!.clear();
                families = clients!![clientPosition]!!.family_Details
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!!)
                }
                familyAdapter = ArrayAdapter(
                    this@EditFireInsuranceActivity,
                    R.layout.dropdown_item,
                    familyMemberList!!
                )
            }
        }
        val companyPosition: Int = companyAdapter.getPosition(policy!!.company_name)
        binding!!.spCompanyName.setSelection(companyPosition)
    }
}