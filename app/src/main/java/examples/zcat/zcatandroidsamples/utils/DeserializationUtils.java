package examples.zcat.zcatandroidsamples.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DeserializationUtils {

    public static JsonObject getSafeJsonObject(JsonObject jsonObject, String attribute) {
        JsonElement element = jsonObject.get(attribute);
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        return null;
    }

    public static JsonArray getSafeJsonArray(JsonObject jsonObject, String attribute) {
        JsonElement element = jsonObject.get(attribute);
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        return null;
    }

    public static String getSafeString(JsonObject jsonObject, String attribute) {
        JsonElement element = jsonObject.get(attribute);
        if (element != null && element.isJsonPrimitive()) {
            return element.getAsString();
        }
        return null;
    }

    public static int getSafeInt(JsonObject jsonObject, String attribute) {
        JsonElement element = jsonObject.get(attribute);
        if (element != null && element.isJsonPrimitive()) {
            return element.getAsInt();
        }
        return 0;
    }

}
