package com.example.policyagent.data.requests.editclient

import java.io.File

class EditClient(
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
    var password: String = "",
    var file: ArrayList<File> = ArrayList(),
    var familayRemove: String? = "",
    var documentsRemoveDataArray: String? = "",
) : java.io.Serializable