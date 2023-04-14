package com.example.policyagent.data.responses.fireinsurancelist

data class FireInsuranceListResponse(
    val `data`: ArrayList<FireInsuranceData?>? = arrayListOf(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false
) : java.io.Serializable