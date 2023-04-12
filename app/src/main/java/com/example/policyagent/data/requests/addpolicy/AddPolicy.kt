package com.example.policyagent.data.requests.addpolicy

import java.io.File

data class AddPolicy(
    var model_type: String? = "",
    var member_id: String? = "",
    var policy_number: String? = "",
    var company_name: String? = "",
    var start_date: String? = "",
    var end_date: String? = "",
    var sa: String? = "",
    var plan_name: String? = "",
    var premium: String? = "",
    var document: File? = null,
) : java.io.Serializable