package com.example.policyagent.ui.viewmodels.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.ClientProfileListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson

class ClientProfileViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: ClientProfileListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }
    fun onLogout(mContext: Context) {
        listener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = Gson().fromJson(repository.clientLogout(), CommonResponse::class.java)
                if (authResponse.status!!){
                    Log.e("response",authResponse.toString())
                    authResponse.let {
                        listener?.onSuccess(it)
                        return@main
                    }
                } else {
                    listener?.onFailure(authResponse.message!!)
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