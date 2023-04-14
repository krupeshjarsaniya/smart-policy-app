package com.example.policyagent.data.responses.portfolio

data class PortfolioResponse(
    val `data`: PortfolioData? = PortfolioData(),
    val status: Boolean? = false,
    val hasmore: Boolean? = false
)