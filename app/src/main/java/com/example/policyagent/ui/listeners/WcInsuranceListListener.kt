package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceData
import com.example.policyagent.data.responses.wcinsurancelist.WcInsuranceListResponse

interface WcInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: WcInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: WcInsuranceData)
}