package examples.zcat.zcatandroidsamples.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Context context;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public OkHttpUrlLoader(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public boolean handles(@NonNull GlideUrl url) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(
            @NonNull GlideUrl model, int width, int height, @NonNull Options options) {
        return new LoadData<>(model, new OkHttpStreamFetcher(context, model));
    }

    /** The default factory for {@link OkHttpUrlLoader}s. */
    // Public API.
    @SuppressWarnings("WeakerAccess")
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final Context context;

        /** Constructor for a new Factory that runs requests using a static singleton client. */
        public Factory(final Context context) {
            this.context = context;
        }


        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new OkHttpUrlLoader(context);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }

}
