package com.example.policyagent.data.responses

data class MemberModel (
    var family_id: String? = "",
    var first_name: String? = "",
    var last_name: String? = "",
    var birth_date: String? = "",
    var f_gender: String? = "",
    var f_height: String? = "",
    var f_weight: String? = "",
    var f_age: String? = "",
    var relationship: String? = "",
    var pan: String? = "",
) : java.io.Serializable