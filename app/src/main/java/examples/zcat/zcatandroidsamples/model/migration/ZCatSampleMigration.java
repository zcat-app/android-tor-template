package examples.zcat.zcatandroidsamples.model.migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;


public class ZCatSampleMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        ZCatSampleMigration_v0_0_1.migrate(schema, oldVersion, newVersion);
    }
}