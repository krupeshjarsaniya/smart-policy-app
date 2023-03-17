package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse

interface ClientProfileListener {
    fun onStarted()
    fun onSuccess(response: CommonResponse)
    fun onFailure(message: String)
}