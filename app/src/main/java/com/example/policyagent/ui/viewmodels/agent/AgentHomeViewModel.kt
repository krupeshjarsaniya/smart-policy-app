package com.example.policyagent.ui.viewmodels.agent

import androidx.lifecycle.ViewModel
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository

class AgentHomeViewModel (
    private val repository: MainRepository
) : ViewModel() {

    fun getLoggedInUser()  = repository.getUser()

    fun getPreference(): PreferenceProvider {
        return repository.getPrefernces()
    }

}