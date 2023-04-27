package com.example.policyagent.data.requests.addclient

import java.io.File

data class AddClient(
    var firstname: String? = "",
    var middlename: String? = "",
    var lastname: String? = "",
    var mobile: String? = "",
    var email: String? = "",
    var address: String? = "",
    var city: String? = "",
    var state: String? = "",
    var birthdate: String? = "",
    var gender: String? = "",
    var height: String? = "",
    var weight: String? = "",
    var age: String? = "",
    var marital_status: String? = "",
    var c_pan_number: String? = "",
    var gst_number: String? = "",
    var family: String? = "",
    var document: String = "",
    var file: ArrayList<File> = ArrayList(),
) : java.io.Serializable