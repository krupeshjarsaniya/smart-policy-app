package com.example.policyagent.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity(tableName = "user")
data class User(
    val address: String? = "",
    val age: String? = "",
    val birthdate: String? = "",
    val password: String? = "",
    val city: String? = "",
    val client_id: String? = "",
    val email: String? = "",
    val mobile: String? = "",
    val firstname: String? = "",
    val gender: String? = "",
    val height: String? = "",
    val id: Int? = 0,
    val lastname: String? = "",
    val marital_status: String? = "",
    val middlename: String? = "",
    val relationship: String? = "",
    val pan_number: String? = "",
    val plantype: String? = "",
    val gst: String? = "",
    val state: String? = "",
    val status: String? = "",
    val weight: String? = "",
    var user_type: String? = ""
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int? = CURRENT_USER_ID
}