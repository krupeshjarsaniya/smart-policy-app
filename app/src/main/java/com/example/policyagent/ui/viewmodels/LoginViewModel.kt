package com.example.policyagent.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.database.entities.CURRENT_USER_ID
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.ui.listeners.LoginListener
import com.example.policyagent.util.*
import com.google.gson.Gson

class LoginViewModel(
    private val repository: MainRepository
) : ViewModel() {
    var authListener: LoginListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun onLogin(userId: String, password: String, mContext: Context){
        authListener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map.put("user_id", userId)
                map.put("password", password)

                val authResponse = Gson().fromJson(repository.userLogin(map), LoginResponse::class.java)
                if (authResponse.status!!){
                    if (authResponse.data != null) {
                        authResponse.let {
                            if (authResponse.access_token != null && !authResponse.access_token.isEmpty()) {
                                getPreference().setStringValue(
                                    AppConstants.ACCESS_HEADER,
                                    it.access_token!!
                                )
                            }
                            if (it.data != null) {
                                it.data.user_type = authResponse.user_type
                                getPreference().setStringValue(AppConstants.USER_ID, it.data.id!!.toString())
                                getPreference().setStringValue(AppConstants.USER_TYPE,it.user_type!!)
                                authListener?.onSuccess(it.data)
                                //CURRENT_USER_ID = it.data.id
                                repository.saveUser(it.data)
                            }
                            return@main
                        }
                    }} else {
                    if (authResponse.status_code == 200) {
                        authListener!!.onFailure(authResponse.message!!)
                    } else {
                        authListener!!.onError(authResponse.error!!)
                    }
                }
            }catch (e: ApiException){
                authListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }catch (ex: Exception){
                printLog("exp-->", ex.message.toString())
                authListener?.onFailure(ex.message!!)
            }
        }
    }

}