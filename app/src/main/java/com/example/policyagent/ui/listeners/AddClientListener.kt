package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.statelist.StateListResponse

interface AddClientListener {
        fun onStarted()
        fun onSuccess(data: CommonResponse)
        fun onSuccessState(client: StateListResponse)
        fun onFailure(message: String)
        fun onRemoveFamily(position: Int)
        fun onError(errors: HashMap<String,Any>)
        fun onLogout(message: String)
}