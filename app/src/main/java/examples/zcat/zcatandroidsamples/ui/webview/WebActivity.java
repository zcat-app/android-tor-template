package examples.zcat.zcatandroidsamples.ui.webview;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acsbendi.requestinspectorwebview.RequestInspectorWebViewClient;
import com.acsbendi.requestinspectorwebview.WebViewRequest;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.ui.tor.TorActivityWithRest;
import examples.zcat.zcatandroidsamples.utils.TorUtils;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WebActivity extends TorActivityWithRest {
    public static final String WEBVIEW_URL = "examples.zcat.zcatandroidsamples.WEBVIEW_URL";
    private static final String DEFAULT_URL = "https://check.torproject.org";
    private static final String UTF8_ENCODING = "UTF-8"; // Default encoding
    private static final String MIME_TYPE = "text/html"; // Default encoding
    private View loadingView;
    private View backButton;
    private WebView webView;
    private ImageView shareButton;
    private TextView urlView;

    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.web_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadingView = findViewById(R.id.loading);
        webView = findViewById(R.id.webView);
        backButton = findViewById(R.id.back);
        shareButton = findViewById(R.id.share_icon);
        urlView = findViewById(R.id.web_url);

        final var intent = getIntent();
        if (intent != null && intent.getStringExtra(WEBVIEW_URL) != null) {
            url = intent.getStringExtra(WEBVIEW_URL);
        }
        else {
            url = DEFAULT_URL;
        }

        urlView.setText(url);
        loadingView.setVisibility(VISIBLE);
        webView.setVisibility(GONE);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        shareButton.setOnClickListener(v -> {
            final var sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");

            final var shareIntent = Intent.createChooser(sendIntent, null);
            context.startActivity(shareIntent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        torProtectedRestCalls();
    }

    @Override
     protected void restCalls(boolean withTor) {
        if (isSafeContext(context)) {
            if (isTorEnabled) {
                    webView.setWebViewClient(new RequestInspectorWebViewClient(webView) {
                        @Override
                        public WebResourceResponse shouldInterceptRequest(@NonNull WebView view, @NonNull WebViewRequest request) {
                            try {
                                final var okHttpCall = createCall(request, withTor);
                                final var response = okHttpCall.execute();

                                final String contentType = response.header("content-type");
                                String encoding = UTF8_ENCODING;
                                String mimeType = MIME_TYPE;
                                if (contentType != null) {
                                    final String[] contentTypeParts = contentType.split(";");
                                    mimeType = findMimeType(contentTypeParts);
                                    encoding = findEncoding(contentTypeParts);
                                }

                                Map<String, String> map = new HashMap<String, String>();
                                Map<String, List<String>> mmap = response.headers().toMultimap();
                                response.headers().toMultimap().keySet().forEach(key -> map.put(key, String.join(";", mmap.get(key))));

                                return new WebResourceResponse(mimeType, encoding, response.code(), response.message(), map, response.body().byteStream());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return failedRequest();
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if (!isSafeContext(context) || loadingView.getVisibility() == GONE) {
                                return;
                            }

                            loadingView.setVisibility(GONE);
                            webView.setVisibility(VISIBLE);
                        }
                    });
            }
            else {
                webView.setWebViewClient(new WebViewClient() {
                     @Override
                     public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                         WebActivity.this.runOnUiThread(() -> {
                             if (loadingView.getVisibility() == GONE) {
                                 return;
                             }
                             loadingView.setVisibility(GONE);
                             webView.setVisibility(VISIBLE);
                         });

                         return super.shouldInterceptRequest(view, request);
                     }
                });
            }

            //settings
            WebSettings webViewSettings = webView.getSettings();
            final boolean allowJsAndOthers = true;
            webViewSettings.setJavaScriptEnabled(allowJsAndOthers);
            webViewSettings.setLoadsImagesAutomatically(allowJsAndOthers);
            webViewSettings.setDomStorageEnabled(allowJsAndOthers);
            webViewSettings.setOffscreenPreRaster(allowJsAndOthers);
            webViewSettings.setSafeBrowsingEnabled(true);

            // Load the URL
            webView.loadUrl(url);
        }
    }

    private Call createCall(final WebViewRequest request, final boolean withTor) {
        if (isSafeContext(context)) {
            final var headers = Headers.of(request.getHeaders());
            final var requestBuilder = new Request.Builder()
                    .headers(headers)
                    .url(request.getUrl());

            if (!request.getMethod().equals("GET")) {
                try {
                    final var contentType = request.getHeaders().get("content-type");
                    final var mediaType = contentType != null ? MediaType.parse(contentType) : null;
                    //Log.d("REQUEST BODY", "Input:\n" + request.getBody());
                    final var requestBodyString = request.getBody();
                    final var requestBody = RequestBody.create(mediaType, requestBodyString);
                    //Log.d("REQUEST BODY", "Output:\n" + requestBody);
                    requestBuilder.method(request.getMethod(), requestBody);
                } catch (Exception e) {
                    Log.e("REQUEST BODY", e.getMessage());
                    //requestBuilder.method(request.getMethod(), RequestBody.create(null, ""));
                }
            } else {
                requestBuilder.get();
            }

            final var okHttpRequest = requestBuilder.build();
            final var okHttpClient = TorUtils.getHttpClient(withTor);
            return okHttpClient.newCall(okHttpRequest);
        }
        return null;
    }

    private WebResourceResponse failedRequest() {
        return new WebResourceResponse("text/html", "UTF-8", 200, "Internal Error", Map.of(), new ByteArrayInputStream("Tor Connection Error".getBytes()));
    }

    private static String findMimeType(String[] contentTypeParts) {
        return contentTypeParts[0].trim();
    }

    private static String findEncoding(String[] contentTypeParts) {
        if (contentTypeParts.length > 1) {
            for (String part : contentTypeParts) {
                if (part.trim().startsWith("charset=")) {
                    return part.split("=")[1].trim();
                }
            }
        }

        return UTF8_ENCODING;
    }
}
