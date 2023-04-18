package com.example.policyagent.data.responses.monthlydue

import com.example.policyagent.data.responses.portfolio.Portfolio

data class MonthlyData(
    val month: Int? = 0,
    val value: ArrayList<Portfolio?>? = arrayListOf()
) : java.io.Serializable