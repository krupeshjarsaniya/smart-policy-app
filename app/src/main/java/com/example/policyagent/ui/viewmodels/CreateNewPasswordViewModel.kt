package com.example.policyagent.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.CreateNewPasswordListener
import com.example.policyagent.util.*
import com.google.gson.Gson

class CreateNewPasswordViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: CreateNewPasswordListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun onCreateNewPassword(mobile: String, password: String, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map.put("mobile_no", mobile)
                map.put("password", password)
                val authResponse = Gson().fromJson(repository.resetPassword(map), CommonResponse::class.java)
                if (authResponse.status!!){
                    Log.e("response",authResponse.toString())
                    authResponse.let {
                        listener?.onSuccess(it)
                        return@main
                    }
                } else {
                    if (authResponse.status_code == 200) {
                        listener?.onFailure(authResponse.message!!)
                    } else {
                        listener?.onError(authResponse.error!!)
                    }
                }
            }catch (e: ApiException){
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (ex: Exception){
                printLog("exp-->", ex.message.toString())
                listener?.onFailure(ex.message!!)
            }
        }
    }

}