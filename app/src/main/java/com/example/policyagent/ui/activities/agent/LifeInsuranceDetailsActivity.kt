package com.example.policyagent.ui.activities.agent

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.policyagent.R
import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.databinding.ActivityLifeInsuranceDetailsBinding
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.adapters.agent.DocumentAdapter
import com.example.policyagent.ui.listeners.LoadDocumentListener
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.getGlideProgress
import com.example.policyagent.util.hide
import com.example.policyagent.util.loadPdf

class LifeInsuranceDetailsActivity : BaseActivity(), LoadDocumentListener {
    private var binding: ActivityLifeInsuranceDetailsBinding? = null
    var policy: LifeInsuranceData? = LifeInsuranceData()
    var clientDetails: ClientPersonalDetails? = ClientPersonalDetails()
    var clientDocumentAdapter: DocumentAdapter? = null
    var insuranceDocumentAdapter: DocumentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_life_insurance_details)
        if (intent.hasExtra(AppConstants.LIFE_INSURANCE)) {
            policy = intent.getSerializableExtra(AppConstants.LIFE_INSURANCE) as LifeInsuranceData
            clientDetails = policy!!.client_Personal_Details
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
        if (policy!!.psd!!.isEmpty()) {
            binding!!.tvStartDate.text = "-"
        } else {
            binding!!.tvStartDate.text = policy!!.psd
        }
        if (policy!!.ped!!.isEmpty()) {
            binding!!.tvEndDate.text = "-"
        } else {
            binding!!.tvEndDate.text = policy!!.ped
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
        if (policy!!.maturity_amount!!.isEmpty()) {
            binding!!.tvMaturityAmount.text = "-"
        } else {
            binding!!.tvMaturityAmount.text = policy!!.maturity_amount
        }
        if (policy!!.policy_term!!.isEmpty()) {
            binding!!.tvPolicyTerm.text = "-"
        } else {
            binding!!.tvPolicyTerm.text = policy!!.policy_term
        }
        if (policy!!.sum_insured!!.isEmpty()) {
            binding!!.tvSi.text = "-"
        } else {
            binding!!.tvSi.text = policy!!.sum_insured
        }
        if (policy!!.preminum_payment_term!!.isEmpty()) {
            binding!!.tvPremiumPaymentTerm.text = "-"
        } else {
            binding!!.tvPremiumPaymentTerm.text = policy!!.preminum_payment_term
        }
        if (policy!!.maturity_benefit!!.isEmpty()) {
            binding!!.tvMaturityBenefit.text = "-"
        } else {
            binding!!.tvMaturityBenefit.text = policy!!.maturity_benefit
        }
        if (policy!!.yearly_bonus_amount!!.isEmpty()) {
            binding!!.tvYearlyBonusAmount.text = "-"
        } else {
            binding!!.tvYearlyBonusAmount.text = policy!!.yearly_bonus_amount
        }
        if (policy!!.company_name!!.isEmpty()) {
            binding!!.tvCompany.text = "-"
        } else {
            binding!!.tvCompany.text = policy!!.company_name
        }
        if (policy!!.nominee_details!!.isEmpty()) {
            binding!!.tvNomineeDetails.text = "-"
        } else {
            binding!!.tvNomineeDetails.text = policy!!.nominee_details
        }
        if (policy!!.additional_rider!!.isEmpty()) {
            binding!!.tvAdditionalRider.text = "-"
        } else {
            binding!!.tvAdditionalRider.text = policy!!.additional_rider
        }
        if (policy!!.maturity_term!!.isEmpty()) {
            binding!!.tvMaturityTerm.text = "-"
        } else {
            binding!!.tvMaturityTerm.text = policy!!.maturity_term
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