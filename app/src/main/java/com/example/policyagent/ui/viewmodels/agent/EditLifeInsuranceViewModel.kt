package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.requests.editlifeinsurance.EditLifeInsurance
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.ui.listeners.AddLifeInsuranceListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditLifeInsuranceViewModel (
    private val repository: MainRepository
) : ViewModel() {
    
    var listener: AddLifeInsuranceListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun editLifeInsurance(editLifeInsurance: EditLifeInsurance, id: String, mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                var gson = Gson()
                val json = gson.toJson(editLifeInsurance)
                Log.e("lifeinsurancerequest",json!!.replace("\\",""))
                val map = HashMap<String, RequestBody>()
                map["client_id"] = editLifeInsurance.client_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["member_id"] = editLifeInsurance.member_id!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["sum_insured"] = editLifeInsurance.sum_insured!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_start_date"] = editLifeInsurance.policy_start_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_end_date"] = editLifeInsurance.policy_end_date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["family"] = editLifeInsurance.family!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["pre_existing_decease"] = editLifeInsurance.pre_existing_decease!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_number"] = editLifeInsurance.policy_number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["company_name"] = editLifeInsurance.company_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["plan_name"] = editLifeInsurance.plan_name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["payment_mode"] = editLifeInsurance.payment_mode!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["premium_amount"] = editLifeInsurance.premium_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_amount"] = editLifeInsurance.maturity_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["policy_term"] = editLifeInsurance.policy_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_benefit"] = editLifeInsurance.maturity_benefit!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["preminum_payment_term"] = editLifeInsurance.preminum_payment_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["maturity_term"] = editLifeInsurance.maturity_term!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["yearly_bonus_amount"] = editLifeInsurance.yearly_bonus_amount!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["commision"] = editLifeInsurance.commision!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["nominee_details"] = editLifeInsurance.nominee_details!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["additional_rider"] = editLifeInsurance.additional_rider!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["additional_rider"] = editLifeInsurance.additional_rider!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["familayRemove"] = editLifeInsurance.familayRemove!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["documentsRemoveDataArray"] = editLifeInsurance.documentsRemoveDataArray!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                map["document"] = editLifeInsurance.document!!.replace("\\","").toRequestBody("multipart/form-data".toMediaTypeOrNull())
                for(i in 0 until editLifeInsurance.file.size) {
                    val uri = Uri.fromFile(editLifeInsurance.file[i])
                    var  imageBody = editLifeInsurance.file[i].asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put("file[$i]\"; filename=\"${editLifeInsurance.file[i].name}\"",imageBody)
                }

                if(editLifeInsurance.policy_file != null) {
                    val uri = Uri.fromFile(editLifeInsurance.policy_file)
                    var  imageBody = editLifeInsurance.policy_file!!.asRequestBody(mContext.contentResolver.getType(uri)?.let {
                        it.toMediaTypeOrNull()
                    })
                    map.put(
                        "policy_file\"; filename=\"${editLifeInsurance.policy_file!!.name}\" ",
                        imageBody
                    )
                }
                //map["policy_file[0]"] = reqFile

                val response = Gson().fromJson(repository.editLifeInsurance(map,id), CommonResponse::class.java)
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
                                listener!!.onFailure(response.message!!)
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

}