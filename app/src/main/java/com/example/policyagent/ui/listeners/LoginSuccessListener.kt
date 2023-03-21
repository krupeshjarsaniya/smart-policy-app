package com.example.policyagent.ui.listeners

import com.example.policyagent.data.database.entities.User
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse

interface LoginSuccessListener {
    fun onStarted()
    fun onSuccessClient(user: ClientListResponse)
    fun onSuccessCompany(user: CompanyListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
}