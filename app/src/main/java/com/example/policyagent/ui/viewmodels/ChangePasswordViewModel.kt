package com.example.policyagent.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.activities.agent.AgentDashboardActivity
import com.example.policyagent.ui.activities.client.ClientDashboardActivity
import com.example.policyagent.ui.listeners.ChangePasswordListener
import com.example.policyagent.util.*
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
                var authResponse : CommonResponse
                var userType = getPreference().getStringValue(AppConstants.USER_TYPE)
                authResponse = if(userType == AppConstants.AGENT){
                    Gson().fromJson(repository.agentChangePassword(map), CommonResponse::class.java)
                } else{
                    Gson().fromJson(repository.clientChangePassword(map), CommonResponse::class.java)
                }
                if (authResponse.status!!) {
                    authResponse.let {
                        listener?.onSuccess(it)
                        return@main
                    }
                } else {
                    when (authResponse.status_code) {
                        200 -> {
                            listener!!.onFailure(authResponse.message!!)
                        }
                        422 -> {
                            listener!!.onFailure(authResponse.message!!)
                        }
                        else -> {
                            listener!!.onLogout(authResponse.message!!)
                        }
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