package pro.devonics.push

import android.content.Context

private const val TAG = "PushCache"

class PushCache {

    private val PUSH_CACHE = "push_cache"
    //private val OLD_REGISTRATION_ID = "old_registration_id"
    private val REGISTRATION_ID = "registration_id"
    //private val REGISTRATION_STATUS = "registration_status"
    private val SUBSCRIBE_STATUS = "subscribe_status"
    private val INTERNAL_ID = "internal_id"
    private val TAG_KEY = "tag_key"
    private val TAG_VALUE = "tag_value"

    private val appContext = AppContextKeeper.getContext()
    private val ed = appContext
        .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
        ?.edit()

    fun saveTagKey(key: String) {
        ed?.putString(TAG_KEY, key)
        ed?.apply()
    }

    fun getTagKey(): String? {
        return appContext
            .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
            ?.getString(TAG_KEY, null)
    }

    fun saveTagValue(tagValue: String) {
        ed?.putString(TAG_VALUE, tagValue)
        ed?.apply()
    }

    fun getTagValue(): String? {
        return appContext
            .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
            ?.getString(TAG_VALUE, null)
    }

    fun saveInternalId(internalId: String) {
        ed?.putString(INTERNAL_ID, internalId)
        ed?.apply()
    }

    fun getInternalId(): String? {
        return appContext
            .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
            ?.getString(INTERNAL_ID, null)
    }

    fun saveRegistrationId(reg_id: String) {
        ed?.putString(REGISTRATION_ID, reg_id)
        //Log.d(TAG, "saveRegistrationIdPref: reg_id = $reg_id")
        ed?.apply()
    }

    fun getRegistrationId(): String? {
        return appContext
            .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
            ?.getString(REGISTRATION_ID, null)
    }

    fun saveSubscribeStatus(status: Boolean) {
        ed?.putBoolean(SUBSCRIBE_STATUS, status)
        ed?.apply()
    }
    fun getSubscribeStatus(): Boolean? {
        return appContext
            .getSharedPreferences(PUSH_CACHE, Context.MODE_PRIVATE)
            ?.getBoolean(SUBSCRIBE_STATUS, false)
    }
}