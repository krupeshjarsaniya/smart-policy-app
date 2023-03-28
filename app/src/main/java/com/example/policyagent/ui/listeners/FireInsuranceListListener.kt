package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceData
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceListResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse

interface FireInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: FireInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: FireInsuranceData)
    fun onDelete(id: String,position: Int)
    fun onEdit(data: FireInsuranceData)
    fun onSuccessDelete(data: CommonResponse, position: Int)
}