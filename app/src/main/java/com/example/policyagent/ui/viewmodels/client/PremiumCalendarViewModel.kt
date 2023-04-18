package com.example.policyagent.ui.viewmodels.client

import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.portfolio.PortfolioResponse
import com.example.policyagent.data.responses.yearlydue.YearlyDueResponse
import com.example.policyagent.ui.listeners.DueCalenderListener
import com.example.policyagent.ui.listeners.PremiumCalendarListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson

class PremiumCalendarViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: PremiumCalendarListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun getYearlyDue(memberId: String,type: String) {
            listener?.onStarted()
        Coroutines.main {
            var map = HashMap<String,Any>()
            map["member_id"] = memberId
            map["insurance_type"] = type
            try {
                val response = Gson().fromJson(repository.getYearlyDue(map), YearlyDueResponse::class.java)
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