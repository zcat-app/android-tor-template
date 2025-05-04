package examples.zcat.zcatandroidsamples.ui.websocket.client;

public interface CommonWebsocketTicker {

    String getExchange(); //use enum instead of string

    Double getPrice();

    String getMarketKey();
}
