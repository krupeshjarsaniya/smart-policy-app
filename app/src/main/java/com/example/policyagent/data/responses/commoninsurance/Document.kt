package com.example.policyagent.data.responses.commoninsurance

data class Document(
    val document: String? = "",
    val document_type: String? = "",
    val id: Int? = 0,
    val modal_type: String? = "",
    val url: String? = ""
) : java.io.Serializable