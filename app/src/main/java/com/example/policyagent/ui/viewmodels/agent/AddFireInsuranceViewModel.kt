package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addfireinsurance.AddFireInsurance
import com.example.policyagent.data.requests.addhealthinsurance.AddHealthInsurance
import com.example.policyagent.data.requests.addwcinsurance.AddWcInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.ui.listeners.AddFireInsuranceListener
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.ui.listeners.AddWcInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddFireInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddFireInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addFireInsurance(addFireInsurance: AddFireInsurance, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addFireInsurance)
                val map = HashMap<String, RequestBody>()
                map["client_id"] = addFireInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addFireInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["st"] = addFireInsurance.st!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["fire_policy_type"] = addFireInsurance.fire_policy_type!!.toUpperCase().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_start_date"] = addFireInsurance.risk_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_end_date"] = addFireInsurance.risk_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addFireInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_id"] = addFireInsurance.company_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = addFireInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["net_preminum"] = addFireInsurance.net_preminum!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst"] = addFireInsurance.gst!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["total_premium"] = addFireInsurance.total_premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = addFireInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addFireInsurance.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until addFireInsurance.file.size) {
                    val uri = Uri.fromFile(addFireInsurance.file[i])
                    var  imageBody = addFireInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${addFireInsurance.file[i].name}\"",imageBody)
                }

                if(addFireInsurance.policy_file != null) {
                    val uri = Uri.fromFile(addFireInsurance.policy_file)
                    var  imageBody = addFireInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "policy_file\"; filename=\"${addFireInsurance.policy_file!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.addFireInsurance(map), CommonResponse::class.java)
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

    fun getClients(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {

                val response = Gson().fromJson(repository.getClients(), ClientListResponse::class.java)
                if (response.status!!){
                    if (response.data != null) {
                        response.let {
                            listener!!.onSuccessClient(it)
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

    fun getCompanies(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {

                val response = Gson().fromJson(repository.getCompanies(), CompanyListResponse::class.java)
                if (response.status!!){
                    if (response.data != null) {
                        response.let {
                            listener!!.onSuccessCompany(it)
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