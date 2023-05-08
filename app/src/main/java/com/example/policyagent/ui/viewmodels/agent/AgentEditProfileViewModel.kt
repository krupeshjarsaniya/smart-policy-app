package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.editAgentProfile.EditAgentProfile
import com.example.policyagent.data.requests.editClientProfile.EditClientProfile
import com.example.policyagent.data.requests.editclient.EditClient
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.ui.listeners.EditClientProfileListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AgentEditProfileViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: EditClientProfileListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }


    fun editClientProfile(editClient: EditAgentProfile, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(editClient)
                Log.e("ediclientrequest",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()
                map["first_name"] = editClient.firstname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["middle_name"] = editClient.middlename!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["last_name"] = editClient.lastname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["email"] = editClient.email!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                //map["mobile"] = editClient.mobile!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["address"] = editClient.address!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["city"] = editClient.city!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["state"] = editClient.state!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["birthdate"] = editClient.birthdate!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gender"] = editClient.gender!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plantype"] = editClient.plantype!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["c_pan_number"] = editClient.c_pan_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst_number"] = editClient.gst_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val response = Gson().fromJson(repository.editAgentProfile(map), LoginResponse::class.java)
                if (response.status!!){
                    listener!!.onSuccess(response)
                    repository.saveUser(response.data!!)
                } else {
                    if (response.status){
                        listener!!.onSuccess(response)
                    } else {
                        when (response.status_code) {
                            200 -> {
                                listener!!.onFailure(response.message!!)
                            }
                            422 -> {
                                listener!!.onError(response.error!!)
                            }
                            else -> {
                                listener!!.onLogout(response.message!!)
                            }
                        }
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


    fun getState(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val response = Gson().fromJson(repository.getState(), StateListResponse::class.java)
                if (response.status!!){
                    if (response.data != null) {
                        response.let {
                            val gson = Gson()
                            val json = gson.toJson(it)
                            Log.e("response","response"+json.toString())
                            listener!!.onSuccessState(it)
                            return@main
                        }
                    }}
                listener!!.onFailure("Something Went Wrong")
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