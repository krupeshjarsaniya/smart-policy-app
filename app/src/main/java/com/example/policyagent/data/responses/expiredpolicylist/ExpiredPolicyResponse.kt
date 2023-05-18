package com.example.policyagent.data.responses.expiredpolicylist

data class ExpiredPolicyResponse(
    val hasmore: Boolean? = false,
    val insurance: ArrayList<Insurance?>? = arrayListOf(),
    val status: Boolean? = false
) : java.io.Serializable