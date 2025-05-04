package examples.zcat.zcatandroidsamples.ui.rest;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumTopic;

public class ZcashForumPostRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String ZCASHFORUM_PREFIX_URL = "https://forum.zcashcommunity.com/t/";
    private static final SimpleDateFormat DD_MM_YY_HH_MM_DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm");

    private final Context context;
    private final List<ZcashForumTopic> items;
    private final Consumer<String> visitConsumer;
    private final boolean isTorEnabled;

    public ZcashForumPostRecycleAdapter(final Context context,
                                        final List<ZcashForumTopic> items,
                                        final Consumer<String> visitConsumer,
                                        final boolean isTorEnabled) {
        this.context = context;
        this.items = items;
        this.visitConsumer = visitConsumer;
        this.isTorEnabled = isTorEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_item, parent, false);
        return new ZcashForumPostViewHolder(context, view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRow((ZcashForumPostViewHolder) viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    private void populateItemRow(ZcashForumPostViewHolder holder, int position) {

        final ZcashForumTopic zcashTopic = items.get(position);
        holder.datetime.setText(DD_MM_YY_HH_MM_DATE_FORMAT.format(new Date(zcashTopic.updatedAt)));


        if (zcashTopic.imageUrl != null) { //image = 0, video = 1
            holder.picture.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(zcashTopic.imageUrl)
                    .into(holder.picture);
            holder.picture.setAdjustViewBounds(true);
            holder.picture.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }
        else {
            //zcash
            holder.picture.setVisibility(View.GONE);
        }

        String content = "";
        if (zcashTopic.title != null && !zcashTopic.title.isEmpty()) {
            content = zcashTopic.title;
        }

        if (zcashTopic.desc != null && !zcashTopic.desc.isEmpty()) {
            content = "\n" + zcashTopic.title;
        }

        if (content.isEmpty()) {
            holder.content.setVisibility(View.GONE);
        }
        else {
            //embed
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(zcashTopic.title);
            holder.content.setMovementMethod(LinkMovementMethod.getInstance());
            //createCustomClicksOnLinks(context, holder.content, isTorEnabled);
        }

        //
        holder.likeLabel.setText(Integer.toString(zcashTopic.likes));
        holder.viewLabel.setText(Integer.toString(zcashTopic.views));
        holder.replyLabel.setText(Integer.toString(zcashTopic.posts));

        String link = ZCASHFORUM_PREFIX_URL + zcashTopic.slug;

        holder.itemView.setOnClickListener(v -> visitConsumer.accept(link));
        holder.shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "ZCASH FORUM POST: " + link);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            context.startActivity(shareIntent);
        });
    }

    public static class ZcashForumPostViewHolder extends RecyclerView.ViewHolder {

        public final TextView content;
        public final ImageView picture;

        public final TextView likeLabel;
        public final TextView viewLabel;
        public final TextView replyLabel;


        public final TextView datetime;
        public final ImageView shareButton;


        public ZcashForumPostViewHolder(final Context context, final @NonNull View itemView) {
            super(itemView);
            //setItemBackground(context, itemView);
            content =itemView.findViewById(R.id.content);
            picture=itemView.findViewById(R.id.picture);

            likeLabel=itemView.findViewById(R.id.forum_like_label);
            viewLabel=itemView.findViewById(R.id.forum_view_label);
            replyLabel=itemView.findViewById(R.id.forum_reply_label);

            datetime=itemView.findViewById(R.id.datetime);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}
