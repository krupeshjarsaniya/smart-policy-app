package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.addlifeinsurance.AddLifewInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.AddLifeInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddLifeInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddLifeInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun addLifeInsurance(addLifeInsurance: AddLifewInsurance, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, RequestBody>()
                map["client_id"] = addLifeInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = addLifeInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["sum_insured"] = addLifeInsurance.sum_insured!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_start_date"] = addLifeInsurance.policy_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_end_date"] = addLifeInsurance.policy_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["family"] = addLifeInsurance.family!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["pre_existing_decease"] = addLifeInsurance.pre_existing_decease!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = addLifeInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_name"] = addLifeInsurance.company_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plan_name"] = addLifeInsurance.plan_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["payment_mode"] = addLifeInsurance.payment_mode!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = addLifeInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_amount"] = addLifeInsurance.maturity_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_term"] = addLifeInsurance.policy_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_benefit"] = addLifeInsurance.maturity_benefit!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["preminum_payment_term"] = addLifeInsurance.preminum_payment_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_term"] = addLifeInsurance.maturity_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["yearly_bonus_amount"] = addLifeInsurance.yearly_bonus_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = addLifeInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["nominee_details"] = addLifeInsurance.nominee_details!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["additional_rider"] = addLifeInsurance.additional_rider!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = addLifeInsurance.document.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                for(i in 0 until addLifeInsurance.file.size) {
                    val uri = Uri.fromFile(addLifeInsurance.file[i])
                    map["file[$i]"] = addLifeInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                }
                val uri = Uri.fromFile(addLifeInsurance.policy_file)
                map["policy_file"] = addLifeInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                    it.toMediaTypeOrNull()
                })

                val response = Gson().fromJson(repository.addLifeInsurance(map), CommonResponse::class.java)
                if (response.status!!){
                    listener!!.onSuccess(response)
                    } else {
                    if (response.status_code == 200) {
                        listener!!.onFailure(response.message!!)
                    } else {
                        listener!!.onError(response.error!!)
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