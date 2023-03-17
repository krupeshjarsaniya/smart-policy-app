package com.example.policyagent.data.responses

import org.json.JSONObject

data class CommonResponse(
    val message: String? = "",
    val status: Boolean? = false,
    val status_code: Int? = 0,
    val error: HashMap<String,Any>? = HashMap<String,Any>(),
)