package com.example.policyagent.data.responses.statelist

data class StateListResponse(
    val `data`: ArrayList<StateData?>? = arrayListOf(),
    val status: Boolean? = false
)