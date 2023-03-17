package com.example.policyagent.data.responses.wcinsurancelist

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.Document

data class WcInsuranceData(
    val agent_name: String? = "",
    val client_Personal_Details: ClientPersonalDetails? = ClientPersonalDetails(),
    val client_name: String? = "",
    val company_name: String? = "",
    val gst: String? = "",
    val id: Int? = 0,
    val insurance_Documents: ArrayList<Document?>? = arrayListOf(),
    val maturity_amount: String? = "",
    val net_preminum: String? = "",
    val no_of: String? = "",
    val payment_mode: String? = "",
    val plan_name: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val premium_amount: String? = "",
    val red: String? = "",
    val rsd: String? = "",
    val st: String? = "",
    val st_with_me: String? = "",
    val total_premium: String? = ""
) : java.io.Serializable