package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.data.responses.statelist.StateListResponse

interface EditClientProfileListener {
    fun onStarted()
    fun onSuccess(data: LoginResponse)
    fun onSuccessState(client: StateListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
}