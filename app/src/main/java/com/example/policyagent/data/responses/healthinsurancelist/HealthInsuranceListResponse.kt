package com.example.policyagent.data.responses.healthinsurancelist

data class HealthInsuranceListResponse(
    val `data`: ArrayList<HealthInsuranceData?>? = arrayListOf(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false,
) : java.io.Serializable