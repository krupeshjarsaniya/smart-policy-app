package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.yearlydue.YearlyDueResponse

interface PremiumCalendarListener {
    fun onStarted()
    fun onSuccess(response: YearlyDueResponse)
    fun onFailure(message: String)
    fun onError(error: HashMap<String,Any>)
    fun onYearSelected(year: String)
}