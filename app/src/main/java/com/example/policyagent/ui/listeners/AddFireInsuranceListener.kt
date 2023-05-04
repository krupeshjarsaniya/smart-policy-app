package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.gst.GstResponse

interface AddFireInsuranceListener {
    fun onStarted()
    fun onSuccess(data: CommonResponse)
    fun onSuccessClient(client: ClientListResponse)
    fun onSuccessCompany(company: CompanyListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
    fun onSuccessGst(gst: GstResponse)

}