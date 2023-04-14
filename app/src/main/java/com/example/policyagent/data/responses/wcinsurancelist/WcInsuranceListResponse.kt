package com.example.policyagent.data.responses.wcinsurancelist

data class WcInsuranceListResponse(
    val `data`: ArrayList<WcInsuranceData?>? = arrayListOf(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false,
) : java.io.Serializable