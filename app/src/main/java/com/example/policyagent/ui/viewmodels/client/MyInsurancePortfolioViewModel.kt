package com.example.policyagent.ui.viewmodels.client

import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.portfolio.PortfolioResponse
import com.example.policyagent.ui.listeners.PortfolioListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson

class MyInsurancePortfolioViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: PortfolioListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getPortFolio(page: Int, showLoader: Boolean) {
        if(showLoader) {
            listener?.onStarted()
        }
        Coroutines.main {
            var map = HashMap<String,Any>()
            map["page_no"] = page.toString()
            try {
                val response = Gson().fromJson(repository.getPortfolio(map), PortfolioResponse::class.java)
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