package com.example.policyagent.ui.listeners
import com.example.policyagent.data.database.entities.User
import org.json.JSONObject


interface LoginListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
    fun onError(errors: HashMap<String,Any>)
}