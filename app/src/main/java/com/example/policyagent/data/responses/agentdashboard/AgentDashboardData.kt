package com.example.policyagent.data.responses.agentdashboard

data class AgentDashboardData(
    val due_payment: String? = "",
    val total_client: String? = "",
    val total_commision: String? = "",
    val total_insurance: String? = "",
    val total_expired_policy: String? = "0",
    val total_upcoming_expire_policy: String? = "0",
    val total_payment_due_policy: String? = "0",
    val total_upcoming_payment_policy: String? = "0",
)