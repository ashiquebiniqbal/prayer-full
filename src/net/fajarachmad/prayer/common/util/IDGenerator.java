package net.fajarachmad.prayer.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 3/11/2016.
 */
public class IDGenerator {

    private static IDGenerator generator;
    private static SimpleDateFormat dateFromatter;

    public static IDGenerator getInstance() {
        if (generator == null) {
            generator = new IDGenerator();
            dateFromatter = new SimpleDateFormat("yyMMddHHmmss");
        }

        return generator;
    }

    public String generate() {
        return dateFromatter.format(new Date());
    }
}
