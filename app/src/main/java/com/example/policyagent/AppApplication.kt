package com.example.policyagent

import android.app.Application
import android.content.Context
import com.example.policyagent.data.database.AppDatabase
import com.example.policyagent.data.network.MyApi
import com.example.policyagent.data.network.NetworkConnectionInterceptor
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.data.repositories.MainRepository
import com.example.policyagent.ui.factory.MainViewModelFactory
import com.example.policyagent.util.AppSignatureHelper
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppApplication : Application(), KodeinAware {
    var mContext: Context? = null
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppApplication))
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { MainRepository(instance(), instance(), instance()) }
        bind() from provider  { MainViewModelFactory(instance()) }
    }

    fun getContext(): Context? {
        return mContext
    }

    fun setContext(context: Context?) {
        mContext = context
    }

    override fun onCreate() {
        super.onCreate()
        /*Following will generate the hash code*/
        val appSignature = AppSignatureHelper(this)
        appSignature.appSignatures

    }
}