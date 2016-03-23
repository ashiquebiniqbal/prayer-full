package net.fajarachmad.prayer.prayer.dao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/24/2016.
 */
public class PrayerDAO {

    private static PrayerDAO ourInstance;
    private Context context;
    private Gson gson;

    public static PrayerDAO getInstance(Context context) {
        if (ourInstance == null)
            return new PrayerDAO(context);
        else return ourInstance;
    }

    private PrayerDAO(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public List<Prayer> getPrayers() {
        String content = readFromFile(R.raw.prayers);
        if (content != null) {
            Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();
            List<Prayer> prayers = gson.fromJson(content, listType);
            return prayers;
        } else {
            return new ArrayList<>();
        }
    }

    private String readFromFile(int resourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(PrayerDAO.class.getName(), "Error read file", e);
        }
        return null;
    }
}
