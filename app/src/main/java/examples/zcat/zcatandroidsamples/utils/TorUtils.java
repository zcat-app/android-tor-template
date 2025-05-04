package examples.zcat.zcatandroidsamples.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TorUtils {

    private static Proxy torProxy;

    public static synchronized Proxy getTorProxy() {
        if (torProxy == null) {
            torProxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9050));
        }
        return torProxy;
    }

    public static OkHttpClient createhOKHttpClient(boolean withTor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
               .proxy(withTor ? getTorProxy() : null)
               .addInterceptor(interceptor)
               .connectTimeout(RequestUtils.CONNECTION_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
               .readTimeout(RequestUtils.READ_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
               .writeTimeout(RequestUtils.WRITE_TIMEOUT_IN_SEC, TimeUnit.SECONDS)
               .build();
    }

    private static OkHttpClient httpClientWithTor;
    private static OkHttpClient httpClientNoTor;
    public static synchronized OkHttpClient getHttpClient(boolean withTor) {
        if (withTor) {
            return httpClientWithTor;
        }
        else {
            return httpClientNoTor;
        }
    }

    public static void createHttpClientsInThread(final boolean isTorTurnedOn) {
        final var withTor = true;
        httpClientNoTor = createhOKHttpClient(!withTor);

        // Set up OkHttpClient with proxy in separate thread
        final var networkThread = new Thread(() -> {
            httpClientWithTor = createhOKHttpClient(withTor);
        });

        // Start the thread
        networkThread.start();
        // Wait for the network thread to complete if tor should be on
        if (isTorTurnedOn) {
            try {
                networkThread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Retrofit createRetrofit(final String baseUrl, final OkHttpClient client, final Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(client)
                .build();
    }


    public static synchronized boolean isServiceRunning(Context context, Class<?> serviceClass) {
        final var activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }

        for (final var service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
