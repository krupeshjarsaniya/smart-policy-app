package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.contactus.ContactUsResponse

interface ContactUsListener {
    fun onStarted()
    fun onSuccess(data: ContactUsResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
}