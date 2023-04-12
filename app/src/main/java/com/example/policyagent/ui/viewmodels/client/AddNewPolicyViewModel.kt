package com.example.policyagent.ui.viewmodels.client

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.policylist.PolicyListResponse
import com.example.policyagent.ui.listeners.PolicyListListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson

class AddNewPolicyViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: PolicyListListener? = null


    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getPolicyList(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val response = Gson().fromJson(repository.getPolicyList(), PolicyListResponse::class.java)
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



    fun deletePolicy(mContext: Context, id: String, position: Int){
        listener?.onStarted()
        Coroutines.main {
            try {
                val response = Gson().fromJson(repository.deletePolicy(id), CommonResponse::class.java)
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