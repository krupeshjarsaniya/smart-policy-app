package com.example.policyagent.data.responses.monthlydue

data class Value(
    val agent_name: String? = "",
    val bonus: String? = "",
    val client_id: String? = "",
    val commision: String? = "",
    val company_name: String? = "",
    val date: Int? = 0,
    val familyWithInsurance: List<Any?>? = listOf(),
    val id: Int? = 0,
    val insuranceType: String? = "",
    val insurance_document: List<Any?>? = listOf(),
    val insurance_type: String? = "",
    val member_name: String? = "",
    val payment_mode: String? = "",
    val ped: String? = "",
    val plan_name: String? = "",
    val policy_file: String? = "",
    val policy_number: String? = "",
    val policy_term: String? = "",
    val premium_amount: String? = "",
    val red: String? = "",
    val rsd: String? = "",
    val st: String? = "",
    val sum_insured: String? = "",
    val total_sum_insured: String? = "",
    val waiting: String? = ""
) : java.io.Serializable