package net.fajarachmad.prayer.common.util;

import android.content.Context;

/**
 * Created by user on 3/6/2016.
 */
public class ResourceUtil {

    public static String getValueByKey(Context ctx, int id, int valueId, String key) {
        int i = -1;
        for (String cc: ctx.getResources().getStringArray(id)) {
            i++;
            if (cc.equals(key))
                break;
        }
        return ctx.getResources().getStringArray(valueId)[i];
    }
}
