package examples.zcat.zcatandroidsamples.enums;

import java.util.ArrayList;
import java.util.List;

public enum WebsocketMarketEnum {
    COINBASE_ZEC_USD("COINBASE", "\"ZEC-USD\"", "ZEC-USD", "coinbase_ZEC-USD"),
    COINBASE_ZEC_BTC("COINBASE", "\"ZEC-BTC\"", "ZEC-BTC", "coinbase_ZEC-BTC"),

    ;

    public String exchange;
    public String channel;
    public String market;
    public String mapId;

    WebsocketMarketEnum(String exchange, String channel, String market, String mapId) {
        this.exchange = exchange;
        this.channel = channel;
        this.market = market;
        this.mapId = mapId;
    }

    public static WebsocketMarketEnum findByMapId(String mapId) {
        for (WebsocketMarketEnum websocketMarket : values()) {
            if (websocketMarket.mapId.equals(mapId)) {
                return websocketMarket;
            }
        }
        return null;
    }

    public static List<WebsocketMarketEnum> findByExchange(String exchange) {
        List<WebsocketMarketEnum> result = new ArrayList<>();
        for (WebsocketMarketEnum websocketMarket : values()) {
            if (websocketMarket.exchange.equals(exchange)) {
                result.add(websocketMarket);
            }
        }
        return result;
    }
}
