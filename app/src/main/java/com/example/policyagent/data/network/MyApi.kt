package com.example.policyagent.data.network

import com.example.policyagent.data.responses.CommonResponse
import com.example.policyagent.data.responses.login.LoginResponse
import com.example.policyagent.util.AppConstants
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.HashMap
import java.util.concurrent.TimeUnit

interface MyApi {

    @POST("login")
    suspend fun userLogin(@HeaderMap map: HashMap<String, Any>, @Body body: HashMap<String, Any>): Response<JsonObject>

    @POST("auth/password-reset/send-mail")
    suspend fun forgotPassword(@HeaderMap map: HashMap<String, Any>, @Body body: HashMap<String, Any>): Response<JsonObject>

    @POST("auth/tokenCheck")
    suspend fun verifyOTP(@HeaderMap map: HashMap<String, Any>, @Body body: HashMap<String, Any>): Response<JsonObject>

    @POST("auth/reset-password")
    suspend fun resetPassword(@HeaderMap map: HashMap<String, Any>, @Body body: HashMap<String, Any>): Response<JsonObject>

    @POST("client/change_password")
    suspend fun clientChangePassword(@HeaderMap map: HashMap<String, Any>, @Body body: HashMap<String, Any>): Response<JsonObject>

    @POST("client/auth/logout")
    suspend fun clientLogout(@HeaderMap map: HashMap<String, Any>): Response<JsonObject>

    @GET("agent/form/life-insurance")
    suspend fun getLifeInsurance(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/form/health-insurance")
    suspend fun getHealthInsurance(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/form/car-insurance")
    suspend fun getCarInsurance(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/form/wc-insurance")
    suspend fun getWcInsurance(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/form/fire-insurance")
    suspend fun getFireInsurance(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/client/show")
    suspend fun getClientList(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @GET("agent/companylist")
    suspend fun getCompanyList(@HeaderMap map: HashMap<String, Any>,): Response<JsonObject>

    @Multipart
    @POST("agent/form/life-insurance/create")
    suspend fun addLifeInsurance(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    @Multipart
    @POST("agent/form/health-insurance/create")
    suspend fun addHealthInsurance(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    @Multipart
    @POST("agent/form/car-insurance/create")
    suspend fun addCarInsurance(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    @Multipart
    @POST("agent/form/wc-insurance/create")
    suspend fun addWcInsurance(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    @Multipart
    @POST("agent/form/fire-insurance/create")
    suspend fun addFireInsurance(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    @GET("agent/form/life-insurance/delete/{id}")
    suspend fun deleteLifeInsurance(@HeaderMap map: HashMap<String, Any>,@Path("id") id:String): Response<JsonObject>

    @GET("agent/form/health-insurance/delete/{id}")
    suspend fun deleteHealthInsurance(@HeaderMap map: HashMap<String, Any>,@Path("id") id:String): Response<JsonObject>

    @GET("agent/form/car-insurance/delete/{id}")
    suspend fun deleteCarInsurance(@HeaderMap map: HashMap<String, Any>,@Path("id") id:String): Response<JsonObject>

    @GET("agent/form/fire-insurance/delete/{id}")
    suspend fun deleteFireInsurance(@HeaderMap map: HashMap<String, Any>,@Path("id") id:String): Response<JsonObject>

    @GET("agent/form/wc-insurance/delete/{id}")
    suspend fun deleteWcInsurance(@HeaderMap map: HashMap<String, Any>,@Path("id") id:String): Response<JsonObject>




    @Multipart
    @POST("agent/form/life-insurance/imageupload")
    suspend fun addFile(@HeaderMap map: HashMap<String, Any>, @PartMap body: HashMap<String, RequestBody>): Response<JsonObject>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {
            val interceptor = HttpLoggingInterceptor()
            var url = AppConstants.getBaseUrl()

            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okkHttpclient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(url)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(MyApi::class.java)
        }
    }
}