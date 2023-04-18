package com.example.policyagent.data.responses.monthlydue

data class MonthlyDueResponse(
    val `data`: ArrayList<MonthlyData?>? = arrayListOf(),
    val status: Boolean? = false
) : java.io.Serializable