package pro.devonics.push

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.logging.HttpLoggingInterceptor
import pro.devonics.push.DataHelper.Companion.startTime
import pro.devonics.push.model.PushData
import pro.devonics.push.model.TimeData
import pro.devonics.push.network.ApiHelper
import pro.devonics.push.network.RetrofitBuilder
import java.util.*
import java.util.logging.Level


private const val TAG = "PushDevonics"
private const val PERMISSIONS_REQUEST_CODE = 2

enum class Level {
    BASIC,BODY, HEADERS, NONE
    //BASIC(true) , NONE(false)
}

class PushDevonics(activity: Activity, appId: String) {

    private val service = ApiHelper(RetrofitBuilder.apiService)
    private val myContext = activity
    private val helperCache = HelperCache(activity)
    private val mAppId = appId
    private val mActivity = activity

    init {
        setLogLevelHttp(pro.devonics.push.Level.NONE)
        AppContextKeeper.setContext(activity)
        createInternalId()
    }

    fun setLogLevelHttp(l: pro.devonics.push.Level) {
        RetrofitBuilder.loggingInterceptor.apply {
            when(l) {
                pro.devonics.push.Level.BASIC -> {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
                pro.devonics.push.Level.BODY -> {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
                pro.devonics.push.Level.HEADERS -> {
                    setLevel(HttpLoggingInterceptor.Level.HEADERS)
                }
                pro.devonics.push.Level.NONE -> {
                    setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            }
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // allow permission
                PushInit.run(mAppId, service)
                startTime()
                startSession()
                sendTransition(service)
                Log.d(TAG, "Allow permission")
                // FCM SDK (and your app) can post notifications.
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // permission denied
                Log.d(TAG, "Permission denied")
            } else {
                // ask permission
                Log.d(TAG, "Ask permission")
                mActivity.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSIONS_REQUEST_CODE
                )
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //Log.d(TAG, "Create user")
                    PushInit.run(mAppId, service)
                    startTime()
                    startSession()
                    sendTransition(service)
                }
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "Ask permission for API < TIRAMISU")
            PushInit.run(mAppId, service)
            startTime()
            startSession()
            sendTransition(service)
        }
    }

    private fun sendTransition(service: ApiHelper) {

        val sentPushId = helperCache.getSentPushId()
        val pushCache = PushCache()
        val registrationId = pushCache.getRegistrationId()
        if (sentPushId != "" || sentPushId != null) {
            val pushData = sentPushId?.let { PushData(it) }
            if (pushData != null) {
                if (registrationId != null) {
                    service.createTransition(registrationId, pushData)
                }
            }
        }
        helperCache.saveSentPushId(null)

    }

    fun openUrl() {
        val openUrl = helperCache.getOpenUrl()
        if (openUrl == "") {
            return
        }

        //Log.v(TAG, "openUrl: openUrl = $openUrl")
        if (openUrl != null) {

            val urlIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(openUrl))

            //Log.d(TAG, "openUrl: Uri.parse = ${Uri.parse(openUrl)}")
            urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                myContext.startActivity(urlIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, "ActivityNotFoundException $e")
            }
        }

        helperCache.saveOpenUrl("")
        //Log.d(TAG, "openUrl = $openUrl")
    }

    fun getDeeplink(): String {
        val deep1 = helperCache.getDeeplink()
        //Log.d(TAG, "getDeeplink: deep1 = $deep1")
        helperCache.saveDeeplink("")
        return deep1.toString()
    }

    private fun createInternalId() {
        val pushCache = PushCache()

        var internalId = pushCache.getInternalId()
        if (internalId == null) {
            val uuid = UUID.randomUUID()
            internalId = uuid.toString()
            //Log.d(TAG, "createInternalId: internalId = $internalId")
            pushCache.saveInternalId(internalId)
        }

        //checkPermission(appId, activity)
    }

    fun getInternalId(): String? {
        val pushCache = PushCache()
        //Log.d(TAG, "getInternalId: internalId = ${pushCache.getInternalId()}")
        return pushCache.getInternalId()
    }

    //Be Public
    fun startSession() {
        //Log.d(TAG, "startSession: ")
        val pushCache = PushCache()
        val registrationId = pushCache.getRegistrationId()
        if (pushCache.getSubscribeStatus() == true) {
            registrationId?.let { service.createSession(it, mAppId) }
            //Log.d(TAG, "subscribeStatus = ${pushCache.getSubscribeStatusFromPref()}")

        }
    }

    fun stopSession() {
        val duration = DataHelper.getDuration()
        val pushCache = PushCache()
        val regId = pushCache.getRegistrationId()
        if (regId != null) {
            val timeData = TimeData(duration)
            service.sendTimeStatistic(regId, timeData)
            //Log.d(TAG, "stopSession: timeData $timeData")
        }

        //Log.d(TAG, "stopSession: duration $duration")
        //Log.d(TAG, "stopSession: regId $regId")
        //Log.d(TAG, "stopSession")
    }

    fun setTags(key: String, value: String) {
        val pushCache = PushCache()
        if (key == null && value == null) {
            pushCache.saveTagKey("")
            pushCache.saveTagValue("")
        } else {
            pushCache.saveTagKey(key)
            pushCache.saveTagValue(value)
        }
    }
}
