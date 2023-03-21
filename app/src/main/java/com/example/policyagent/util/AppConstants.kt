package com.example.policyagent.util
import com.example.policyagent.BuildConfig
import com.example.policyagent.data.responses.clientlist.ClientData
import com.example.policyagent.data.responses.companylist.CompanyData

class AppConstants {
    companion object {

        val COMPANIES: String = "companies"
        val CLIENTS: String = "clients"
        var companies: ArrayList<CompanyData?>? = ArrayList()
        var clients: ArrayList<ClientData?>? = ArrayList()

        val FIRE_INSURANCE: String = "fire_insurance"
        val WC_INSURANCE: String = "wc_insurance"
        val HEALTH_INSURANCE: String = "health_insurance"
        val LIFE_INSURANCE: String = "life_insurance"
        val CAR_INSURANCE: String = "car_insurance"
        val POLICYNAME: String = "policy_name"
        val YEAR: String = "year"
        val VERIFY: String = "verify"
        val MOBILE: String = "mobile"
        val RESEND: String = "resend"
        val USER_TYPE: String = "user_type"
        val AGENT: String = "AGENT"
        val API_BASE_URL = "http://technocometsolutions.in/developers/smart-policy/api/"//Live
        val API_DEVELOPMENT_URL = "http://technocometsolutions.in/developers/smart-policy/api/"//Development
        const val ACCESS_HEADER = "access_token"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val DATE_FORMAT_NEW = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        val APPDIRECTORY = "/PolicyAgent/Images"
        val MAX_SIZE = 1024
        const val AUTHORIZATION = "Authorization"
        const val Accept = "Accept"
        val IS_REMEMBER = "is_remeber"
        var TIMER_TIME: Long = 60000
        var USER_ID: String = "user_id"
        var TOKEN: String = "token"

        val IMAGE_BASE_URL = "https://taka24.in"//Live

        val SEVSERVER_KEY_LIVE = "oE6fUAyRp67ZsHWW8pBg0OJ4tC6xO1izKCmeiCUjB8I"
        val SEVSERVER_KEY_DEV = "oE6fUAyRp67ZsHWW8pBg0OJ4tC6xO1izKCmeiCUjB8I"

        fun getSevserverHeader(): String {
            if (BuildConfig.DEBUG) {
                return SEVSERVER_KEY_DEV
            } else {
                return SEVSERVER_KEY_LIVE
            }
        }

        fun getImageBaseUrl(): String {
            return if (BuildConfig.DEBUG) {
                IMAGE_BASE_URL
            } else {
                IMAGE_BASE_URL
            }
        }

        fun getBaseUrl(): String {
            return if (BuildConfig.DEBUG) {
                API_DEVELOPMENT_URL
            } else {
                API_BASE_URL
            }
        }

    }
}