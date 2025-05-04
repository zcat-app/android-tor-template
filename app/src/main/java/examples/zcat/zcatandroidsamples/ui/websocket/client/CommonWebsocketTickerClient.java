package examples.zcat.zcatandroidsamples.ui.websocket.client;

public interface CommonWebsocketTickerClient {

    String getExchange(); //use enum instead of string

    void subscribe() throws Exception;

    void close();
}
