package com.example.policyagent.data.responses.clienthome

data class ClientHomeResponse(
    val `data`: ArrayList<ClientHomeData?>? = arrayListOf(),
    val status: Boolean? = false
)