package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import org.json.JSONObject

interface ChangePasswordListener {
    fun onStarted()
    fun onSuccess(response: CommonResponse)
    fun onFailure(message: String)
    fun onError(error: HashMap<String,Any>)
    fun onLogout(message: String)

}