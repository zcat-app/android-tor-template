package examples.zcat.zcatandroidsamples.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {

    static final String SHARED_PREF_NAME = "examples.zcat.zcatandroidsamples";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    static final String APP_USE_TOR= "APP_USE_TOR";
    public static boolean setAppUseTor(Context context, boolean useTor) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(APP_USE_TOR, useTor);
        return editor.commit();
    }

    public static boolean isAppUseTor(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return pref.getBoolean(APP_USE_TOR, false);
    }

}
