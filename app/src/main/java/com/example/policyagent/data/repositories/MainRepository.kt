package com.example.policyagent.data.repositories
import com.example.policyagent.data.database.AppDatabase
import com.example.policyagent.data.database.entities.User
import com.example.policyagent.data.network.MyApi
import com.example.policyagent.data.network.SafeApiRequest
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.util.AppConstants
import com.google.gson.JsonObject
import java.util.*

class MainRepository(private val api: MyApi, private val db: AppDatabase, private val appPrefrences: PreferenceProvider) : SafeApiRequest() {


    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()

    fun deleteUser() = db.getUserDao().deleteUser()

    fun getPrefernces():PreferenceProvider{
        return appPrefrences
    }

    /*fun getLanguage():String{
        val language = getPrefernces().getStringValue(AppConstants.LANGUAGE)
        var mainLanguage = AppConstants.LANGUAGE_ENGLISH
        if (language != null && !language.isEmpty()){
            mainLanguage = language
        }

        return mainLanguage
    }*/

    fun getServerHeader(): String {
        var urlServerHeader = AppConstants.getSevserverHeader()
        return urlServerHeader
    }


    suspend fun userLogin(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.userLogin(getHeaderMap(),map) }
    }

    suspend fun forgotPassword(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.forgotPassword(getHeaderMap(),map) }
    }

    suspend fun verifyOTP(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.verifyOTP(getHeaderMap(),map) }
    }

    suspend fun resetPassword(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.resetPassword(getHeaderMap(),map) }
    }

    suspend fun clientChangePassword(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.clientChangePassword(getHeaderMap(),map)}
    }

    suspend fun clientLogout(): JsonObject {
        return apiRequest { api.clientLogout(getHeaderMap()) }
    }

    suspend fun getLifeInsurance(): JsonObject {
        return apiRequest { api.getLifeInsurance(getHeaderMap()) }
    }

    suspend fun getHealthInsurance(): JsonObject {
        return apiRequest { api.getHealthInsurance(getHeaderMap()) }
    }

    suspend fun getCarInsurance(): JsonObject {
        return apiRequest { api.getCarInsurance(getHeaderMap()) }
    }

    suspend fun getWcInsurance(): JsonObject {
        return apiRequest { api.getWcInsurance(getHeaderMap()) }
    }

    suspend fun getFireInsurance(): JsonObject {
        return apiRequest { api.getFireInsurance(getHeaderMap()) }
    }

    fun getHeaderMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[AppConstants.Accept] = "application/json"
        map[AppConstants.AUTHORIZATION] = getPrefernces().getStringValue(AppConstants.ACCESS_HEADER)!!
        return map
    }
}