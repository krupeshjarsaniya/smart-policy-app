package com.example.policyagent.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.data.responses.companylist.CompanyListResponse
import com.example.policyagent.data.responses.contactus.ContactUsResponse
import com.example.policyagent.ui.listeners.ContactUsListener
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.Coroutines
import com.example.policyagent.util.NoInternetException
import com.example.policyagent.util.printLog
import com.google.gson.Gson

class ContactUsViewModel (
    private val repository: MainRepository
) : ViewModel() {

    var listener: ContactUsListener? = null

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }


    fun getContactUs(mContext: Context){
        listener?.onStarted()
        Coroutines.main {
            try {

                val response = Gson().fromJson(repository.getContactUs(), ContactUsResponse::class.java)
                if (response.success!!){
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