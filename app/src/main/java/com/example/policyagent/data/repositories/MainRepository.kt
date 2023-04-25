package com.example.policyagent.data.repositories
import com.example.policyagent.data.database.AppDatabase
import com.example.policyagent.data.database.entities.User
import com.example.policyagent.data.network.MyApi
import com.example.policyagent.data.network.SafeApiRequest
import com.example.policyagent.data.preferences.PreferenceProvider
import com.example.policyagent.util.AppConstants
import com.google.gson.JsonObject
import okhttp3.RequestBody
import kotlin.collections.HashMap

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

    suspend fun agentChangePassword(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.agentChangePassword(getHeaderMap(),map)}
    }

    suspend fun clientLogout(): JsonObject {
        return apiRequest { api.clientLogout(getHeaderMap()) }
    }

    /*suspend fun getLifeInsurance(): JsonObject {
        return apiRequest { api.getLifeInsurance(getHeaderMap()) }
    }*/

    suspend fun getLifeInsurance(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getLifeInsurance(getHeaderMap(),map) }
    }

    suspend fun getHealthInsurance(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getHealthInsurance(getHeaderMap(),map) }
    }

    suspend fun getCarInsurance(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getCarInsurance(getHeaderMap(),map) }
    }

    suspend fun getAgentDashboard(): JsonObject {
        return apiRequest { api.getAgentDashboard(getHeaderMap()) }
    }

    suspend fun getWcInsurance(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getWcInsurance(getHeaderMap(),map) }
    }

    suspend fun getFireInsurance(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getFireInsurance(getHeaderMap(),map) }
    }

    suspend fun getClients(): JsonObject {
        return apiRequest { api.getClientList(getHeaderMap()) }
    }

    suspend fun getMembers(): JsonObject {
        return apiRequest { api.getMemberList(getHeaderMap()) }
    }

    suspend fun getCompanies(): JsonObject {
        return apiRequest { api.getCompanyList(getHeaderMap()) }
    }

    suspend fun addLifeInsurance(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addLifeInsurance(getHeaderMap(),map) }
    }

    suspend fun addHealthInsurance(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addHealthInsurance(getHeaderMap(),map) }
    }

    suspend fun addCarInsurance(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addCarInsurance(getHeaderMap(),map) }
    }

    suspend fun addWcInsurance(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addWcInsurance(getHeaderMap(),map) }
    }

    suspend fun addFireInsurance(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addFireInsurance(getHeaderMap(),map) }
    }

    suspend fun addFile(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addFile(getHeaderMap(),map) }
    }

    suspend fun deleteLifeInsurance(id: String): JsonObject {
        return apiRequest { api.deleteLifeInsurance(getHeaderMap(),id) }
    }

    suspend fun deleteHealthInsurance(id: String): JsonObject {
        return apiRequest { api.deleteHealthInsurance(getHeaderMap(),id) }
    }

    suspend fun deleteCarInsurance(id: String): JsonObject {
        return apiRequest { api.deleteCarInsurance(getHeaderMap(),id) }
    }

    suspend fun deleteFireInsurance(id: String): JsonObject {
        return apiRequest { api.deleteFireInsurance(getHeaderMap(),id) }
    }

    suspend fun deleteWcInsurance(id: String): JsonObject {
        return apiRequest { api.deleteWcInsurance(getHeaderMap(),id) }
    }

    suspend fun editLifeInsurance(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editLifeInsurance(getHeaderMap(),map,id) }
    }

    suspend fun editHealthInsurance(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editHealthInsurance(getHeaderMap(),map,id) }
    }

    suspend fun editFireInsurance(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editFireInsurance(getHeaderMap(),map,id) }
    }

    suspend fun editWcInsurance(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editWcInsurance(getHeaderMap(),map,id) }
    }

    suspend fun editCarInsurance(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editCarInsurance(getHeaderMap(),map,id) }
    }

    suspend fun getPolicyList(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getPolicyList(getHeaderMap(),map) }
    }

    suspend fun addPolicy(map: HashMap<String, RequestBody>): JsonObject {
        return apiRequest { api.addPolicy(getHeaderMap(),map) }
    }

    suspend fun editPolicy(map: HashMap<String, RequestBody>, id: String): JsonObject {
        return apiRequest { api.editPolicy(getHeaderMap(),map,id) }
    }

    suspend fun deletePolicy(id: String): JsonObject {
        return apiRequest { api.deletePolicy(getHeaderMap(),id) }
    }

    suspend fun getPortfolio(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getPortfolio(getHeaderMap(),map) }
    }

    suspend fun getYearlyDue(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getYearlyDue(getHeaderMap(),map) }
    }

    suspend fun getMonthlyDue(map: HashMap<String, Any>): JsonObject {
        return apiRequest { api.getMonthlyDue(getHeaderMap(),map) }
    }

    suspend fun getClientDocuments(): JsonObject {
        return apiRequest { api.getClientDocuments(getHeaderMap()) }
    }

    suspend fun deleteDocument(id: String): JsonObject {
        return apiRequest { api.deleteDocument(getHeaderMap(),id) }
    }

    fun getHeaderMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[AppConstants.Accept] = "application/json"
        map[AppConstants.AUTHORIZATION] = getPrefernces().getStringValue(AppConstants.ACCESS_HEADER)!!
        return map
    }
}