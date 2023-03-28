package com.example.policyagent.data.responses.fireinsurancelist

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.Document

data class FireInsuranceData(
    val agent_id: String? = "",
    val client_Personal_Details: ClientPersonalDetails? = ClientPersonalDetails(),
    val client_id: String? = "",
    val commision: String? = "",
    val company_id: String? = "",
    val fire_policy_type: String? = "",
    val gst: String? = "",
    val id: Int? = 0,
    val insurance_Documents: ArrayList<Document?>? = arrayListOf(),
    val maturity_amount: String? = "",
    val member_id: String? = "",
    val net_preminum: String? = "",
    val payment_mode: String? = "",
    val plan_name: String? = "",
    val company_name: String? = "",
    val policy_term: String? = "",
    val policy_number: String? = "",
    val premium_amount: String? = "",
    val red: String? = "",
    val rsd: String? = "",
    val st: String? = "",
    val total_premium: String? = "",
    val member_name: String? = "",
    val policy_file: String? = "",
) : java.io.Serializable