package com.example.policyagent.data.requests.addfireinsurance

import java.io.File

data class AddFireInsurance(
    var client_id: String? = "",
    var member_id: String? = "",
    var fire_policy_type: String? = "",
    var st: String? = "",
    var risk_start_date: String? = "",
    var risk_end_date: String? = "",
    var policy_number: String? = "",
    var company_id: String? = "",
    var premium_amount: String? = "",
    var net_preminum: String? = "",
    var gst: String? = "",
    var total_premium: String? = "",
    var commision: String? = "",
    var policy_file: File? = null,
    var document: String = "",
    var file: ArrayList<File> = ArrayList(),
    ) : java.io.Serializable