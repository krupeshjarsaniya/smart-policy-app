package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.clientlist.ClientListResponse

interface ClientListListener {
    fun onStarted()
    fun onSuccess(data: ClientListResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onItemClick(data: ClientData)
    fun onEdit(data: ClientData)
    fun onWhatsApp(data: ClientData)
    fun onDelete(id: String,position: Int)
    fun onSuccessDelete(data: CommonResponse, position: Int)
    fun onLogout(message: String)
}