package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse

interface VerifyOtpListener {
    fun onStarted()
    fun onSuccess(response: CommonResponse,type: String)
    fun onFailure(message: String)
    fun onError(error: HashMap<String,Any>)
}