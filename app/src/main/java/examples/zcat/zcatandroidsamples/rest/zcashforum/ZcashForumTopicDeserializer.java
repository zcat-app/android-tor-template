package examples.zcat.zcatandroidsamples.rest.zcashforum;

import static examples.zcat.zcatandroidsamples.utils.DeserializationUtils.getSafeInt;
import static examples.zcat.zcatandroidsamples.utils.DeserializationUtils.getSafeJsonArray;
import static examples.zcat.zcatandroidsamples.utils.DeserializationUtils.getSafeString;
import static examples.zcat.zcatandroidsamples.utils.FormattingUtils.getTimestampFromDateAndTime;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import examples.zcat.zcatandroidsamples.model.zcashforum.ZcashForumTopic;

public class ZcashForumTopicDeserializer implements JsonDeserializer<List<ZcashForumTopic>> {

    @Override
    public List<ZcashForumTopic> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject response = json.getAsJsonObject();

        JsonElement topicListObject = response.get("topic_list");
        if (topicListObject == null || !topicListObject.isJsonObject()) {
            Log.e("Zcash FORUM REST", "No topic_list found!");
            return Collections.emptyList();
        }

        List<ZcashForumTopic> topics = new ArrayList<>();
        JsonArray topicList = getSafeJsonArray(topicListObject.getAsJsonObject(), "topics");
        if (topicList != null) {
            for (JsonElement topicElement : topicList) {
                if (!topicElement.isJsonObject()) {
                    continue;
                }

                JsonObject topicObject = topicElement.getAsJsonObject();

                //                "id": 8,
                //                "title": "Welcome to the Zcash Forum!",
                //                "fancy_title": "Welcome to the Zcash Forum!",
                //                "slug": "welcome-to-the-zcash-forum",
                //                "posts_count": 28,
                //                "reply_count": 11,
                //                "highest_post_number": 35,
                //                "image_url": null,
                //                "created_at": "2016-01-12T09:43:13.956Z",
                //                "last_posted_at": "2025-01-23T06:45:21.211Z",
                //                "bumped": true,
                //                "bumped_at": "2025-01-23T06:45:21.211Z",
                //                "archetype": "regular",
                //                "unseen": false,
                //                "pinned": true,
                //                "unpinned": null,
                //                "excerpt": "We are happy to have you here! :heart: \nThis is a community for discussing :zcash: Zcash, an open-source, decentralized cryptocurrency that offers complete and flexible confidentiality. \nPlease read our Community Guideli&hellip;",
                //                "visible": true,
                //                "closed": false,
                //                "archived": false,
                //                "bookmarked": null,
                //                "liked": null,
                //                "tags_descriptions": {},
                //                "views": 12623,
                //                "like_count": 91,
                //                "has_summary": false,
                //                "last_poster_username": "akira",
                //                "category_id": 1,
                //                "pinned_globally": true,
                //                "featured_link": null,
                //                "has_accepted_answer": false,
                //                "can_vote": false,
                //                "posters": [...]

                ZcashForumTopic zcashForumTopic = new ZcashForumTopic();
                zcashForumTopic.id = getSafeInt(topicObject, "id");
                zcashForumTopic.title = getSafeString(topicObject, "title");
                zcashForumTopic.desc = getSafeString(topicObject, "excerpt");
                zcashForumTopic.imageUrl = getSafeString(topicObject, "image_url");
                //https://forum.zcashcommunity.com/t/
                zcashForumTopic.slug = getSafeString(topicObject, "slug");
                zcashForumTopic.posts = getSafeInt(topicObject, "posts_count");
                zcashForumTopic.likes = getSafeInt(topicObject, "like_count");
                zcashForumTopic.views = getSafeInt(topicObject, "views");

                zcashForumTopic.updatedAt = getTimestampFromDateAndTime(getSafeString(topicObject, "last_posted_at"));
                zcashForumTopic.createdAt = getTimestampFromDateAndTime(getSafeString(topicObject, "created_at"));
                if (zcashForumTopic.updatedAt == 0L) {
                    zcashForumTopic.updatedAt = zcashForumTopic.createdAt;
                }


                topics.add(zcashForumTopic);
            }
        }

        return topics;
    }
}
