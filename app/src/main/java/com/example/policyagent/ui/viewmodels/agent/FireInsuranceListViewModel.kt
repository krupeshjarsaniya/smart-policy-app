package com.example.policyagent.ui.viewmodels.agent

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.fireinsurancelist.FireInsuranceListResponse
import com.example.policyagent.data.responses.lifeinsurancelist.LifeInsuranceListResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.ui.listeners.FireInsuranceListListener
import com.example.policyagent.ui.listeners.LifeInsuranceListListener
import com.example.policyagent.ui.listeners.LoginListener
import com.example.policyagent.util.*
import com.google.gson.Gson

class FireInsuranceListViewModel (
    private val repository: MainRepository
) : ViewModel() {
    var listener: FireInsuranceListListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getFireInsurance(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {
                val response = Gson().fromJson(repository.getFireInsurance(), FireInsuranceListResponse::class.java)
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