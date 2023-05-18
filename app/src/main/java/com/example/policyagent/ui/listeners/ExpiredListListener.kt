package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.expiredpolicylist.ExpiredPolicyResponse
import com.example.policyagent.data.responses.expiredpolicylist.Insurance
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse


interface ExpiredListListener {
    fun onStarted()
    fun onSuccess(data: ExpiredPolicyResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: Insurance)
    fun onLogout(message: String)
}