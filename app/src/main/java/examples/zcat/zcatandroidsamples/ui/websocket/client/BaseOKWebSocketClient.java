package examples.zcat.zcatandroidsamples.ui.websocket.client;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import examples.zcat.zcatandroidsamples.utils.TorUtils;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class BaseOKWebSocketClient {

    private static final String TAG = "WebSocket";
    private WebSocket webSocket;
    private boolean isConnected = false;

    private String url;
    private WebSocketCommand command;

    private boolean withTor;

    private CountDownLatch connectLatch = new CountDownLatch(1);


    public BaseOKWebSocketClient(String url, WebSocketCommand command, boolean withTor) {
        this.url = url;
        this.command = command;
        this.withTor = withTor;
    }

    public void connect() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = TorUtils.getHttpClient(withTor).newWebSocket(request, new okhttp3.WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("Connected to WebSocket server.");
                isConnected = true;
                command.onOpen();
                connectLatch.countDown();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "Received message: " + text);
                // Handle incoming text message
                command.onMessage(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d(TAG, "Received binary message: " + bytes.hex());
                //command.onMessage(bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "Closing WebSocket: " + reason);
                webSocket.close(code, reason);
                command.onClose(code, reason);
                isConnected = false;
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket closed: " + reason);
                isConnected = false;
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.d(TAG, "Failure: " + t.getMessage());
                command.onError(t);
                isConnected = false;
                connectLatch.countDown();

            }
        });

        //client.dispatcher().executorService().shutdown(); // Optional: shutdown dispatcher after use
    }

    // Method to send a message to the WebSocket server
    public void sendMessage(String message) {
        if (isConnected && webSocket != null) {
            webSocket.send(message);
            Log.d(TAG, "Message sent: " + message);
        } else {
            Log.d(TAG, "WebSocket is not connected. Message not sent.");
        }
    }

    // Method to disconnect from the WebSocket server
    public void disconnect() {
        if (isConnected && webSocket != null) {
            webSocket.close(1000, "Client is disconnecting");
            isConnected = false;
            Log.d(TAG, "WebSocket disconnected.");
        } else {
            Log.d(TAG, "WebSocket is not connected.");
        }
    }

    public boolean connectBlocking(int timeout, TimeUnit unit) throws InterruptedException {
        connect();
        return connectLatch.await(timeout, unit);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
