package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addclient.AddClient
import com.example.policyagent.data.requests.addlifeinsurance.AddLifeInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.ui.listeners.AddClientListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AddClientViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: AddClientListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addClient(addClient: AddClient, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addClient)
                Log.e("addclient",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()

                map["firstname"] = addClient.firstname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["middlename"] = addClient.middlename!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["lastname"] = addClient.lastname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["mobile"] = addClient.mobile!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["email"] = addClient.email!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["family"] = addClient.family!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["address"] = addClient.address!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["city"] = addClient.city!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["state"] = addClient.state!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["birthdate"] = addClient.birthdate!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gender"] = addClient.gender!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["height"] = addClient.height!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["weight"] = addClient.weight!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["age"] = addClient.age!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["marital_status"] = addClient.marital_status!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["c_pan_number"] = addClient.c_pan_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst_number"] = addClient.gst_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addClient.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until addClient.file.size) {
                    val uri = Uri.fromFile(addClient.file[i])
                    var  imageBody = addClient.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${addClient.file[i].name}\"",imageBody)
                }


                val response = Gson().fromJson(repository.addClient(map), CommonResponse::class.java)
                if (response.status!!){
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