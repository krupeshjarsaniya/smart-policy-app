package com.example.policyagent.data.requests.editlifeinsurance

import java.io.File

data class EditLifeInsurance(
    var client_id: String? = "",
    var member_id: String? = "",
    var sum_insured: String? = "",
    var policy_start_date: String? = "",
    var policy_end_date: String? = "",
    var pre_existing_decease: String? = "",
    var policy_number: String? = "",
    var company_name: String? = "",
    var plan_name: String? = "",
    var payment_mode: String? = "",
    var premium_amount: String? = "",
    var maturity_amount: String? = "",
    var policy_term: String? = "",
    var maturity_benefit: String? = "",
    var preminum_payment_term: String? = "",
    var maturity_term: String? = "",
    var yearly_bonus_amount: String? = "",
    var commision: String? = "",
    var nominee_details: String? = "",
    var additional_rider: String? = "",
    var policy_file: File? = null,
    var family: String? = "",
    var document: String? = "",
    var familayRemove: String? = "",
    var documentsRemoveDataArray: String? = "",
    var file: ArrayList<File> = ArrayList(),
    ) : java.io.Serializable