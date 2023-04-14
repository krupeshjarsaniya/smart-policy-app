package com.example.policyagent.data.responses.carinsurancelist

data class CarInsuranceListResponse(
    val `data`: ArrayList<CarInsuranceData?>? = arrayListOf(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false,
) : java.io.Serializable