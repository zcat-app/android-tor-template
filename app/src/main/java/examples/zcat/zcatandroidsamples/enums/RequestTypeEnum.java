package examples.zcat.zcatandroidsamples.enums;


public enum RequestTypeEnum {

    ZCASH_FORUM_LATEST(1000, 10),

    ;

    //in minutes
    public final int limit;

    public final int code;

    //private final int desc;

    RequestTypeEnum(int code, int limit) {
        this.code = code;
        this.limit = limit;
        //this.desc = desc;
    }

    public long getLimitInMs() {
        return (long) limit * 60L * 1000L;
    }

    public int getCode() {
        return code;
    }
}
