package com.example.policyagent.ui.listeners

import com.example.policyagent.data.database.entities.User
import com.example.policyagent.data.responses.CommonResponse

interface ForgotPasswordListener {
    fun onStarted()
    fun onSuccess(response: CommonResponse)
    fun onFailure(message: String)
    fun onError(error: HashMap<String,Any>)
}