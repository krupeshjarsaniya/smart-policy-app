package com.example.policyagent.ui.viewmodels.client

import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository

class YearlyPremiumViewModel (
    private val repository: MainRepository
) : ViewModel() {

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

}