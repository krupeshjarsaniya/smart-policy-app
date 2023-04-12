package com.example.policyagent.data.network

import android.app.Application
import com.example.policyagent.ui.activities.BaseActivity
import com.example.policyagent.ui.activities.BaseActivity.Companion.baseActivity
import com.example.policyagent.ui.activities.LoginActivity
import com.example.policyagent.ui.activities.LoginSuccessActivity
import com.example.policyagent.util.ApiException
import com.example.policyagent.util.AppConstants
import com.example.policyagent.util.launchActivity
import com.example.policyagent.util.launchLoginActivity
import com.google.gson.JsonParser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    @ExperimentalCoroutinesApi
    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            try {
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let {
                    if(response.code() == 422){
                        val jsonParser = JsonParser()
                        val json = jsonParser.parse(it)
                        return json as T
                    }
                    if(response.code() == 401){
                        val jsonParser = JsonParser()
                        val json = jsonParser.parse(it)
                        return json as T
                    }
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) {
                    }
                    message.append("\n")
                }
                message.append("Error Code: ${response.code()}")
                    throw ApiException(message.toString())
            }catch (ex : Exception){
                ex.printStackTrace()
                if (ex.message != null && !ex.message!!.isEmpty()){
                    throw ApiException(ex.message.toString())
                }

            }
        }
        throw ApiException("Unable to connect with server")
    }
}