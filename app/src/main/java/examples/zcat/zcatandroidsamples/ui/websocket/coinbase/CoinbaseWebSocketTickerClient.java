package examples.zcat.zcatandroidsamples.ui.websocket.coinbase;

import static examples.zcat.zcatandroidsamples.enums.WebsocketMarketEnum.COINBASE_ZEC_USD;

import android.text.TextUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import examples.zcat.zcatandroidsamples.enums.WebsocketMarketEnum;
import examples.zcat.zcatandroidsamples.ui.websocket.client.BaseOKWebSocketClient;
import examples.zcat.zcatandroidsamples.ui.websocket.client.CommonWebsocketTickerClient;
import examples.zcat.zcatandroidsamples.ui.websocket.client.WebSocketTicker;
import examples.zcat.zcatandroidsamples.ui.websocket.client.WebSocketTickerCommand;


public class CoinbaseWebSocketTickerClient implements CommonWebsocketTickerClient {

    public static final Map<WebsocketMarketEnum, WebSocketTicker> tickerDictionary;
    static {
        Map<WebsocketMarketEnum, WebSocketTicker> initMap = new LinkedHashMap<>();
        initMap.put(COINBASE_ZEC_USD, new WebSocketTicker("USD", "ZEC"));
        //initMap.put(COINBASE_ZEC_BTC, new WebSocketTicker("BTC", "ZEC"));
        tickerDictionary = Collections.unmodifiableMap(initMap);
    }

    private static final String SOCKET_BASE_URL = "wss://ws-feed.exchange.coinbase.com";
    private static final String SUBSCRIBE_JSON =
            "{\n" +
            "    \"type\": \"subscribe\",\n" +
            "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [{PLACEHOLDER}] }]\n" +
            "}";
    private static final String UNSUBSCRIBE_JSON =
            "{\n" +
            "    \"type\": \"unsubscribe\",\n" +
            "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [{PLACEHOLDER}] }]\n" +
            "}";

    private BaseOKWebSocketClient client;
    private WebSocketTickerCommand command;

    private String markets;
    private boolean withTor;
    public CoinbaseWebSocketTickerClient(WebSocketTickerCommand command, List<WebsocketMarketEnum> markets, boolean withTor) {
        this.command = command;
        this.markets = TextUtils.join(",", markets.stream()
                .filter(market -> market.exchange.equals(getExchange()))
                .map(market -> market.channel)
                .collect(Collectors.toList()));
        this.withTor = withTor;
    }

    @Override
    public String getExchange() {
        return "COINBASE";
    }

    @Override
    public void subscribe() throws Exception {
        //"BTC-EUR","BTC-USD","ETH-EUR","ETH-USD","ETH-BTC"
        if (!markets.isEmpty()) {
            client = new BaseOKWebSocketClient(SOCKET_BASE_URL, command, withTor);
            client.connectBlocking(5, TimeUnit.SECONDS);
            client.sendMessage(SUBSCRIBE_JSON.replace("{PLACEHOLDER}", markets));
        }
    }

    @Override
    public void close() {
        if (client != null && !markets.isEmpty()) {
            if (client.isConnected()) {
                client.sendMessage(UNSUBSCRIBE_JSON.replace("{PLACEHOLDER}", markets));
            }
            client.disconnect();
        }
    }

}
