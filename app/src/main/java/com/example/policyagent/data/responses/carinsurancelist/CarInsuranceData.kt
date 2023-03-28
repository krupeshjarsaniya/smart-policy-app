package com.example.policyagent.data.responses.carinsurancelist

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails
import com.example.policyagent.data.responses.commoninsurance.Document

data class CarInsuranceData(
    val agent_name: String? = "",
    val claim_details: String? = "",
    val client_Personal_Details: ClientPersonalDetails? = ClientPersonalDetails(),
    val client_name: String? = "",
    val company_name: String? = "",
    val discount: String? = "",
    val gst: String? = "",
    val gvw: String? = "",
    val id: Int? = 0,
    val idv_vehical_value: String? = "",
    val insurance_Documents: ArrayList<Document?>? = arrayListOf(),
    val insurance_sub_type: String? = "",
    val insurance_type: String? = "",
    val maturity_amount: String? = "",
    val net_preminum: String? = "",
    val no_claim_bonus: String? = "",
    val own_damage_premium: String? = "",
    val payment_mode: String? = "",
    val plan_name: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val premium_amount: String? = "",
    val red: String? = "",
    val registration_no: String? = "",
    val rsd: String? = "",
    val seating_capacity: String? = "",
    val total_premium: String? = "",
    val tp_premium: String? = "",
    val commision: String? = "",
    val member_name: String? = "",
    val policy_file: String? = "",
    val premium_type: String? = "",
) : java.io.Serializable