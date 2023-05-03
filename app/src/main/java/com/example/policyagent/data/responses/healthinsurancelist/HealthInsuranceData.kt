package com.example.policyagent.data.responses.healthinsurancelist

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.Document
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail

data class HealthInsuranceData(
    val agent_id: String? = "",
    val bonus: String? = "",
    val client_Personal_Details: ClientPersonalDetails? = ClientPersonalDetails(),
    val client_id: String? = "",
    val company_id: String? = "",
    val id: Int? = 0,
    val insurance_Documents: ArrayList<Document?>? = arrayListOf(),
    val insurance_type: String? = "",
    val company_name: String? = "",
    val payment_mode: String? = "",
    val ped: String? = "",
    val plan_name: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val premium_amount: String? = "",
    val net_premium: String? = "",
    val gst: String? = "",
    val red: String? = "",
    val rsd: String? = "",
    val st: String? = "",
    val sum_insured: String? = "",
    val total_sum_insured: String? = "",
    val waiting: String? = "",
    val commision: String? = "",
    val member_name: String? = "",
    val policy_file: String? = "",
    val family: ArrayList<FamilyDetail?>? = arrayListOf(),
    ) : java.io.Serializable