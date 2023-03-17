package com.example.policyagent.data.preferences
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun setStringValue(Key: String, savedAt: String) {
        preference.edit().putString(Key, savedAt).apply()
    }

    fun getStringValue(Key: String): String? {
        return preference.getString(Key, "")
    }

    fun setBooleanValue(Key: String, savedAt: Boolean) {
        preference.edit().putBoolean(Key, savedAt).apply()
    }

    fun getBooleanValue(Key: String): Boolean? {
        return preference.getBoolean(Key, false)
    }

    fun setFloatValue(Key: String, savedAt: Float) {
        preference.edit().putFloat(Key, savedAt).apply()
    }

    fun getFLoatValue(Key: String): Float? {
        return preference.getFloat(Key, 0f)
    }


    fun setIntValue(Key: String, savedAt: Int) {
        preference.edit().putInt(Key, savedAt).apply()
    }

    fun getIntValue(Key: String): Int? {
        return preference.getInt(Key, 0)
    }

    fun setLongValue(Key: String, savedAt: Long) {
        preference.edit().putLong(Key, savedAt).apply()
    }

    fun getLongValue(Key: String): Long? {
        return preference.getLong(Key, 0)
    }

    fun clearPrefrences(){
        preference.edit().clear().apply()
    }


}