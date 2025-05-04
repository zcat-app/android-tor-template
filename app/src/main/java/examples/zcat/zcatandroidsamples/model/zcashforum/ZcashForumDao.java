package examples.zcat.zcatandroidsamples.model.zcashforum;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;


public class ZcashForumDao {

    public static void updateOrCreate(final List<ZcashForumTopic> zecPosts) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                         realm.copyToRealmOrUpdate(zecPosts);
                        }
                    }
            );
        } finally {
            realm.close();
        }
    }

    public static void updateOrCreate(final ZcashForumTopic zecPost) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(
                    new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(zecPost);
                        }
                    }
            );
        } finally {
            realm.close();
        }
    }

    public static void deleteAll() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(ZcashForumTopic.class)
                            .findAll()
                            .deleteAllFromRealm();
                }
            });
        } finally {
            realm.close();
        }
    }


    public static List<ZcashForumTopic> findAll() {
        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<ZcashForumTopic> query = realm.where(ZcashForumTopic.class);

        RealmResults<ZcashForumTopic> realmResult = query
                .sort("updatedAt", Sort.DESCENDING)
                .findAll();

        List<ZcashForumTopic> resultItems = new ArrayList<>();
        if (realmResult != null) {
            resultItems = realm.copyFromRealm(realmResult);
        }
        realm.close();
        return resultItems;
    }

}
