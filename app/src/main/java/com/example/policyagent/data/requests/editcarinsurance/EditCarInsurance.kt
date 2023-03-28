package com.example.policyagent.data.requests.editcarinsurance

import java.io.File

data class EditCarInsurance(
    var client_id: String? = "",
    var member_id: String? = "",
    var insurance_type: String? = "",
    var insurance_sub_type: String? = "",
    var premium_type: String? = "",
    var registration_number_rto: String? = "",
    var risk_start_date: String? = "",
    var risk_end_date: String? = "",
    var idv_vehical_value: String? = "",
    var no_claim_bonus: String? = "",
    var discount: String? = "",
    var policy_number: String? = "",
    var company_id: String? = "",
    var plan_name: String? = "",
    var premium_amount: String? = "",
    var seating_capacity: String? = "",
    var gvw: String? = "",
    var claim_details: String? = "",
    var own_damage_premium: String? = "",
    var tp_premium: String? = "",
    var net_preminum: String? = "",
    var gst: String? = "",
    var total_premium: String? = "",
    var policy_file: File? = null,
    var commision: String? = "",
    var document: String = "",
    var file: ArrayList<File> = ArrayList(),
    var documentsRemoveDataArray: String? = "",
    ) : java.io.Serializable