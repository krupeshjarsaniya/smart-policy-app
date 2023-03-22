package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
interface AddHealthInsuranceListener {
    fun onStarted()
    fun onSuccess(data: CommonResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
}