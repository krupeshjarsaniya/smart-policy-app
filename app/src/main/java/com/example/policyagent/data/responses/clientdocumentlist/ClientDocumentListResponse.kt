package com.example.policyagent.data.responses.clientdocumentlist

data class ClientDocumentListResponse(
    val `data`: ArrayList<ClientDocumentListData?>? = arrayListOf(),
    val status: Boolean? = false
)