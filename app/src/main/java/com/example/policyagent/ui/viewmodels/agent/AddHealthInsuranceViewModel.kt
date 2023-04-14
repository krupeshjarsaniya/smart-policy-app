package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addhealthinsurance.AddHealthInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.AddHealthInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddHealthInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddHealthInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addHealthInsurance(addHealthInsurance: AddHealthInsurance, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(addHealthInsurance)
                Log.e("HealthInsurancerequest",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()
                map["client_id"] = addHealthInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addHealthInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["insurance_type"] = addHealthInsurance.insurance_type!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["st"] = addHealthInsurance.st!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_start_date"] = addHealthInsurance.risk_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["risk_end_date"] = addHealthInsurance.risk_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["family"] = addHealthInsurance.family!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["ped"] = addHealthInsurance.pre_existing_decease!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["bonus"] = addHealthInsurance.bonus!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addHealthInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_id"] = addHealthInsurance.company_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plan_name"] = addHealthInsurance.plan_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["payment_mode"] = addHealthInsurance.payment_mode!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = addHealthInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_term"] = addHealthInsurance.policy_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["waiting"] = addHealthInsurance.waiting!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["sum_insured"] = addHealthInsurance.sum_insured!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["total_sum_insured"] = addHealthInsurance.total_sum_insured!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = addHealthInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addHealthInsurance.document.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until addHealthInsurance.file.size) {
                    val uri = Uri.fromFile(addHealthInsurance.file[i])
                    var  imageBody = addHealthInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${addHealthInsurance.file[i].name}\"",imageBody)
                }

                if(addHealthInsurance.policy_file != null) {
                    val uri = Uri.fromFile(addHealthInsurance.policy_file)
                    var  imageBody = addHealthInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "policy_file\"; filename=\"${addHealthInsurance.policy_file!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.addHealthInsurance(map), CommonResponse::class.java)
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

}