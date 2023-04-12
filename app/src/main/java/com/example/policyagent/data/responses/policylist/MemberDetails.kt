package com.example.policyagent.data.responses.policylist

data class MemberDetails(
    val address: String? = "",
    val age: String? = "",
    val agent_id: String? = "",
    val birthdate: String? = "",
    val city: String? = "",
    val client_id: String? = "",
    val email: String? = "",
    val firstname: String? = "",
    val gender: String? = "",
    val height: String? = "",
    val id: Int? = 0,
    val lastname: String? = "",
    val marital_status: String? = "",
    val middlename: String? = "",
    val mobile: String? = "",
    val pan_number: String? = "",
    val parent_id: String? = "",
    val password: String? = "",
    val relationship: String? = "",
    val state: String? = "",
    val status: String? = "",
    val weight: String? = ""
) :java.io.Serializable