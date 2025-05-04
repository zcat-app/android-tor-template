package examples.zcat.zcatandroidsamples.ui.websocket.coinbase;

import com.google.gson.annotations.SerializedName;

import examples.zcat.zcatandroidsamples.ui.websocket.client.CommonWebsocketTicker;


public class CoinbaseTicker implements CommonWebsocketTicker {
    //{"type":"ticker","sequence":17500471476,"product_id":"ETH-USD","price":"2252.2","open_24h":"2369.28","volume_24h":"499178.96750564","low_24h":"2207.74","high_24h":"2574.86","volume_30d":"17277402.11450947","best_bid":"2252.20","best_ask":"2252.21","side":"sell","time":"2021-05-29T21:21:43.265804Z","trade_id":124900118,"last_size":"0.14377092"}
    public String ticker;
    public long sequence;
    @SerializedName("product_id")
    public String productId;
    public Double price;
    @SerializedName("volume_24h")
    public Double volume;
    @SerializedName("best_bid")
    public Double bid;
    @SerializedName("best_ask")
    public Double ask;

    @Override
    public String getExchange() {
        return "COINBASE";
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public String getMarketKey() {
        return productId;
    }
}
