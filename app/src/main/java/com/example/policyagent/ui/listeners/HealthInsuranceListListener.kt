package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceData
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceListResponse


interface HealthInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: HealthInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: HealthInsuranceData)
}