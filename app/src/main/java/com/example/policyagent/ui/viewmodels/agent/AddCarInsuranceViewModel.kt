package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addcarinsurance.AddCarInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.clientlist.ClientListResponse
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.ui.listeners.AddCarInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddCarInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddCarInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addCarInsurance(addCarInsurance: AddCarInsurance, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addCarInsurance)
                Log.e("CarInsurancerequest",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()
                map["client_id"] = addCarInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addCarInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["insurance_type"] = addCarInsurance.insurance_type!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["insurance_sub_type"] = addCarInsurance.insurance_sub_type!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["registration_number_rto"] = addCarInsurance.registration_number_rto!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_start_date"] = addCarInsurance.risk_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_end_date"] = addCarInsurance.risk_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["idv_vehical_value"] = addCarInsurance.idv_vehical_value!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["no_claim_bonus"] = addCarInsurance.no_claim_bonus!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["discount"] = addCarInsurance.discount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addCarInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_id"] = addCarInsurance.company_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plan_name"] = addCarInsurance.plan_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = addCarInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["seating_capacity"] = addCarInsurance.seating_capacity!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gvw"] = addCarInsurance.gvw!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["claim_details"] = addCarInsurance.claim_details!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["own_damage_premium"] = addCarInsurance.own_damage_premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["tp_premium"] = addCarInsurance.tp_premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["net_preminum"] = addCarInsurance.net_preminum!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["gst"] = addCarInsurance.gst!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["total_premium"] = addCarInsurance.total_premium!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_type"] = addCarInsurance.premium_type!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = addCarInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addCarInsurance.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until addCarInsurance.file.size) {
                    val uri = Uri.fromFile(addCarInsurance.file[i])
                    var  imageBody = addCarInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${addCarInsurance.file[i].name}\"",imageBody)
                }

                if(addCarInsurance.policy_file != null) {
                    val uri = Uri.fromFile(addCarInsurance.policy_file)
                    var  imageBody = addCarInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "policy_file\"; filename=\"${addCarInsurance.policy_file!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.addCarInsurance(map), CommonResponse::class.java)
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