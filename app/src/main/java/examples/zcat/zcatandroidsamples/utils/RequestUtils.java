package examples.zcat.zcatandroidsamples.utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;

import examples.zcat.zcatandroidsamples.enums.RequestTypeEnum;
import examples.zcat.zcatandroidsamples.model.RequestCache;
import examples.zcat.zcatandroidsamples.model.RequestCacheDao;
import examples.zcat.zcatandroidsamples.rest.Command;
import examples.zcat.zcatandroidsamples.ui.webview.WebActivity;

public class RequestUtils {

    public static int CONNECTION_TIMEOUT_IN_SEC = 45;

    public static int READ_TIMEOUT_IN_SEC  = 30;

    public static int WRITE_TIMEOUT_IN_SEC = 30;

    public static void failedRequest(final Command command, final Throwable t, final RequestCache cachedRequest, final RequestTypeEnum requestTypeEnum, final String detail, final int errorCode) {
        long timestamp = Calendar.getInstance().getTimeInMillis();
        RequestCacheDao.updateOrCreate(
                new RequestCache(
                        cachedRequest == null ? timestamp : cachedRequest.getId(),
                        requestTypeEnum.getCode(),
                        detail,
                        errorCode,
                        timestamp,
                        cachedRequest == null ? 0 : cachedRequest.getLastSuccessfulTimestamp())
        );
        command.fail();
        if (t != null) {
            t.printStackTrace();
        }
    }

    public static void websiteVisitRequest(final Context context, final String url, final boolean isTorEnabled) {
        if (isTorEnabled) {
            openWebView(context, url);
            return;
        }

        try {
            CustomTabsUtils.createIntent().launchUrl(context, Uri.parse(url));
        }
        catch(Exception e) {
            openWebView(context, url);
        }

    }

    private static void openWebView(final Context context, final String url) {
        Intent i = new Intent(context, WebActivity.class);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("url", url);
        context.startActivity(i);
    }

}
