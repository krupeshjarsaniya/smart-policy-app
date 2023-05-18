package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.expiredpolicylist.ExpiredPolicyResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse
import com.example.policyagent.ui.listeners.ExpiredListListener
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.RequestBody

class ExpiredPolicyListViewModel (
    private val repository: MainRepository
) : ViewModel() {
    var listener: ExpiredListListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getExpiredInsurance(mContext: Context,  page: Int){
            listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["page_no"] = page.toString()
                val response = Gson().fromJson(repository.getExpiredPolicy(map), ExpiredPolicyResponse::class.java)
                if (response.status!!){
                    if (response.insurance != null) {
                        response.let {
                            listener!!.onSuccess(it)
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

    fun getUpcomingInsurance(mContext: Context,  page: Int){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["page_no"] = page.toString()
                val response = Gson().fromJson(repository.getUpcomingExpirePolicy(map), ExpiredPolicyResponse::class.java)
                if (response.status!!){
                    if (response.insurance != null) {
                        response.let {
                            listener!!.onSuccess(it)
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

    fun getPaymentDueInsurance(mContext: Context,  page: Int){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["page_no"] = page.toString()
                val response = Gson().fromJson(repository.getPaymentDueInsurance(map), ExpiredPolicyResponse::class.java)
                if (response.status!!){
                    if (response.insurance != null) {
                        response.let {
                            listener!!.onSuccess(it)
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

    fun getUpcomingPaymentInsurance(mContext: Context,  page: Int){
        listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["page_no"] = page.toString()
                val response = Gson().fromJson(repository.getUpcomingPaymentInsurance(map), ExpiredPolicyResponse::class.java)
                if (response.status!!){
                    if (response.insurance != null) {
                        response.let {
                            listener!!.onSuccess(it)
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