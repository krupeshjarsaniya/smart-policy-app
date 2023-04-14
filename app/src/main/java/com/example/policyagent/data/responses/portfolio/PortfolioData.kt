package com.example.policyagent.data.responses.portfolio

import com.example.policyagent.data.responses.commoninsurance.ClientPersonalDetails

data class PortfolioData(
    val client: ArrayList<ClientPersonalDetails?>? = arrayListOf(),
    val portfolio: ArrayList<Portfolio?>? = arrayListOf()
)