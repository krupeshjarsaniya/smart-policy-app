package com.example.policyagent.data.responses.memberlist

data class MemberListResponse(
    val `data`: ArrayList<MemberData?>? = arrayListOf(),
    val status: Boolean? = false
)