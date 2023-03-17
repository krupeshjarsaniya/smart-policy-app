package com.example.policyagent.ui.fragments

import androidx.fragment.app.Fragment
import com.example.policyagent.ui.activities.BaseActivity
import java.lang.Exception

open class BaseFragment : Fragment() {


    fun showMessage(message: String) {
        (context as BaseActivity).showToastMessage(message)
    }

    fun showToastMessage(message: String) {
        (context as BaseActivity).showToastMessage(message)
    }

    fun showProgress(isShow: Boolean) {
        try {
            (context as BaseActivity).showProgress(isShow)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun hideProgress() {
        try {
            (context as BaseActivity).hideProgress()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getActivityIndtance(): BaseActivity {
        return BaseActivity.baseActivity
    }

}