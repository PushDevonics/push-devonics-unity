package pro.devonics.push

import android.app.Activity
import com.unity3d.player.UnityPlayer;

class PushCallbackAndroid {
    private var pushDevonics: PushDevonics? = null
    fun initPush(appId: String?) {
        val activity: Activity = UnityPlayer.currentActivity
        pushDevonics = PushDevonics(activity, appId!!)
    }
    fun onResume() {
        val activity = UnityPlayer.currentActivity
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