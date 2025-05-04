package examples.zcat.zcatandroidsamples.ui.websocket.client;

import java.nio.ByteBuffer;

public interface WebSocketCommand {

    void onOpen();

    void onClose(int code, String reason);

    void onMessage(String message);

    void onMessage(ByteBuffer message);

    void onError(Exception ex);

    void onError(Throwable ex);

}
