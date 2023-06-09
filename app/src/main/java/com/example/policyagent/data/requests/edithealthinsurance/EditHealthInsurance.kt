package com.example.policyagent.data.requests.edithealthinsurance

import java.io.File

data class EditHealthInsurance(
    var client_id: String? = "",
    var member_id: String? = "",
    var insurance_type: String? = "",
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
    var net_premium: String = "",
    var gst: String = "",
    var file: ArrayList<File> = ArrayList(),
    var familayRemove: String? = "",
    var documentsRemoveDataArray: String? = "",
    ) : java.io.Serializable