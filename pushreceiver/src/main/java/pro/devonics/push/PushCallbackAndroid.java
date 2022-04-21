package pro.devonics.push;

import android.annotation.SuppressLint;
import android.app.Activity;

public class PushCallbackAndroid {
    @SuppressLint("StaticFieldLeak")
    private static PushDevonics pushDevonics;

    public static void initPush(String appId, Activity activity) {
        pushDevonics = new PushDevonics(activity, appId);
    }

    public static void onResume() {
        pushDevonics.startSession();
    }

    public static void onStop() {
        pushDevonics.stopSession();
    }

    public static void sendTag(String key, String value) {
        pushDevonics.setTags(key, value);
    }

    public static String getInternalID() {
        return pushDevonics.getInternalId();
    }
    public static String getDeeplink() {
        return pushDevonics.getDeeplink();
    }
    public static void openPushUrl() {
        pushDevonics.openUrl();
    }
}
