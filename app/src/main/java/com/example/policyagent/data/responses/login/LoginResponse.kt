package com.example.policyagent.data.responses.login

import com.example.policyagent.data.database.entities.User
import org.json.JSONObject

data class LoginResponse(
    val access_token: String? = "",
    val `data`: User? = User(),
    val message: String? = "",
    val status: Boolean? = false,
    val status_code: Int? = 0,
    val token_type: String? = "",
    val user_type: String? = "",
    val error: HashMap<String,Any>? = HashMap<String,Any>(),
)