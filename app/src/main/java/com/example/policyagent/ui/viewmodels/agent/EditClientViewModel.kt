package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.editclient.EditClient
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.statelist.StateListResponse
import com.example.policyagent.ui.listeners.AddClientListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditClientViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddClientListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun editClient(editClient: EditClient, id: String, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(editClient)
                Log.e("ediclientrequest",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()
                map["firstname"] = editClient.firstname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["middlename"] = editClient.middlename!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["lastname"] = editClient.lastname!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["mobile"] = editClient.mobile!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["email"] = editClient.email!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["family"] = editClient.family!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["address"] = editClient.address!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["city"] = editClient.city!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["state"] = editClient.state!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["birthdate"] = editClient.birthdate!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gender"] = editClient.gender!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["height"] = editClient.height!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["weight"] = editClient.weight!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["age"] = editClient.age!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["marital_status"] = editClient.marital_status!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["c_pan_number"] = editClient.c_pan_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst_number"] = editClient.gst_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["password"] = editClient.password!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = editClient.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["familayRemove"] = editClient.familayRemove!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["documentsRemoveDataArray"] = editClient.documentsRemoveDataArray!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until editClient.file.size) {
                    val uri = Uri.fromFile(editClient.file[i])
                    var  imageBody = editClient.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${editClient.file[i].name}\"",imageBody)
                }
                
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.editClient(map,id), CommonResponse::class.java)
                if (response.status!!){
                    listener!!.onSuccess(response)
                    } else {
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