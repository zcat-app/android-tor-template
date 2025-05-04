package examples.zcat.zcatandroidsamples.ui.websocket;

import static examples.zcat.zcatandroidsamples.enums.WebsocketMarketEnum.COINBASE_ZEC_USD;
import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import examples.zcat.zcatandroidsamples.enums.WebsocketMarketEnum;
import examples.zcat.zcatandroidsamples.ui.websocket.client.CommonWebsocketTicker;
import examples.zcat.zcatandroidsamples.ui.websocket.client.CommonWebsocketTickerClient;
import examples.zcat.zcatandroidsamples.ui.websocket.client.WebSocketTicker;
import examples.zcat.zcatandroidsamples.ui.websocket.coinbase.CoinbaseWebSocketTickerClient;
import examples.zcat.zcatandroidsamples.ui.websocket.coinbase.CoinbaseWebSocketTickerCommand;

import kotlin.jvm.functions.Function2;

public class WebsocketManager {

    private Context context;

    private Map<WebsocketMarketEnum, WebSocketTicker> mapTicker;
    private List<CommonWebsocketTickerClient> websocketClients;
    private List<WebsocketMarketEnum> markets;

    private ExecutorService executor;
    private Handler handler;
    private Function2<WebSocketTicker, Double, Void> tickerConsumer;
    private boolean withTor;

    public WebsocketManager(Context context,
                            Handler handler,
                            Function2<WebSocketTicker, Double, Void> tickerConsumer,
                            boolean withTor) {
        this.context = context;
        this.handler = handler;
        this.tickerConsumer = tickerConsumer;
        this.withTor = withTor;
    }

    void killWebSocketThread() {
        closeWebSocketConnections();
    }

    private void closeWebSocketConnections() {
        if (websocketClients != null) {
            executor.shutdown();
            websocketClients.forEach(CommonWebsocketTickerClient::close);
        }
    }

    void runWebSocketThread(final boolean init) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Gson gson = new GsonBuilder().create();
            loadTrackedMarkets();
            if (!markets.isEmpty()) {
                if (init) {
                    initWebSocketMap();
                    createWebSocketClients(gson);
                    //prepareViewGeneration();
                    //initTickersFromDB();
                }
                websocketClients.forEach(client -> subscribeWebSocketClient(client, handler));
            }
        });
    }

    private void loadTrackedMarkets() {
        markets = new ArrayList<>();
        markets.addAll(List.of(COINBASE_ZEC_USD));
    }

    private void createWebSocketClients(Gson gson) {
        websocketClients = new ArrayList<>();
        //add different clients
        websocketClients.add(new CoinbaseWebSocketTickerClient(new CoinbaseWebSocketTickerCommand(gson, this::processWebSocketTicker), markets, withTor));
    }

    private void initWebSocketMap() {
        mapTicker = new LinkedHashMap<>();
        mapTicker.putAll(CoinbaseWebSocketTickerClient.tickerDictionary);
    }

    private void subscribeWebSocketClient(CommonWebsocketTickerClient client, Handler handler) {
        try {
            client.subscribe();
        }
        catch(Exception e) {
            e.printStackTrace();
            if (isSafeContext(context)) {
                handler.post(() -> {
                        Toast.makeText(context, "Could not connect!", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    void processWebSocketTicker(CommonWebsocketTicker ticker) {
        if (isSafeContext(context) && ticker != null) {
            //testing only zec-usd, for more markets and exchanges needs some dictionary
            processCommonTicker(COINBASE_ZEC_USD, ticker.getPrice());
        }
    }

    private void processCommonTicker(WebsocketMarketEnum key, Double priceValue) {
        if (key != null) {
            WebSocketTicker tickerPair = mapTicker.get(key);

            if (tickerPair != null) {
                handler.post(() -> {
                    try {
                        tickerConsumer.invoke(tickerPair, priceValue);
                    } catch (Exception e) {
                        Log.e("Websocket handler:", e.toString());
                    }
                });
            }
        }
    }
}
