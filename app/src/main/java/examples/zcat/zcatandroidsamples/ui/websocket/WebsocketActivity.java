package examples.zcat.zcatandroidsamples.ui.websocket;

import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.ui.tor.TorActivityWithRest;
import examples.zcat.zcatandroidsamples.ui.websocket.client.WebSocketTicker;

public class WebsocketActivity extends TorActivityWithRest {

    private WebsocketManager websocketManager;

    private TextView price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.websocket_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        price = findViewById(R.id.price);

        //handler to process changes on UI thread
        Handler handler = new Handler(Looper.getMainLooper());
        websocketManager = new WebsocketManager(context, handler, this::setTickerData, isTorEnabled);

    }

    Void setTickerData(WebSocketTicker tickerPair, Double priceValue) {
        if (isSafeContext(context)) {
            price.setText(BigDecimal.valueOf(priceValue).setScale(2, RoundingMode.HALF_UP) + " " + tickerPair.market);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        torProtectedRestCalls();
    }

    @Override
    protected void restCalls(boolean withTor) {
        //init values via rest
        initWebSocketClients();
    }

    private void initWebSocketClients() {
        if (!isSafeContext(context)) {
            return;
        }
        websocketManager.runWebSocketThread(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        websocketManager.killWebSocketThread();
    }

}