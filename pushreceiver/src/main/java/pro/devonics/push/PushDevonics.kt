package pro.devonics.push

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import pro.devonics.push.DataHelper.Companion.createTransition
import pro.devonics.push.DataHelper.Companion.startTime
import pro.devonics.push.model.PushData
import pro.devonics.push.model.TimeData
import pro.devonics.push.network.ApiHelper
import pro.devonics.push.network.RetrofitBuilder
import java.util.*


private const val TAG = "PushDevonics"

class PushDevonics(context: Context, appId: String) {

    private val service = ApiHelper(RetrofitBuilder.apiService)

    private val myContext = context

    init {
        AppContextKeeper.setContext(context)
        PushInitialization.run(appId)
        createInternalId()
        startTime()
    }

    fun sendIntent(intent: Intent) {

        if ("transition" == intent.getStringExtra("command")) {
            val bundle = intent.extras
            val sentPushId = bundle?.get("sent_push_id").toString()
            val pushData = PushData(sentPushId)
            createTransition(pushData)
        }
    }

    fun openUrl(openUrl: String?) {
        if (openUrl != null) {
            val urlIntent = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(openUrl))

            urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                myContext.startActivity(urlIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e(TAG, "ActivityNotFoundException $e")
            }
        }
    }

    private fun createInternalId() {
        val pushCache = PushCache()

        var internalId = pushCache.getInternalIdFromPref()
        if (internalId == null) {
            val uuid = UUID.randomUUID()
            internalId = uuid.toString()
            pushCache.saveInternalId(internalId)
        }
    }

    fun getInternalId(): String? {
        val pushCache = PushCache()
        return pushCache.getInternalIdFromPref()
    }

    private fun startSession() {
        Log.d(TAG, "startSession: ")
        val pushCache = PushCache()
        val registrationId = pushCache.getRegistrationIdFromPref()
        if (pushCache.getSubscribeStatusFromPref() == true) {
            val session = registrationId?.let { service.createSession(it) }
            //Log.d(TAG, "subscribeStatus = ${pushCache.getSubscribeStatusFromPref()}")

        }
    }

    private fun stopSession() {
        val duration = DataHelper.getDuration()
        val pushCache = PushCache()
        val regId = pushCache.getRegistrationIdFromPref()
        if (regId != null) {
            val timeData = TimeData(duration)
            service.sendTimeStatistic(regId, timeData)
            //Log.d(TAG, "stopSession: timeData $timeData")
        }

        //Log.d(TAG, "stopSession: duration $duration")
        //Log.d(TAG, "stopSession: regId $regId")
        Log.d(TAG, "stopSession")
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