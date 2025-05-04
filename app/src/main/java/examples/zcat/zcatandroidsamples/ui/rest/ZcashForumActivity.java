package examples.zcat.zcatandroidsamples.ui.rest;

import static examples.zcat.zcatandroidsamples.utils.CustomTabsUtils.initChromeTabs;
import static examples.zcat.zcatandroidsamples.utils.ViewUtils.isSafeContext;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import examples.zcat.zcatandroidsamples.R;
import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumDao;
import examples.zcat.zcatandroidsamples.rest.Command;
import examples.zcat.zcatandroidsamples.ui.tor.TorActivityWithRest;
import examples.zcat.zcatandroidsamples.utils.HttpClientUtils;

public class ZcashForumActivity extends TorActivityWithRest {

    private TextView infoText;

    private RecyclerView resultRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private Parcelable resultListViewState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_activity);

        initChromeTabs(context);
        initResultList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSafeContext(context)) {
            infoText.setVisibility(View.VISIBLE);
            infoText.setText("Loading ...");
            resultRecyclerView.setVisibility(View.GONE);

            torProtectedRestCalls();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (resultRecyclerView.getLayoutManager() != null) {
            resultListViewState = resultRecyclerView.getLayoutManager().onSaveInstanceState();
        }
    }

    @Override
    protected void restCalls(boolean withTor) {
        mSwipeLayout.setRefreshing(true);
        HttpClientUtils.getZcashForumApiInstance(withTor).getLatest(new Command() {

            @Override
            public void success() {
                loadResultList(); //safeContext check inside
            }

            @Override
            public void fail() {
                if (isSafeContext(context)) {
                    loadResultList();
                    Toast.makeText(context, "Data fetching failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initResultList() {
        resultRecyclerView = findViewById(R.id.news_forum_result_list);
        infoText = findViewById(R.id.news_forum_result_no_record);
        mSwipeLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(() -> {
            resultListViewState = null;
            mSwipeLayout.setRefreshing(true);
            torProtectedRestCalls();
        });
        resultListViewState = null;
        resultRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadResultList() {
        if (isSafeContext(context)) {
            final var feedRecords = ZcashForumDao.findAll();

            if (feedRecords.isEmpty()) {
                infoText.setVisibility(View.VISIBLE);
                infoText.setText("No record found!");
                resultRecyclerView.setVisibility(View.GONE);
                mSwipeLayout.setRefreshing(false);
                return;
            }

            infoText.setVisibility(View.GONE);
            resultRecyclerView.setVisibility(View.VISIBLE);

            final var newsZcashForumRecycleAdapter = new ZcashForumPostRecycleAdapter(context, feedRecords, this::visitLink, isTorEnabled);
            final var layoutManager = new LinearLayoutManager(context);
            resultRecyclerView.setLayoutManager(layoutManager);
            resultRecyclerView.setAdapter(newsZcashForumRecycleAdapter);
            newsZcashForumRecycleAdapter.notifyDataSetChanged();

            if (resultListViewState!= null && resultRecyclerView.getLayoutManager() != null) {
                resultRecyclerView.getLayoutManager().onRestoreInstanceState(resultListViewState);
            }

            mSwipeLayout.setRefreshing(false);
        }
    }
}