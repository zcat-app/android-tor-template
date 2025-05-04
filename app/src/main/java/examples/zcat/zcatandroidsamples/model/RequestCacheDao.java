package examples.zcat.zcatandroidsamples.model;

import java.util.Calendar;

import examples.zcat.zcatandroidsamples.enums.RequestTypeEnum;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RequestCacheDao {

    public static void updateOrCreate(final RequestCache request) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(request);
                    }
                }
            );
        } finally {
            realm.close();
        }
    }

    public static void deleteRequestById(final String detailId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RequestCache> rows = realm.where(RequestCache.class)
                            .equalTo("detailId", detailId).findAll();
                    rows.deleteAllFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }

    public static RequestCache findCachedRequest(RequestTypeEnum requestTypeEnum, String detailId) {
        final Realm realm = Realm.getDefaultInstance();
        RealmObject realmResult = realm.where(RequestCache.class)
                .equalTo("requestType", requestTypeEnum.getCode())
                .equalTo("detailId", detailId)
                .findFirst();
        RequestCache result = realmResult==null? null : (RequestCache)realm.copyFromRealm(realmResult);
        realm.close();
        return result;
    }


    public static void deleteCachedRequest(final RequestTypeEnum requestTypeEnum, final String detailId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RequestCache> rows = realm.where(RequestCache.class)
                            .equalTo("requestType", requestTypeEnum.getCode())
                            .equalTo("detailId", detailId)
                            .findAll();
                    rows.deleteAllFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }

    public static void deleteAllRequests() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(RequestCache.class).findAll().deleteAllFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }

    public static void deleteOldCachedRequests() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);

        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RequestCache> rows = realm.where(RequestCache.class)
                            .lessThan("lastTimestamp", c.getTimeInMillis())
                            .or()
                            .lessThan("lastSuccessfulTimestamp", c.getTimeInMillis())
                            .findAll();
                    rows.deleteAllFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }
}
