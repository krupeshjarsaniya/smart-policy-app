package com.example.policyagent.data.responses.policylist

data class PolicyData(
    val company_name: String? = "",
    val documents: ArrayList<Document?>? = arrayListOf(),
    val end_date: String? = "",
    val id: Int? = 0,
    val memberDetails: MemberDetails? = MemberDetails(),
    val model_type: String? = "",
    val plan_name: String? = "",
    val policy_number: String? = "",
    val premium: String? = "",
    val sa: String? = "",
    val start_date: String? = ""
) :java.io.Serializable