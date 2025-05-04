package examples.zcat.zcatandroidsamples.ui.websocket.client;

import android.util.Log;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public abstract class WebSocketTickerCommand implements WebSocketCommand {

    protected static final String LOG_TAG = "WebSockets";

    protected Gson gson;
    protected Consumer<CommonWebsocketTicker> tickerConsumer;
    protected CommonWebsocketTicker ticker;
    protected Map<String, CommonWebsocketTicker> channelTickerMap;
    protected Disposable disposable;

    public WebSocketTickerCommand(Gson gson,
                                  Consumer<CommonWebsocketTicker> tickerConsumer) {
        this.gson = gson;
        this.tickerConsumer = tickerConsumer;
        this.ticker = null;
        this.channelTickerMap = new HashMap<>();
    }

    public void onOpen() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        this.disposable = Observable.interval(1500, TimeUnit.MILLISECONDS)
                .map(tick -> {
                    for (String key : channelTickerMap.keySet()) {
                        CommonWebsocketTicker ticker = channelTickerMap.get(key);
                        if (ticker != null && ticker.getPrice() != null) {
                            tickerConsumer.accept(ticker);
                        }
                    }
                    channelTickerMap.clear();
                    return 1;
                }).subscribe();
    }

    public void onClose(int code, String reason) {
        Log.d(LOG_TAG, reason);
        disposable.dispose();
    }

    public void onMessage(String message) {
        if (disposable == null || disposable.isDisposed()) {
            Log.d(LOG_TAG, "disposable null or disposed");
        }
        Log.d(LOG_TAG, message);
    }

    public void onMessage(ByteBuffer message) {

    }

    public void onError(Exception ex) {
        if (ex != null && ex.getMessage() !=null) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    public void onError(Throwable ex) {
        if (ex != null && ex.getMessage() !=null) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    abstract protected String getExchange();

}
