package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.databinding.ActivityCarInsuranceDetailsBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.DocumentAdapter
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.getGlideProgress
import com.example.policyagent.util.hide
import com.example.policyagent.util.loadPdf

class CarInsuranceDetailsActivity : BaseActivity(), LoadDocumentListener {

    private var binding: ActivityCarInsuranceDetailsBinding? = null
    var policy: CarInsuranceData? = CarInsuranceData()
    var clientDetails: ClientPersonalDetails? = ClientPersonalDetails()
    var clientDocumentAdapter: DocumentAdapter? = null
    var insuranceDocumentAdapter: DocumentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_insurance_details)
        if (intent.hasExtra(AppConstants.CAR_INSURANCE)) {
            policy = intent.getSerializableExtra(AppConstants.CAR_INSURANCE) as CarInsuranceData
            clientDetails = policy!!.client_Personal_Details
        }
        if(intent.hasExtra(AppConstants.PERSONAL_DETAILS)){
            clientDetails = intent.getSerializableExtra(AppConstants.PERSONAL_DETAILS) as ClientPersonalDetails
        }
        binding!!.appBar.tvTitle.text = clientDetails!!.firstname
        binding!!.appBar.ivBack.setOnClickListener {
            finish()
        }


        if (policy!!.policy_number!!.isEmpty()) {
            binding!!.tvPolicyNo.text = "-"
        } else {
            binding!!.tvPolicyNo.text = policy!!.policy_number
        }
        if (clientDetails!!.firstname!!.isEmpty()) {
            binding!!.tvClientName.text = "-"
        } else {
            binding!!.tvClientName.text = clientDetails!!.firstname
        }
        if (policy!!.rsd!!.isEmpty()) {
            binding!!.tvStartDate.text = "-"
        } else {
            binding!!.tvStartDate.text = policy!!.rsd
        }
        if (policy!!.red!!.isEmpty()) {
            binding!!.tvEndDate.text = "-"
        } else {
            binding!!.tvEndDate.text = policy!!.red
        }
        if (policy!!.plan_name!!.isEmpty()) {
            binding!!.tvPlanName.text = "-"
        } else {
            binding!!.tvPlanName.text = policy!!.plan_name
        }
        if (policy!!.payment_mode!!.isEmpty()) {
            binding!!.tvPaymentMode.text = "-"
        } else {
            binding!!.tvPaymentMode.text = policy!!.payment_mode
        }
        if (policy!!.premium_amount!!.isEmpty()) {
            binding!!.tvPremiumAmount.text = "-"
        } else {
            binding!!.tvPremiumAmount.text = policy!!.premium_amount
        }
        if (policy!!.policy_term!!.isEmpty()) {
            binding!!.tvPolicyTerm.text = "-"
        } else {
            binding!!.tvPolicyTerm.text = policy!!.policy_term
        }
        if (policy!!.gst!!.isEmpty()) {
            binding!!.tvGst.text = "-"
        } else {
            binding!!.tvGst.text = policy!!.gst
        }
        if (policy!!.company_name!!.isEmpty()) {
            binding!!.tvCompany.text = "-"
        } else {
            binding!!.tvCompany.text = policy!!.company_name
        }
        if (policy!!.insurance_type!!.isEmpty()) {
            binding!!.tvInsuranceType.text = "-"
        } else {
            binding!!.tvInsuranceType.text = policy!!.insurance_type
        }
        if (policy!!.insurance_sub_type!!.isEmpty()) {
            binding!!.tvInsuranceSubType.text = "-"
        } else {
            binding!!.tvInsuranceSubType.text = policy!!.insurance_sub_type
        }
        if (policy!!.registration_no!!.isEmpty()) {
            binding!!.tvRto.text = "-"
        } else {
            binding!!.tvRto.text = policy!!.registration_no
        }
        if (policy!!.gvw!!.isEmpty()) {
            binding!!.tvGvw.text = policy!!.gvw
        } else {
            binding!!.tvGvw.text = policy!!.gvw
        }
        if (policy!!.claim_details!!.isEmpty()) {
            binding!!.tvClaimDetails.text = "-"
        } else {
            binding!!.tvClaimDetails.text = policy!!.claim_details
        }
        if (policy!!.maturity_amount!!.isEmpty()) {
            binding!!.tvMaturityAmount.text = "-"
        } else {
            binding!!.tvMaturityAmount.text = policy!!.maturity_amount
        }
        if (policy!!.own_damage_premium!!.isEmpty()) {
            binding!!.tvOwnDamagePremium.text = "-"
        } else {
            binding!!.tvOwnDamagePremium.text = policy!!.own_damage_premium
        }
        if (policy!!.tp_premium!!.isEmpty()) {
            binding!!.tvTpPremium.text = "-"
        } else {
            binding!!.tvTpPremium.text = policy!!.tp_premium
        }
        if (policy!!.idv_vehical_value!!.isEmpty()) {
            binding!!.tvIdv.text = "-"
        } else {
            binding!!.tvIdv.text = policy!!.idv_vehical_value
        }
        if (policy!!.no_claim_bonus!!.isEmpty()) {
            binding!!.tvNcb.text = "-"
        } else {
            binding!!.tvNcb.text = policy!!.no_claim_bonus
        }
        if (policy!!.net_preminum!!.isEmpty()) {
            binding!!.tvNetPremium.text = "-"
        } else {
            binding!!.tvNetPremium.text = policy!!.net_preminum
        }
        if (policy!!.total_premium!!.isEmpty()) {
            binding!!.tvTotalPremium.text = "-"
        } else {
            binding!!.tvTotalPremium.text = policy!!.total_premium
        }
        if (policy!!.discount!!.isEmpty()) {
            binding!!.tvDiscount.text = "-"
        } else {
            binding!!.tvDiscount.text = policy!!.discount
        }



        if (clientDetails!!.firstname!!.isEmpty()) {
            binding!!.tvName.text = "-"
        } else {
            binding!!.tvName.text = clientDetails!!.firstname
        }
        if (clientDetails!!.email!!.isEmpty()) {
            binding!!.tvEmail.text = "-"
        } else {
            binding!!.tvEmail.text = clientDetails!!.email
        }
        if (clientDetails!!.mobile!!.isEmpty()) {
            binding!!.tvMobile.text = "-"
        } else {
            binding!!.tvMobile.text = clientDetails!!.mobile
        }
        if (clientDetails!!.birthdate!!.isEmpty()) {
            binding!!.tvBirthDate.text = "-"
        } else {
            binding!!.tvBirthDate.text = clientDetails!!.birthdate
        }
        if (clientDetails!!.marital_status!!.isEmpty()) {
            binding!!.tvMaritalStatus.text = "-"
        } else {
            binding!!.tvMaritalStatus.text = clientDetails!!.marital_status
        }
        if (clientDetails!!.gender!!.isEmpty()) {
            binding!!.tvGender.text = "-"
        } else {
            binding!!.tvGender.text = clientDetails!!.gender
        }
        if (clientDetails!!.age!!.isEmpty()) {
            binding!!.tvAge.text = "-"
        } else {
            binding!!.tvAge.text = clientDetails!!.age
        }
        if (clientDetails!!.status!!.isEmpty()) {
            binding!!.tvStatus.text = "-"
        } else {
            binding!!.tvStatus.text = clientDetails!!.status
        }
        if (clientDetails!!.height!!.isEmpty()) {
            binding!!.tvHeight.text = "-"
        } else {
            binding!!.tvHeight.text = clientDetails!!.height
        }
        if (clientDetails!!.weight!!.isEmpty()) {
            binding!!.tvWeight.text = "-"
        } else {
            binding!!.tvWeight.text = clientDetails!!.weight
        }
        if (clientDetails!!.city!!.isEmpty()) {
            binding!!.tvCity.text = "-"
        } else {
            binding!!.tvCity.text = clientDetails!!.city
        }
        if (clientDetails!!.state!!.isEmpty()) {
            binding!!.tvState.text = "-"
        } else {
            binding!!.tvState.text = clientDetails!!.state
        }
        if (clientDetails!!.address!!.isEmpty()) {
            binding!!.tvAddress.text = "-"
        } else {
            binding!!.tvAddress.text = clientDetails!!.address
        }




        clientDocumentAdapter = DocumentAdapter(this, this)
        binding!!.rvClientDocument.adapter = clientDocumentAdapter
        clientDocumentAdapter!!.updateList(clientDetails!!.client_Documents!!)

        insuranceDocumentAdapter = DocumentAdapter(this, this)
        binding!!.rvInsuranceDocument.adapter = insuranceDocumentAdapter
        insuranceDocumentAdapter!!.updateList(policy!!.insurance_Documents!!)

        if (policy!!.insurance_Documents!!.isEmpty()) {
            binding!!.llInsuranceDocument.hide()
        }
        if (clientDetails!!.client_Documents!!.isEmpty()) {
            binding!!.llClientDocument.hide()
        }
    }

    override fun onLoadImage(image: String, imageview: ImageView) {
        Glide.with(this).load(image).placeholder(getGlideProgress(this))
            .error(R.drawable.ic_warning).into(imageview)
    }

    override fun onLoadPdf(url: String) {
        loadPdf(this, url)
    }
}