package com.example.policyagent.ui.viewmodels.client

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addpolicy.AddPolicy
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.AddPolicyListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PolicyFormViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: AddPolicyListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addPolicy(addPolicy: AddPolicy, mContext: Context) {
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addPolicy)
                val map = HashMap<String, RequestBody>()
                map["model_type"] = addPolicy.model_type!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addPolicy.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addPolicy.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_name"] = addPolicy.company_name!!.toUpperCase().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["start_date"] = addPolicy.start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["end_date"] = addPolicy.end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["sa"] = addPolicy.sa!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plan_name"] = addPolicy.plan_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium"] = addPolicy.premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                if(addPolicy.document != null) {
                    val uri = Uri.fromFile(addPolicy.document)
                    var  imageBody = addPolicy.document!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "document\"; filename=\"${addPolicy.document!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.addPolicy(map), CommonResponse::class.java)
                if (response.status!!){
                    listener!!.onSuccess(response)
                } else {
                    when (response.status_code) {
                        200 -> {
                            listener!!.onFailure(response.message!!)
                        }
                        422 -> {
                            listener!!.onFailure(response.message!!)
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

}