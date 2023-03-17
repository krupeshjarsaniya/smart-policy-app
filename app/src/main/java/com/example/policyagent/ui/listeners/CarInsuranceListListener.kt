package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceData
import com.example.policyagent.data.responses.carinsurancelist.CarInsuranceListResponse


interface CarInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: CarInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: CarInsuranceData)
}