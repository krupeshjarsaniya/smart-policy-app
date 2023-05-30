package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientdocumentlist.ClientDocumentListResponse


interface ClientDocumentListener {
    fun onStarted()
    fun onSuccess(data: ClientDocumentListResponse)
    fun onDownload(url: String, position: Int)
    fun onSuccessDelete(data: CommonResponse, position: Int)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
}