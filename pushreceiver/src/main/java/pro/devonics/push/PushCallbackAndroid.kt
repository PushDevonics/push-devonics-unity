package pro.devonics.push

import android.app.Activity

class PushCallbackAndroid {
    private var pushDevonics: PushDevonics? = null
    fun initPush(appId: String?, activity: Activity?) {
        pushDevonics = PushDevonics(activity!!, appId!!)
    }
    fun onResume(activity: Activity) {
        pushDevonics!!.startSession()
        pushDevonics!!.sendIntent(activity.intent)
    }

    fun onStop() {
        pushDevonics!!.stopSession()
    }

    fun sendTag(key: String?, value: String?) {
        pushDevonics!!.setTags(key!!, value!!)
    }

    fun getInternalID(): String? {
        return pushDevonics!!.getInternalId()
    }
}
