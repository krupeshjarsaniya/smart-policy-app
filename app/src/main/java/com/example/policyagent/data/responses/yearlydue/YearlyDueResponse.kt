package com.example.policyagent.data.responses.yearlydue

data class YearlyDueResponse(
    val `data`: YearlyDueData? = YearlyDueData(),
    val status: Boolean? = false
)