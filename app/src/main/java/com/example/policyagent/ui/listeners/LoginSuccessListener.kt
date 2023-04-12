package com.example.policyagent.ui.listeners

import com.example.policyagent.data.database.entities.User
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.memberlist.MemberListResponse

interface LoginSuccessListener {
    fun onStarted()
    fun onSuccessClient(client: ClientListResponse)
    fun onSuccessMember(member: MemberListResponse)
    fun onSuccessCompany(company: CompanyListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
}