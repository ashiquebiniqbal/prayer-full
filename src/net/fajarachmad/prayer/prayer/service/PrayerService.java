package net.fajarachmad.prayer.prayer.service;

import android.content.Context;

import net.fajarachmad.prayer.prayer.dao.PrayerDAO;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;

import java.util.List;

/**
 * Created by user on 3/24/2016.
 */
public class PrayerService {
    private static PrayerService ourInstance;

    private Context context;
    private PrayerDAO prayerDAO;

    public static PrayerService getInstance(Context context) {

        if (ourInstance == null)
            return new PrayerService(context);
        else
            return ourInstance;

    }

    private PrayerService(Context context) {
        this.context = context;
        this.prayerDAO = PrayerDAO.getInstance(context);
    }

    public List<Prayer> getPrayers() {
        return prayerDAO.getPrayers();
    }
}
