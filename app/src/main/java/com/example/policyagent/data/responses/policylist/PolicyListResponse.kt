package com.example.policyagent.data.responses.policylist

data class PolicyListResponse(
    val `data`: ArrayList<PolicyData?>? = arrayListOf(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false
) :java.io.Serializable