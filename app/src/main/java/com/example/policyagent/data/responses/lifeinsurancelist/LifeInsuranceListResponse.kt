package com.example.policyagent.data.responses.lifeinsurancelist

data class LifeInsuranceListResponse(
    val `data`: ArrayList<LifeInsuranceData?>? = arrayListOf(),
    val status: Boolean? = false
) : java.io.Serializable