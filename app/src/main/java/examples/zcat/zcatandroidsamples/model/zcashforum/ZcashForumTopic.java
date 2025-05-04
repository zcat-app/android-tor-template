package examples.zcat.zcatandroidsamples.model.zcashforum;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ZcashForumTopic extends RealmObject {

    @PrimaryKey
    public int id;
    public String title;
    public String desc;
    public String imageUrl ;
    public String slug;
    public int posts;
    public int likes;
    public int views ;
    public long updatedAt;
    public long createdAt;

}
