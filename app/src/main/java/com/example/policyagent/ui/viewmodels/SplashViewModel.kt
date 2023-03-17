package com.example.policyagent.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.util.AppConstants
import kotlinx.coroutines.*

class SplashViewModel(
    private val repository: MainRepository
) : ViewModel() {

    fun getLoggedInUser()  = repository.getUser()

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()
    init {
        GlobalScope.launch {
            delay(1000)
            mutableLiveData.postValue(SplashState.SplashActivity())
        }
    }

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

    fun isLogin():Boolean{
        var isLogin = false
        val is_remember = getPreference().getStringValue(AppConstants.IS_REMEMBER)
        if (is_remember.toString().equals("true")){
            isLogin = true
        }
        return isLogin
    }

    sealed class SplashState {
        class SplashActivity : SplashState()
    }

}