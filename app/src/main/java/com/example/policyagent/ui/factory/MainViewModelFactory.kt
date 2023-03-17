package com.example.policyagent.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.ui.viewmodels.*
import com.example.policyagent.ui.viewmodels.agent.*
import com.example.policyagent.ui.viewmodels.client.*


class MainViewModelFactory(
    private val repository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SplashViewModel::class.java -> {
                SplashViewModel(repository) as T
            }
            LoginViewModel::class.java -> {
                LoginViewModel(repository) as T
            }
            ForgotPasswordViewModel::class.java -> {
                ForgotPasswordViewModel(repository) as T
            }
            ConfirmOtpViewModel::class.java -> {
                ConfirmOtpViewModel(repository) as T
            }
            CreateNewPasswordViewModel::class.java -> {
                CreateNewPasswordViewModel(repository) as T
            }
            ClientDashboardViewModel::class.java -> {
                ClientDashboardViewModel(repository) as T
            }
            ClientHomeViewModel::class.java -> {
                ClientHomeViewModel(repository) as T
            }
            ClientDocumentViewModel::class.java -> {
                ClientDocumentViewModel(repository) as T
            }
            ClientProfileViewModel::class.java -> {
                ClientProfileViewModel(repository) as T
            }
            MyInsurancePortfolioViewModel::class.java -> {
                MyInsurancePortfolioViewModel(repository) as T
            }
            ChangePasswordViewModel::class.java -> {
                ChangePasswordViewModel(repository) as T
            }
            PremiumCalendarViewModel::class.java -> {
                PremiumCalendarViewModel(repository) as T
            }
            PolicyTypeViewModel::class.java -> {
                PolicyTypeViewModel(repository) as T
            }
            LifeInsuranceViewModel::class.java -> {
                LifeInsuranceViewModel(repository) as T
            }
            ClientEditProfileViewModel::class.java -> {
                ClientEditProfileViewModel(repository) as T
            }
            ContactUsViewModel::class.java -> {
                ContactUsViewModel(repository) as T
            }
            YearlyPremiumViewModel::class.java -> {
                YearlyPremiumViewModel(repository) as T
            }
            AddNewPolicyViewModel::class.java -> {
                AddNewPolicyViewModel(repository) as T
            }
            PolicyFormViewModel::class.java -> {
                PolicyFormViewModel(repository) as T
            }
            LifeInsuranceListViewModel::class.java -> {
                LifeInsuranceListViewModel(repository) as T
            }
            HealthInsuranceListViewModel::class.java -> {
                HealthInsuranceListViewModel(repository) as T
            }
            CarInsuranceListViewModel::class.java -> {
                CarInsuranceListViewModel(repository) as T
            }
            WcInsuranceListViewModel::class.java -> {
                WcInsuranceListViewModel(repository) as T
            }
            FireInsuranceListViewModel::class.java -> {
                FireInsuranceListViewModel(repository) as T
            }
            else -> {
                SplashViewModel(repository) as T
            }
        }
    }
}