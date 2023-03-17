package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.healthinsurancelist.HealthInsuranceListResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.ui.listeners.HealthInsuranceListListener
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.ui.listeners.LoginListener
import com.example.policyagent.util.*
import com.google.gson.Gson

class HealthInsuranceListViewModel (
    private val repository: MainRepository
) : ViewModel() {
    var listener: HealthInsuranceListListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getHealthInsurance(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {

                val response = Gson().fromJson(repository.getHealthInsurance(), HealthInsuranceListResponse::class.java)
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

}