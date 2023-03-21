package com.example.policyagent.ui.activities.agent

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.policyagent.R
import com.example.policyagent.data.requests.addlifeinsurance.AddLifewInsurance
import com.example.policyagent.data.responses.DocumentModel
import com.example.policyagent.data.responses.MemberModel
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail
import com.example.policyagent.data.responses.companylist.CompanyData
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.databinding.ActivityAddLifeInsuranceBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.MemberAdapter
import com.example.policyagent.ui.adapters.agent.UploadDocumentAdapter
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.ui.listeners.FilePickerListener
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.ui.viewmodels.agent.AddLifeInsuranceViewModel
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.getFileChooserIntent
import com.example.policyagent.util.getFileFromURI
import com.google.gson.Gson
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddLifeInsuranceActivity : BaseActivity(), KodeinAware, LoadDocumentListener,
    FilePickerListener {
    override val kodein by kodein()
    private val factory: MainViewModelFactory by instance()
    private var binding: ActivityAddLifeInsuranceBinding? = null
    private var viewModel: AddLifeInsuranceViewModel? = null
    private var familyList = ArrayList<MemberModel>()
    private var documentList = ArrayList<DocumentModel>()
    private var fileList = ArrayList<File>()
    private var memberAdapter: MemberAdapter? = null
    private var documentAdapter: UploadDocumentAdapter? = null
    private val FILEREQUEST = 100
    private var pos: Int = 0
    private var familyJson: ArrayList<String>? = ArrayList()
    private var docJson: ArrayList<String>? = ArrayList()
    var gson = Gson()
    var addLifeInsurance: AddLifewInsurance? = AddLifewInsurance()
    var familyMemberList: ArrayList<String>? = ArrayList()
    var families: ArrayList<FamilyDetail?>? = ArrayList()
    var companyList: ArrayList<String>? = ArrayList()
    var companies: ArrayList<CompanyData?>? = ArrayList()
    var clientList: ArrayList<String>? = ArrayList()
    var clients: ArrayList<ClientData?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_life_insurance)
        viewModel = ViewModelProvider(this, factory)[AddLifeInsuranceViewModel::class.java]
        binding!!.appBar.tvTitle.text = resources.getString(R.string.life_insurance)
        memberAdapter = MemberAdapter(this)
        documentAdapter = UploadDocumentAdapter(this, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding!!.rvFamily.layoutManager = layoutManager
        binding!!.rvFamily.adapter = memberAdapter
        binding!!.rvDocument.adapter = documentAdapter
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }
        val clientJson: String = viewModel!!.getPreference().getStringValue(AppConstants.CLIENTS)!!
        val clientObj: ClientListResponse =
            gson.fromJson(clientJson, ClientListResponse::class.java)
        clients = clientObj.data//resources.getStringArray(R.array.clients)
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
                addLifeInsurance!!.client_id = clients!![position]!!.client_id
                familyMemberList!!.clear();
                families = clients!![position]!!.family_Details
                for (i in 0 until families!!.size) {
                    familyMemberList!!.add(families!![i]!!.firstname!!)
                }
                val familyAdapter = ArrayAdapter(
                    this@AddLifeInsuranceActivity,
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
                addLifeInsurance!!.member_id = families!![position]!!.id!!.toString()
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
                this@AddLifeInsuranceActivity,
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
            datePicker.show()
        }

        binding!!.tvEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar.get(Calendar.YEAR)
            val mm = calendar.get(Calendar.MONTH)
            val dd = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this@AddLifeInsuranceActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1).toString() + "-" + year.toString())
                    binding!!.tvEndDate.setText(date)
                },
                yy,
                mm,
                dd
            )
            binding!!.tvEndDate.setTextColor(resources.getColor(R.color.black))
            datePicker.show()
        }

        binding!!.ivPolicyFile.setOnClickListener {
            startActivityForResult(getFileChooserIntent(), FILEREQUEST)
        }
        familyList.add(MemberModel())
        memberAdapter!!.updateList(familyList)

        documentList.add(DocumentModel())
        fileList.add(File(""))
        documentAdapter!!.updateList(documentList, fileList)

        binding!!.tvAddMember.setOnClickListener {
            var addData: Boolean? = false
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
            var addData: Boolean? = false
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
            for (family in familyList) {
                familyJson!!.add(gson.toJson(family))
            }
            for (doc in documentList) {
                docJson!!.add(gson.toJson(doc))
            }
            if (binding!!.etSumInsured.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.sum_insured = binding!!.etSumInsured.editableText.toString()
            } else {
                binding!!.etSumInsured.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etSumInsured.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.tvStartDate.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.policy_start_date = binding!!.tvStartDate.text.toString()
            } else {
                binding!!.tvStartDate.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.tvStartDate.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.tvEndDate.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.policy_end_date = binding!!.tvEndDate.text.toString()
            } else {
                binding!!.tvEndDate.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.tvEndDate.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPed.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.pre_existing_decease = binding!!.etPed.editableText.toString()
            } else {
                binding!!.etPed.background = resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPed.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPolicyNumber.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.policy_number = binding!!.etPolicyNumber.editableText.toString()
            } else {
                binding!!.etPolicyNumber.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPolicyNumber.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPlanName.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.plan_name = binding!!.etPlanName.editableText.toString()
            } else {
                binding!!.etPolicyNumber.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPlanName.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPremiumAmount.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.premium_amount =
                    binding!!.etPremiumAmount.editableText.toString()
            } else {
                binding!!.etPremiumAmount.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPremiumAmount.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etMaturityAmount.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.maturity_amount =
                    binding!!.etMaturityAmount.editableText.toString()
            } else {
                binding!!.etMaturityAmount.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etMaturityAmount.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPolicyTerm.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.policy_term = binding!!.etPolicyTerm.editableText.toString()
            } else {
                binding!!.etPolicyTerm.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPolicyTerm.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etMaturityBenefit.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.maturity_benefit =
                    binding!!.etMaturityBenefit.editableText.toString()
            } else {
                binding!!.etMaturityBenefit.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etMaturityBenefit.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etPremiumPaymentTerm.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.preminum_payment_term =
                    binding!!.etPremiumPaymentTerm.editableText.toString()
            } else {
                binding!!.etPremiumPaymentTerm.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etPremiumPaymentTerm.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etMaturityTerm.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.maturity_term = binding!!.etMaturityTerm.editableText.toString()
            } else {
                binding!!.etMaturityTerm.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etMaturityTerm.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etYearlyBonusAmount.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.yearly_bonus_amount =
                    binding!!.etYearlyBonusAmount.editableText.toString()
            } else {
                binding!!.etYearlyBonusAmount.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etYearlyBonusAmount.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etCommission.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.commision = binding!!.etCommission.editableText.toString()
            } else {
                binding!!.etCommission.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etCommission.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etNomineeDetails.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.nominee_details =
                    binding!!.etNomineeDetails.editableText.toString()
            } else {
                binding!!.etNomineeDetails.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etNomineeDetails.error = resources.getString(R.string.invalid_data)
            }
            if (binding!!.etAdditionalRider.editableText.toString().isNotEmpty()) {
                addLifeInsurance!!.additional_rider =
                    binding!!.etAdditionalRider.editableText.toString()
            } else {
                binding!!.etAdditionalRider.background =
                    resources.getDrawable(R.drawable.bg_edit_text_error)
                binding!!.etAdditionalRider.error = resources.getString(R.string.invalid_data)
            }
            addLifeInsurance!!.family = familyJson.toString()
            addLifeInsurance!!.document = docJson.toString()
            addLifeInsurance!!.file = fileList
            viewModel!!.addLifeInsurance(addLifeInsurance!!, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageSelected = data!!.data
            if (requestCode == FILEREQUEST) {
                addLifeInsurance!!.policy_file = getFileFromURI(imageSelected!!, this)
                binding!!.ivPolicyFile.setImageURI(imageSelected)
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
}