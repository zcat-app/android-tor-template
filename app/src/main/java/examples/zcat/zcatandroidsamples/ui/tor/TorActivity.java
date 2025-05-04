package examples.zcat.zcatandroidsamples.ui.tor;

import static examples.zcat.zcatandroidsamples.utils.RequestUtils.websiteVisitRequest;
import static examples.zcat.zcatandroidsamples.utils.TorUtils.isServiceRunning;
import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import org.torproject.jni.TorService;

import java.io.IOException;

import examples.zcat.zcatandroidsamples.utils.SharedPreferencesUtils;


public abstract class TorActivity extends AppCompatActivity {
    protected static final boolean WITH_TOR = true, WITHOUT_TOR = false;

    protected Context context;
    protected boolean isTorEnabled;
    protected boolean failedCallOnTorNotReady = false;
    protected boolean isTorReady;

    private TorService torService;
    private ServiceConnection serviceConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        isTorEnabled = SharedPreferencesUtils.isAppUseTor(context);
        initTorService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTorReady) {
            onTorConnectionStartResult();
        }
        else {
            onTorConnectionStopResult();
        }
    }

    protected void onTorConnectionStartResult() {
        isTorReady = true;
    }

    protected void onTorConnectionCommonResult() {

    }

    protected void onTorConnectionStopResult() {
        isTorReady = false;
    }

    protected void onTorConnectionFailResult() {
        SharedPreferencesUtils.setAppUseTor(context, false);
        isTorReady = false;
    }

    protected void onTorConnectionConnecting() {
        isTorReady = false;
    }

    protected void startTorConnection() {
        if (!isServiceRunning(getApplicationContext(), TorService.class)) {
            getApplicationContext().bindService(new Intent(getApplicationContext(), TorService.class), serviceConnection, BIND_AUTO_CREATE);
        }
        else {
            isTorReady = true;
        }
    }

    protected void stopTorConnection() {
        SharedPreferencesUtils.setAppUseTor(context, false);
    }

    protected void initTorService() {
        getOnBackPressedDispatcher().addCallback(
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finish();
                    }
                }
        );

        final var broadcastReceiver = createBroadCastReceiver();
        serviceConnection = createConnectionService();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, new IntentFilter(TorService.ACTION_STATUS), RECEIVER_NOT_EXPORTED);
        }
        else {
            registerReceiver(broadcastReceiver, new IntentFilter(TorService.ACTION_STATUS));
        }

        startTorConnection();
    }

    private BroadcastReceiver createBroadCastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String status = intent.getStringExtra(TorService.EXTRA_STATUS);
                if (status == null) {
                    return;
                }

                switch (status) {
                    case TorService.STATUS_ON:
                        onTorConnectionStartResult();
                        break;
                    case TorService.STATUS_OFF:
                        onTorConnectionStopResult();
                        break;
                    case TorService.STATUS_STARTING:
                    case TorService.STATUS_STOPPING:
                        onTorConnectionConnecting();
                        break;
                }
            }
        };
    }

    private ServiceConnection createConnectionService() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                torService = ((TorService.LocalBinder) service).getService();
                while (torService.getTorControlConnection() == null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    torService.getTorControlConnection().takeOwnership();
                } catch (IOException e) {
                    onTorConnectionFailResult();
                }
                onTorConnectionStartResult();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //
                onTorConnectionStopResult();
            }
        };
    }

    protected void torIsNotReady() {
        if (isSafeContext(context)) {
            Toast.makeText(context, "TOR connection is not ready, try to turn it on again, or disable it.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void visitLink(String link) {
        websiteVisitRequest(context, link, isTorEnabled);
    }

}
