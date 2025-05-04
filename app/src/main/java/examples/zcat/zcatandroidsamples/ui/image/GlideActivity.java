package examples.zcat.zcatandroidsamples.ui.image;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.ui.tor.TorActivity;

public class GlideActivity extends TorActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.glide_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ImageView imageView = findViewById(R.id.imageView);
        Glide.with(context)
                .load("https://assets.coingecko.com/coins/images/486/large/circle-zcash-color.png")
                .fitCenter()
                .circleCrop()
                .into(imageView);
    }
}