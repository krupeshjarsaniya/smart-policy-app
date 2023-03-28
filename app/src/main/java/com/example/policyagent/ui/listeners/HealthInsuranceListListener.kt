package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceListResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData


interface HealthInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: HealthInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: HealthInsuranceData)
    fun onEdit(data: HealthInsuranceData)
    fun onDelete(id: String,position: Int)
    fun onSuccessDelete(data: CommonResponse, position: Int)
}