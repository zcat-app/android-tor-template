package examples.zcat.zcatandroidsamples.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FormattingUtils {

    public static SimpleDateFormat DD_MM_YYYY_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static SimpleDateFormat YYYY_MM_DD_DATE_TIME_ZONE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static long getTimestampFromDateAndTime(String stringDateTimeWithTimeZone) {
        SimpleDateFormat input = YYYY_MM_DD_DATE_TIME_ZONE_FORMAT;
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        String updated = stringDateTimeWithTimeZone;

        try {
            Date date = YYYY_MM_DD_DATE_TIME_ZONE_FORMAT.parse(updated);
            return date.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    public static String getDateFromTimestamp(long timestamp) {
        SimpleDateFormat format = DD_MM_YYYY_DATE_FORMAT;
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(timestamp));
    }
}
