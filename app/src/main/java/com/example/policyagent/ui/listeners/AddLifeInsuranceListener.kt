package com.example.policyagent.ui.listeners
import com.example.policyagent.data.responses.CommonResponse

interface AddLifeInsuranceListener {
    fun onStarted()
    fun onSuccess(data: CommonResponse)
    fun onFailure(message: String)
    fun onRemoveFamily(position: Int)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)

}