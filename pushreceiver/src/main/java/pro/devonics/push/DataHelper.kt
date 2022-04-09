package pro.devonics.push

import pro.devonics.push.model.PushData
import pro.devonics.push.model.Tag
import pro.devonics.push.model.TimeData
import pro.devonics.push.network.ApiHelper
import pro.devonics.push.network.RetrofitBuilder
import java.util.*

private const val TAG = "Repository"

class DataHelper {

    companion object {

        private val service = ApiHelper(RetrofitBuilder.apiService)

        private var startTime: Long = 0
        private var stopTime: Long = 0

        private val cache = PushCache()
        val registrationId = cache.getRegistrationIdFromPref()

        fun getDuration(): Long {
            val date = Calendar.getInstance().timeInMillis
            stopTime = date

            return (stopTime - startTime) / 1000
        }

        fun startTime() {
            val date = Calendar.getInstance().timeInMillis
            startTime = date
            //Log.d(TAG, "onCreate: startTime = $startTime")
        }

        fun createTransition(pushData: PushData) {
            //val pushCache = PushCache()
            //val regId = cache.getRegistrationIdFromPref()
            val transition = registrationId?.let { service.createTransition(it, pushData) }
            //Log.d(TAG, "createTransition: = $transition")
        }
    }
}