package examples.zcat.zcatandroidsamples.utils;

import examples.zcat.zcatandroidsamples.rest.zcashforum.ZcashForumAPI;

public class HttpClientUtils {
    private static final boolean WITH_TOR = true, WITHOUT_TOR = false;

    private static ZcashForumAPI zcashForumApi, zcashForumApiWithTor;
    public static synchronized ZcashForumAPI getZcashForumApiInstance(boolean withTor) {
        if (withTor) {
            if (zcashForumApiWithTor == null) {
                zcashForumApiWithTor = new ZcashForumAPI(WITH_TOR);
            }
            return zcashForumApiWithTor;
        }

        if (zcashForumApi == null) {
            zcashForumApi = new ZcashForumAPI(WITHOUT_TOR);
        }
        return zcashForumApi;
    }

}
