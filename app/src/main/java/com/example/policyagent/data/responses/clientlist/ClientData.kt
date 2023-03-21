package com.example.policyagent.data.responses.clientlist

import com.example.policyagent.data.responses.commoninsurance.Document
import com.example.policyagent.data.responses.commoninsurance.FamilyDetail

data class ClientData(
    val address: String? = "",
    val age: String? = "",
    val birthdate: String? = "",
    val city: String? = "",
    val client_Documents: ArrayList<Document?>? = arrayListOf(),
    val client_id: String? = "",
    val email: String? = "",
    val family_Details: ArrayList<FamilyDetail?>? = arrayListOf(),
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