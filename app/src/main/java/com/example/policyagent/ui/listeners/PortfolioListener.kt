package com.example.policyagent.ui.listeners

import com.google.gson.JsonObject

interface PortfolioListener {

    fun onStarted()
    fun onSuccess(data: JsonObject)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)

}