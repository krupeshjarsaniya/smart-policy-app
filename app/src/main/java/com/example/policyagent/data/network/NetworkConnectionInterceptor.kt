package com.example.policyagent.data.network
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.policyagent.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response


class NetworkConnectionInterceptor(
    context: Context
) : Interceptor, Exception() {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoInternetException("Make sure you have an active internet connection")
        }
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Request-Type", "Android")
            .addHeader("Content-Type", "application/json")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }

}