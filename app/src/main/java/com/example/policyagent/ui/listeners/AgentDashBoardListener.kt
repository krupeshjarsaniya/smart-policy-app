package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.agentdashboard.AgentDashBoardResponse

interface AgentDashBoardListener {
    fun onStarted()
    fun onSuccess(data: AgentDashBoardResponse)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
    fun onLogout(message: String)
}