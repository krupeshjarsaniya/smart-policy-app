package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.policylist.PolicyData
import com.example.policyagent.data.responses.policylist.PolicyListResponse

interface PolicyListListener {
    fun onStarted()
    fun onSuccess(data: PolicyListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
    fun onItemClick(data: PolicyData)
    fun onDelete(id: String,position: Int)
    fun onEdit(data: PolicyData)
    fun onSuccessDelete(data: CommonResponse, position: Int)
}