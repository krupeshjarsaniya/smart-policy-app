package com.example.policyagent.data.responses.agentdashboard

data class AgentDashBoardResponse(
    val `data`: AgentDashboardData? = AgentDashboardData(),
    val status: Boolean? = false
)