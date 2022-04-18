package pro.devonics.push;

import android.app.Activity;

public class PushCallbackAndroid {
    private static PushDevonics pushDevonics;

    public static void initPush(String appId, Activity activity) {
        pushDevonics = new PushDevonics(activity, appId);
        pushDevonics.sendIntent(activity.getIntent());
    }

    public static void onResume(Activity activity) {
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
}
