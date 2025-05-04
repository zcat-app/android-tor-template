package examples.zcat.zcatandroidsamples;

import android.app.Application;

import examples.zcat.zcatandroidsamples.model.migration.ZCatSampleMigration;
import examples.zcat.zcatandroidsamples.utils.SharedPreferencesUtils;
import examples.zcat.zcatandroidsamples.utils.TorUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final var context = getApplicationContext();

        //init realm db
        Realm.init(getApplicationContext());
        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder()
                .name("examples.zcat.zcatandroidsamples.realm");
        configBuilder = configBuilder.schemaVersion(1)
                .compactOnLaunch()
                .allowWritesOnUiThread(true)
                .migration(new ZCatSampleMigration());

        RealmConfiguration config = configBuilder.build();
        Realm.setDefaultConfiguration(config);

        //init http clients
        TorUtils.createHttpClientsInThread(SharedPreferencesUtils.isAppUseTor(context));
    }

}
