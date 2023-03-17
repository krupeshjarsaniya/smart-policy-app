package com.example.policyagent.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.ForgotPasswordListener
import com.example.policyagent.ui.listeners.VerifyOtpListener
import com.example.policyagent.util.*
import com.google.gson.Gson

class ConfirmOtpViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: VerifyOtpListener? = null


    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun onVerifyOTP(otp: String,mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map.put("otp", otp)
                val authResponse = Gson().fromJson(repository.verifyOTP(map), CommonResponse::class.java)

                if (authResponse.status!!){
                    authResponse.let {
                        listener?.onSuccess(it,AppConstants.VERIFY)
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

    fun onForgotPassword(userId: String,mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map.put("user_id", userId)
                val authResponse = Gson().fromJson(repository.forgotPassword(map), CommonResponse::class.java)

                if (authResponse.status!!){
                    authResponse.let {
                        listener?.onSuccess(it,AppConstants.RESEND)
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
