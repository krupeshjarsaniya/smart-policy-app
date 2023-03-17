package com.example.policyagent.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.ChangePasswordListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson
import org.json.JSONObject

class ChangePasswordViewModel(
    private val repository: MainRepository
) : ViewModel() {

    var listener: ChangePasswordListener? = null

    fun getLoggedInUser() = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun onChangePassword(currentPassword: String, password: String, mContext: Context) {
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["current_password"] = currentPassword
                map["password"] = password
                val authResponse = Gson().fromJson( repository.clientChangePassword(map), CommonResponse::class.java)

                //Log.e("responsebody",authResponse.body().toString())
                if (authResponse.status!!) {
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
            } catch (e: ApiException) {
                listener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure(e.message!!)
            } catch (ex: Exception) {
                printLog("exp-->", ex.message.toString())
                listener?.onFailure(ex.message!!)
            }
        }
    }
}