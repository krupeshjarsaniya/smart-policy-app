package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.monthlydue.MonthlyDueResponse
import com.example.policyagent.data.responses.portfolio.Portfolio

interface YearlyPremiumListener {
    fun onStarted()
    fun onSuccess(response: MonthlyDueResponse)
    fun onFailure(message: String)
    fun onError(error: HashMap<String,Any>)
    fun onMonthSelected(position: Int)
    fun onItemClick(portfolio: Portfolio)
}