package com.example.policyagent.data.responses.commoninsurance

data class ClientPersonalDetails(
    val address: String? = "",
    val age: String? = "",
    val birthdate: String? = "",
    val city: String? = "",
    val client_Documents: ArrayList<Document?>? = arrayListOf(),
    val family_details: ArrayList<FamilyDetail?>? = arrayListOf(),
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
    val password: String? = "",
    val relationship: String? = "",
    val state: String? = "",
    val status: String? = "",
    val weight: String? = ""
) : java.io.Serializable