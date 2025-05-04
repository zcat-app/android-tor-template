package examples.zcat.zcatandroidsamples.utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;


public class CustomTabsUtils {

    public static void initChromeTabs(Context context) {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            String packageName = resolveInfo.activityInfo.packageName;

            if (packageName != null && !packageName.isEmpty()) {
                CustomTabsClient.connectAndInitialize(context, packageName);
            }
        }
    }

    public static CustomTabsIntent createIntent() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.BLACK);
        builder.setSecondaryToolbarColor(Color.BLACK);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        return customTabsIntent;
    }

}
