package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.util.*
import com.google.gson.Gson
import okhttp3.RequestBody

class LifeInsuranceListViewModel (
    private val repository: MainRepository
) : ViewModel() {
    var listener: LifeInsuranceListListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getLifeInsurance(mContext: Context,  page: Int){
            listener?.onStarted()
        Coroutines.main {
            try {
                val map = HashMap<String, Any>()
                map["page_no"] = page.toString()
                val response = Gson().fromJson(repository.getLifeInsurance(map), LifeInsuranceListResponse::class.java)
                if (response.status!!){
                    if (response.data != null) {
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

    fun deleteLifeInsurance(mContext: Context, id: String, position: Int){
        listener?.onStarted()
        Coroutines.main {
            try {
                val response = Gson().fromJson(repository.deleteLifeInsurance(id), CommonResponse::class.java)
                if (response.status!!){
                        response.let {
                            listener!!.onSuccessDelete(it,position)
                            return@main
                        }
                    }
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