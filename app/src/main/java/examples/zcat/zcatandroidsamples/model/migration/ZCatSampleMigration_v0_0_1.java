package examples.zcat.zcatandroidsamples.model.migration;

import io.realm.FieldAttribute;
import io.realm.RealmSchema;


public class ZCatSampleMigration_v0_0_1 {

    public static void migrate(RealmSchema schema, long oldVersion, long newVersion) {
        // in case of add/editing/removing of 'entities'
//        if (oldVersion < 2) {
//            schema.create("Free2ZSource")
//                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                    .addField("name", String.class);
//        }
    }
}