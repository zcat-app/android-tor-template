package examples.zcat.zcatandroidsamples.ui;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.ui.image.GlideActivity;
import examples.zcat.zcatandroidsamples.ui.rest.ZcashForumActivity;
import examples.zcat.zcatandroidsamples.ui.tor.TorActivity;
import examples.zcat.zcatandroidsamples.ui.websocket.WebsocketActivity;
import examples.zcat.zcatandroidsamples.ui.webview.WebActivity;
import examples.zcat.zcatandroidsamples.utils.SharedPreferencesUtils;

public class MainActivity extends TorActivity {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button tor = findViewById(R.id.tor);
        tor.setText(isTorEnabled ? "Turn Off TOR" : "Turn On TOR");
        tor.setOnClickListener(v -> {
            if (isTorEnabled) {
                stopTorConnection();
                tor.setText("Turn On TOR");
                SharedPreferencesUtils.setAppUseTor(context, false);
            }
            else {
                startTorConnection();
                tor.setText("Turn Off TOR");
                SharedPreferencesUtils.setAppUseTor(context, true);
            }
            isTorEnabled = !isTorEnabled;
        });

        findViewById(R.id.image).setOnClickListener(v -> {
            Intent i = new Intent(context, GlideActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        findViewById(R.id.rest).setOnClickListener(v -> {
            Intent i = new Intent(context, ZcashForumActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        findViewById(R.id.websocket).setOnClickListener(v -> {
            Intent i = new Intent(context, WebsocketActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        findViewById(R.id.webview).setOnClickListener(v -> {
            Intent i = new Intent(context, WebActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

    }
}