package com.example.policyagent.data.responses.yearlydue

data class YearlyDueData(
    val chart: ArrayList<Chart?>? = arrayListOf(),
    val years: ArrayList<Year?>? = arrayListOf()
)