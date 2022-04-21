package pro.devonics.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

private const val TAG = "MyReceiver"

class MyReceiver : BroadcastReceiver() {

    private lateinit var pushDevonics: PushDevonics

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive")
        val helperCache = HelperCache(context)

        val mIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)

        // Bundle
        val bundle = intent.extras
        val sentPushId = bundle?.get("sent_push_id").toString()
        val openUrl = bundle?.get("open_url").toString()
        val deeplink = bundle?.get("deeplink").toString()
        Log.d(TAG, "onReceive: sentPushId $sentPushId")
        Log.d(TAG, "onReceive: openUrl $openUrl")
        Log.d(TAG, "onReceive: deeplink $deeplink")

        // Save to preferences
        //helperCache.saveSentPushId(sentPushId)
        //helperCache.saveOpenUrl(openUrl)
        //helperCache.saveDeeplink(deeplink)

        if (mIntent != null) {
            startActivity(context, mIntent)
        }
    }

    private fun startActivity(context: Context, intent: Intent) {
        Log.d(TAG, "startActivity:")
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        context.startActivity(intent)
    }
}