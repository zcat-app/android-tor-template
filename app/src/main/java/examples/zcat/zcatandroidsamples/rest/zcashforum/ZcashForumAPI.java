package examples.zcat.zcatandroidsamples.rest.zcashforum;

import static examples.zcat.zcatandroidsamples.enums.RequestTypeEnum.ZCASH_FORUM_LATEST;
import static examples.zcat.zcatandroidsamples.utils.RequestUtils.failedRequest;
import static examples.zcat.zcatandroidsamples.utils.TorUtils.createRetrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

import examples.zcat.zcatandroidsamples.model.RequestCache;
import examples.zcat.zcatandroidsamples.model.RequestCacheDao;
import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumDao;
import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumTopic;
import examples.zcat.zcatandroidsamples.rest.Command;
import examples.zcat.zcatandroidsamples.utils.TorUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZcashForumAPI {
    private IZcashForumAPI zcashForumAPI;
    private Gson gson;

    public ZcashForumAPI(boolean withTor) {
        gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<ZcashForumTopic>>(){}.getType(), new ZcashForumTopicDeserializer())
                .create();
        zcashForumAPI = createRetrofit("https://forum.zcashcommunity.com/", TorUtils.getHttpClient(withTor), gson)
                .create(IZcashForumAPI.class);
    }

    public void getLatest(final Command command) {
        // check if we need to update values
        final RequestCache cachedRequest = RequestCacheDao.findCachedRequest(ZCASH_FORUM_LATEST, null);
        if (cachedRequest == null || cachedRequest.isObsolete(ZCASH_FORUM_LATEST.getLimitInMs())) {
            Callback zcashForumCallback = new Callback<List<ZcashForumTopic>>() {
                @Override public void onResponse(Call<List<ZcashForumTopic>> call, Response<List<ZcashForumTopic>> response) {
                    long timestamp = Calendar.getInstance().getTimeInMillis();
                    if(response.isSuccessful()) {
                        List<ZcashForumTopic> result = response.body();
                        if (result != null && !result.isEmpty()) {
                            ZcashForumDao.updateOrCreate(result);
                            RequestCacheDao.updateOrCreate(
                                    new RequestCache(
                                            cachedRequest == null? timestamp : cachedRequest.getId(),
                                            ZCASH_FORUM_LATEST.getCode(),
                                            null,
                                            null,
                                            timestamp,
                                            timestamp)
                            );
                            command.success();
                            return;
                        }
                    }
                    failedRequest(command, null, cachedRequest, ZCASH_FORUM_LATEST, null, response.code());
                    command.fail();
                    Log.e("ERROR", response.toString());

                }

                @Override public void onFailure(Call<List<ZcashForumTopic>> call, Throwable t) {
                    failedRequest(command, null, cachedRequest, ZCASH_FORUM_LATEST, null, 0);
                    command.fail();
                    t.printStackTrace();
                }
            };
            zcashForumAPI.getLatest().enqueue(zcashForumCallback);
        }
        else {
            command.success();
        }
    }

}
