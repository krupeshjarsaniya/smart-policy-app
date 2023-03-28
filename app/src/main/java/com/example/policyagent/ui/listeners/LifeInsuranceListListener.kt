package com.example.policyagent.ui.listeners

import android.widget.ImageView
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceData
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse

interface LifeInsuranceListListener {
    fun onStarted()
    fun onSuccess(data: LifeInsuranceListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onDelete(id: String,position: Int)
    fun onEdit(data: LifeInsuranceData)
    fun onSuccessDelete(data: CommonResponse,position: Int)
    fun onItemClick(data: LifeInsuranceData)
}