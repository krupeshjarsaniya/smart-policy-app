package com.example.policyagent.data.responses.portfolio

data class Portfolio(
    val additional_rider: String? = "",
    val agent_id: String? = "",
    val agent_name: String? = "",
    val bonus: String? = "",
    val claim_details: String? = "",
    val client_id: String? = "",
    val client_name: String? = "",
    val commision: String? = "",
    val company_id: String? = "",
    val company_name: String? = "",
    val discount: String? = "",
    val familyWithInsurance: List<FamilyWithInsurance?>? = listOf(),
    val fire_policy_type: String? = "",
    val gst: String? = "",
    val gvw: String? = "",
    val id: Int? = 0,
    val idv_vehical_value: String? = "",
    val insuranceType: String? = "",
    val insurance_document: List<InsuranceDocument?>? = listOf(),
    val insurance_sub_type: String? = "",
    val insurance_type: String? = "",
    val maturity_amount: String? = "",
    val maturity_benefit: String? = "",
    val maturity_term: String? = "",
    val member_id: String? = "",
    val member_name: String? = "",
    val net_preminum: String? = "",
    val no_claim_bonus: String? = "",
    val no_of: String? = "",
    val nominee_details: String? = "",
    val own_damage_premium: String? = "",
    val payment_mode: String? = "",
    val ped: String? = "",
    val plan_name: String? = "",
    val policy_file: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val pre_exist_decease: String? = "",
    val preminum_payment_term: String? = "",
    val premium_amount: String? = "",
    val premium_type: String? = "",
    val psd: String? = "",
    val red: String? = "",
    val registration_no: String? = "",
    val rsd: String? = "",
    val seating_capacity: String? = "",
    val st: String? = "",
    val st_with_me: String? = "",
    val sum_insured: String? = "",
    val total_premium: String? = "",
    val total_sum_insured: String? = "",
    val tp_premium: String? = "",
    val waiting: String? = "",
    val yearly_bonus_amount: String? = ""
) : java.io.Serializable