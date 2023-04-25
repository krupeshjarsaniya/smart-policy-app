package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addhealthinsurance.AddHealthInsurance
import com.example.policyagent.data.requests.addwcinsurance.AddWcInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.ui.listeners.AddWcInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddWcInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddWcInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addWcInsurance(addWcInsurance: AddWcInsurance, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addWcInsurance)
                val map = HashMap<String, RequestBody>()
                map["client_id"] = addWcInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addWcInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["st"] = addWcInsurance.st!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_start_date"] = addWcInsurance.risk_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_end_date"] = addWcInsurance.risk_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["st_with_me"] = addWcInsurance.st_with_me!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["no_of"] = addWcInsurance.no_of!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addWcInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_id"] = addWcInsurance.company_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = addWcInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["net_preminum"] = addWcInsurance.net_preminum!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst"] = addWcInsurance.gst!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["total_premium"] = addWcInsurance.total_premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = addWcInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addWcInsurance.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until addWcInsurance.file.size) {
                    val uri = Uri.fromFile(addWcInsurance.file[i])
                    var  imageBody = addWcInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${addWcInsurance.file[i].name}\"",imageBody)
                }

                if(addWcInsurance.policy_file != null) {
                    val uri = Uri.fromFile(addWcInsurance.policy_file)
                    var  imageBody = addWcInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "policy_file\"; filename=\"${addWcInsurance.policy_file!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.addWcInsurance(map), CommonResponse::class.java)
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