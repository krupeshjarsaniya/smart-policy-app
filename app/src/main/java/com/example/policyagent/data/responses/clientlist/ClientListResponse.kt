package com.example.policyagent.data.responses.clientlist

data class ClientListResponse(
    val `data`: ArrayList<ClientData?>? = arrayListOf(),
    val status: Boolean? = true
)