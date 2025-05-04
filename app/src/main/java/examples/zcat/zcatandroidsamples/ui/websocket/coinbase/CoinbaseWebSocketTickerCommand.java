package examples.zcat.zcatandroidsamples.ui.websocket.coinbase;

import android.util.Log;

import com.google.gson.Gson;

import java.util.function.Consumer;

import examples.zcat.zcatandroidsamples.ui.websocket.client.CommonWebsocketTicker;
import examples.zcat.zcatandroidsamples.ui.websocket.client.WebSocketTickerCommand;

public class CoinbaseWebSocketTickerCommand extends WebSocketTickerCommand {

    public CoinbaseWebSocketTickerCommand(Gson gson,
                                          Consumer<CommonWebsocketTicker> tickerConsumer) {
        super(gson, tickerConsumer);
    }

    @Override
    public void onMessage(String message) {
        super.onMessage(message);
        try {
            ticker = gson.fromJson(message, CoinbaseTicker.class);
            if (ticker.getMarketKey() != null) {
                channelTickerMap.put(ticker.getMarketKey(), ticker);
            }
        }
        catch(Exception e) {
            Log.e(LOG_TAG, "Coinbase websocket ticker fail: " + e.getMessage());
        }
    }

    @Override
    protected String getExchange() {
        return "COINBASE";
    }
}
