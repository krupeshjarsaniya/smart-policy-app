package com.example.policyagent.data.responses.lifeinsurancelist

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.Document
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail

data class LifeInsuranceData(
    val additional_rider: String? = "",
    val agent_name: String? = "",
    val client_Personal_Details: ClientPersonalDetails? = ClientPersonalDetails(),
    val client_name: String? = "",
    val company_name: String? = "",
    val id: Int? = 0,
    val insurance_Documents: ArrayList<Document?>? = arrayListOf(),
    val maturity_amount: String? = "",
    val maturity_benefit: String? = "",
    val maturity_term: String? = "",
    val nominee_details: String? = "",
    val payment_mode: String? = "",
    val ped: String? = "",
    val plan_name: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val pre_exist_decease: String? = "",
    val preminum_payment_term: String? = "",
    val premium_amount: String? = "",
    val psd: String? = "",
    val sum_insured: String? = "",
    val yearly_bonus_amount: String? = "",
    val commision: String? = "",
    val member_name: String? = "",
    val policy_file: String? = "",
    val family: ArrayList<FamilyDetail?>? = arrayListOf(),
    ) : java.io.Serializable