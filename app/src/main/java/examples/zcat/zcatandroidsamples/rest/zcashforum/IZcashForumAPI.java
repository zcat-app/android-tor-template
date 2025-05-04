package examples.zcat.zcatandroidsamples.rest.zcashforum;

import java.util.List;

import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumTopic;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IZcashForumAPI {

    @GET("latest.json")
    Call<List<ZcashForumTopic>> getLatest();

}
