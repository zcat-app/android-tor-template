package examples.zcat.zcatandroidsamples.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestCache extends RealmObject {

    @PrimaryKey
    long id;

    int requestType;

    String detailId;

    Integer errorCode;

    long lastTimestamp;

    long lastSuccessfulTimestamp;

    public RequestCache() {
    }

    public RequestCache(long id, int requestType, String detailId, Integer errorCode, long lastTimestamp, long lastSuccessfulTimestamp) {
        this.id = id;
        this.requestType = requestType;
        this.detailId = detailId;
        this.errorCode = errorCode;
        this.lastTimestamp = lastTimestamp;
        this.lastSuccessfulTimestamp = lastSuccessfulTimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public long getLastSuccessfulTimestamp() {
        return lastSuccessfulTimestamp;
    }

    public void setLastSuccessfulTimestamp(long lastSuccessfulTimestamp) {
        this.lastSuccessfulTimestamp = lastSuccessfulTimestamp;
    }

    /**
     * Tests if it should be updated
     *
     * @param limit
     * @return
     */
    public boolean isObsolete(long limit) {
        return limit < new Date().getTime() - lastSuccessfulTimestamp;
    }
}
