package utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas on 05.05.2015.
 */
public class DateUtil {

    private static DateFormat df = new SimpleDateFormat("yy_M_dd_HH_m");

    public static DateFormat getDateFormat() {
        return df;
    }

    public static String getCurrentFormattedDate() {
        return df.format(new Date());
    }
}
