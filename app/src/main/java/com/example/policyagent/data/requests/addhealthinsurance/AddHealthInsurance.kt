package com.example.policyagent.data.requests.addhealthinsurance

import java.io.File

data class AddHealthInsurance(
    var client_id: String? = "",
    var member_id: String? = "",
    var insurance_type: String? = "SINGLE",
    var st: String? = "",
    var risk_start_date: String? = "",
    var risk_end_date: String? = "",
    var pre_existing_decease: String? = "",
    var bonus: String? = "",
    var policy_number: String? = "",
    var policy_term: String? = "",
    var company_id: String? = "",
    var plan_name: String? = "",
    var payment_mode: String? = "",
    var premium_amount: String? = "",
    var waiting: String? = "",
    var sum_insured: String? = "",
    var total_sum_insured: String? = "",
    var commision: String? = "",
    var policy_file: File? = null,
    var family: String? = "",
    var document: String = "",
    var gst: String = "",
    var net_premium: String = "",
    var file: ArrayList<File> = ArrayList(),
    ) : java.io.Serializable