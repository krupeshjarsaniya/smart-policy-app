package com.example.policyagent.ui.listeners

import com.example.policyagent.data.responses.portfolio.Portfolio
import com.example.policyagent.data.responses.portfolio.PortfolioResponse

interface PortfolioListener {

    fun onStarted()
    fun onSuccess(data: PortfolioResponse)
    fun onFailure(message: String)
    fun onItemClick(portfolio: Portfolio)
    fun onError(errors: HashMap<String,Any>)

}