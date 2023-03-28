package com.example.policyagent.data.responses

import java.io.File

data class DocumentModel (
    var documentype: String? = "",
    var documentsub_type: String? = "",
    var hidden_id: String? = ""
) : java.io.Serializable

data class ImageModel (
    var url: String? = "",
    var file: File? = null,
) : java.io.Serializable