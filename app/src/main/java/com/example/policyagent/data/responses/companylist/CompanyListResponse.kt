package com.example.policyagent.data.responses.companylist

data class CompanyListResponse(
    val `data`: ArrayList<CompanyData?>? = arrayListOf(),
    val status: Boolean? = false
)