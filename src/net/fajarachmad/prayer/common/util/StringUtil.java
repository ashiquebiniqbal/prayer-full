package net.fajarachmad.prayer.common.util;

/**
 * Created by user on 3/12/2016.
 */
public class StringUtil {

    public static boolean isBlank(String value) {
        if (value != null && !value.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
