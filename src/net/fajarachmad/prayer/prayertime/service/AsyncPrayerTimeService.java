package net.fajarachmad.prayer.prayertime.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.common.notification.DismissButtonListener;
import net.fajarachmad.prayer.common.notification.NotificationPublisher;
import net.fajarachmad.prayer.common.service.CallbackListener;
import net.fajarachmad.prayer.common.util.GPSTracker;
import net.fajarachmad.prayer.common.util.HttpRequestUtil;
import net.fajarachmad.prayer.common.util.PrayTime;
import net.fajarachmad.prayer.common.util.StringUtil;
import net.fajarachmad.prayer.prayertime.wrapper.Location;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerTime;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.fajarachmad.prayer.common.constant.AppConstant.*;

/**
 * Created by user on 3/19/2016.
 */
public class AsyncPrayerTimeService {

    private static String PRAYERTIME_DOCUMENT_ID = "prayertimes";
    public static String ACTION_UPDATE_NEXT_PRAYER = "action_update_next_prayer";
    public static String ACTION_LOCATION_UPDATED = "action_location_updated";

    private Context context;
    private static AsyncPrayerTimeService service;
    private List<String> tuningValues;
    private SharedPreferences sharedPrefs;
    private PrayTime prayerCalculator;
    private Map<String, String> prayerNameMap;
    private Gson gson = new Gson();
    private GPSTracker gpsTracker;
    private AlarmManager alarmManager;

    public static AsyncPrayerTimeService getInstance(Context context) {
        if (service == null) {
            service = new AsyncPrayerTimeService(context);
        }

        return service;
    }

    public  AsyncPrayerTimeService(Context context) {
        this.context = context;
        this.sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
        this.prayerCalculator = new PrayTime();
        this.prayerNameMap = new HashMap<>();
        this.gpsTracker = new GPSTracker(context);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        initTuningValue();
        initPrayerTimeNameMap();
    }

    public Location getCurrentLocation() {
        String fileContent = readFileContent(Location.class.getName());
        if (StringUtil.isBlank(fileContent)) {
            return null;
        } else {
            Location location = gson.fromJson(fileContent, Location.class);
            return  location;
        }
    }

    private String readFileContent(String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateCurrentLocation() {
        boolean isResourceAvailable = false;
        if (isGPSAvailable() && isNetworkAvailable()) {
            isResourceAvailable = true;
        }

        if (isResourceAvailable) {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        return determineLocationFromGps();
                    } catch (Exception e) {
                        return null;
                    }

                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    Location location = (Location)o;
                    save(location);
                    Intent intent = new Intent(ACTION_LOCATION_UPDATED);
                    context.sendBroadcast(intent);
                }
            };
            task.execute();
        }

        return isResourceAvailable;
    }

    public void save(Location location) {
        String data = gson.toJson(location);
        writeDataToFile(Location.class.getName(), data);
    }

    public Location determineLocationFromGps() throws Exception{
        Location location;
        List<Address> addresses = gpsTracker.getGeocoderAddress(context);
        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            location = convertToLocation(address);
            return location;
        }
        return null;
    }


    public Location convertToLocation(Address address) throws Exception{
        Location location = new Location();
        location.setAddressLine(address.getFeatureName());
        location.setCity(address.getSubAdminArea() != null ? address.getSubAdminArea() : address.getAdminArea());
        location.setCountry(address.getCountryName());
        location.setLatitude(address.getLatitude());
        location.setLongitude(address.getLongitude());
        location.setTimezone(getTimezone(address.getLatitude(), address.getLongitude()));
        return location;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isGPSAvailable() {
        return gpsTracker.getIsGPSTrackingEnabled();
    }

    public int getTimezone(double lat, double lng) throws Exception{
        Date date = new Date();
        String url = new StringBuilder()
                .append(GOOGLE_TIMEZONE_API)
                .append("location=")
                .append(lat)
                .append(",")
                .append(lng)
                .append("&timestamp=")
                .append(date.getTime() / 1000)
                .append("&key=")
                .append(API_KEY).toString();
        String jsonResult = HttpRequestUtil.GET(url);
        JSONObject jObj = new JSONObject(jsonResult);
        int rawOffset = jObj.getInt("rawOffset");
        return (rawOffset / 60 / 60);
    }

    public void getPrayertimes(double lat, double lng, int timezone, final CallbackListener listener) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    List<PrayerTime> prayerTimes = getPrayerTime(Double.valueOf(objects[0].toString()), Double.valueOf(objects[1].toString()), Integer.valueOf(objects[2].toString()));
                    save(prayerTimes);
                    return prayerTimes;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                try {
                    super.onPostExecute(o);
                    List<PrayerTime> prayerTimes = (List<PrayerTime>)o;
                    registerNotification(prayerTimes);
                    Map<String, Object> curPrayerMap =  getCurrentPrayer(prayerTimes);
                    PrayerTime nextPrayer = (PrayerTime)curPrayerMap.get("NEXT");
                    Intent intent = new Intent(ACTION_UPDATE_NEXT_PRAYER);
                    intent.putExtra(PrayerTime.class.getName(), gson.toJson(nextPrayer));
                    context.sendBroadcast(intent);

                    if (listener != null) {
                        listener.afterProcess(prayerTimes);
                    }
                } catch (Exception e) {
                    //TODO: show error
                }

            }
        };
        task.execute(lat, lng, timezone);
    }

    private void save(List<PrayerTime> prayerTimes) {
        String data = gson.toJson(prayerTimes);
        writeDataToFile(PRAYERTIME_DOCUMENT_ID, data);
    }

    private void writeDataToFile(String fileName, String data) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initPrayerTimeNameMap() {
        prayerNameMap.put(FAJR_ID, getResourceString(R.string.prayer_fajr_name));
        prayerNameMap.put(DHUHR_ID, getResourceString(R.string.prayer_dhuhr_name));
        prayerNameMap.put(ASR_ID, getResourceString(R.string.prayer_asr_name));
        prayerNameMap.put(MAGHRIB_ID, getResourceString(R.string.prayer_maghrib_name));
        prayerNameMap.put(ISHA_ID, getResourceString(R.string.prayer_isha_name));
    }

    private List<PrayerTime> getPrayerTime(double latitude, double longitude, int timezone) throws ParseException {
        List<PrayerTime> list = new ArrayList<>();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int[] offsets = new int[7];
        offsets[0] = resolveTuningValue(sharedPrefs.getFloat(PREF_TUNE_FAJR_KEY, 0.5f), tuningValues);
        offsets[1] = 0;
        offsets[2] = resolveTuningValue(sharedPrefs.getFloat(PREF_TUNE_DHUHR_KEY, 0.5f), tuningValues);
        offsets[3] = resolveTuningValue(sharedPrefs.getFloat(PREF_TUNE_ASR_KEY, 0.5f), tuningValues);
        offsets[4] = 0;
        offsets[5] = resolveTuningValue(sharedPrefs.getFloat(PREF_TUNE_MAGHRIB_KEY, 0.5f), tuningValues);
        offsets[6] = resolveTuningValue(sharedPrefs.getFloat(PREF_TUNE_ISHA_KEY, 0.5f), tuningValues);

        prayerCalculator.setCalcMethod(Integer.valueOf(sharedPrefs.getString(PREF_CALULATION_METHOD_KEY, DEFAULT_CALC_METHOD)));
        prayerCalculator.setAsrJuristic(Integer.valueOf(sharedPrefs.getString(PREF_ASR_METHOD_KEY, DEFAULT_ASR_METHOD)));
        prayerCalculator.tune(offsets);
        ArrayList<String> prayerTimes = prayerCalculator.getPrayerTimes(cal, latitude, longitude, timezone);
        ArrayList<String> prayerNames = prayerCalculator.getTimeNames();

        for (int i = 0; i < prayerTimes.size(); i++) {
            String prayId = prayerNames.get(i);
            Date prayTime = constructPrayerDateTime(prayerTimes.get(i));
            String prayName = prayerNameMap.get(prayId);

            if (!prayId.equals(SUNSET_ID) && !prayId.equals(SUNRISE_ID)) {
                list.add(new PrayerTime(prayId, prayName, prayerTimes.get(i), prayTime));
            }
        }

        return list;
    }

    private Date constructPrayerDateTime(String time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
        return cal.getTime();
    }

    private void initTuningValue() {
        tuningValues = new ArrayList<String>();
        for (int i = -60; i <= 60; i++) {
            tuningValues.add(String.valueOf(i));
        }
    }

    private int resolveTuningValue(float value, List<String> values) {
        int index = (int) (value * values.toArray(new String[0]).length);
        index = Math.min(index, values.toArray(new String[0]).length - 1);
        return Integer.valueOf(values.toArray(new String[0])[index]);

    }

    private void registerNotification(List<PrayerTime> prayerTimes) {
        int idx=1;
        for (PrayerTime prayerTime: prayerTimes) {
            evaluateNotification(idx, prayerTime.getPrayId(), prayerTime.getPrayName(), prayerTime.getPrayDate());
            idx++;
        }
    }

    private void evaluateNotification(int idx, String prayId, String prayName, Date date) {
        cancelNotification(idx);
        cancelNotification(idx+10);
        long delay = date.getTime() - new Date().getTime();
        if (delay > 0) {
            sendPrayAlarm(idx, prayId, prayName, delay);
        }
    }

    private void sendPrayAlarm(int id, String prayId, String parayerName, long delay){
        String message = getResourceString(R.string.notif_on_prayer)+" "+parayerName;
        String title = getResourceString(R.string.notif_title);
        boolean isNotificationDisable = sharedPrefs.getBoolean(PREF_DISABLED_NOTIFICATION_KEY, false);

        switch (prayId) {
            case FAJR_ID:
                cancelNotification(id+20);
                cancelNotification(id+10+20);
                if (!isNotificationDisable) {
                    String sound = null;
                    if (sharedPrefs.getBoolean(PREF_FAJR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM)) {
                        sound = sharedPrefs.getString(PREF_FAJR_ONPRAY_SOUND_KEY, DEFAULT_SOUND);
                    }
                    scheduleNotification(getNotification(title, message), id, Long.valueOf(delay).intValue(), sound);
                    int delayTomorrow = Long.valueOf(delay).intValue() + 86400000;
                    scheduleNotification(getNotification(title, message), id+20, delayTomorrow, sound);
                }

                if (sharedPrefs.getBoolean(PREF_FAJR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM) && !isNotificationDisable) {
                    int beforeTimeMinute = Integer.valueOf(sharedPrefs.getString(PREF_FAJR_BEFOREPRAY_NOTIFY_KEY, "0"));
                    long beforeTimeMilis = beforeTimeMinute * 60  * 1000;
                    long newDelay = delay - beforeTimeMilis;
                    if (newDelay > 0) {
                        String sound = sharedPrefs.getString(PREF_FAJR_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
                        message = beforeTimeMinute+" "+getResourceString(R.string.notif_before_pray)+" "+parayerName;
                        scheduleNotification(getNotification(title, message), id+10, Long.valueOf(newDelay).intValue(), sound);
                        int delayTomorrow = Long.valueOf(newDelay).intValue() + 86400000;
                        scheduleNotification(getNotification(title, message), id+10+20, delayTomorrow, sound);
                    }

                }
                break;
            case DHUHR_ID:
                if (!isNotificationDisable) {
                    String sound = null;
                    if (sharedPrefs.getBoolean(PREF_DHUHR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM)) {
                        sound = sharedPrefs.getString(PREF_DHUHR_ONPRAY_SOUND_KEY, DEFAULT_SOUND);
                    }
                    scheduleNotification(getNotification(title, message), id, Long.valueOf(delay).intValue(), sound);
                }
                if (sharedPrefs.getBoolean(PREF_DHUHR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM) && !isNotificationDisable) {
                    int beforeTimeMinute = Integer.valueOf(sharedPrefs.getString(PREF_DHUHR_BEFOREPRAY_NOTIFY_KEY, "0"));
                    long beforeTimeMilis = beforeTimeMinute * 60  * 1000;
                    long newDelay = delay - beforeTimeMilis;
                    if (newDelay > 0) {
                        String sound = sharedPrefs.getString(PREF_DHUHR_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
                        message = beforeTimeMinute+" "+getResourceString(R.string.notif_before_pray)+" "+parayerName;
                        scheduleNotification(getNotification(title, message), id+10, Long.valueOf(newDelay).intValue(), sound);
                    }
                }
                break;
            case ASR_ID:
                if (!isNotificationDisable) {
                    String sound = null;
                    if (sharedPrefs.getBoolean(PREF_ASR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM)) {
                        sound = sharedPrefs.getString(PREF_ASR_ONPRAY_SOUND_KEY, DEFAULT_SOUND);
                    }
                    scheduleNotification(getNotification(title, message), id, Long.valueOf(delay).intValue(), sound);
                }
                if (sharedPrefs.getBoolean(PREF_ASR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM) && !isNotificationDisable) {
                    int beforeTimeMinute = Integer.valueOf(sharedPrefs.getString(PREF_ASR_BEFOREPRAY_NOTIFY_KEY, "0"));
                    long beforeTimeMilis = beforeTimeMinute * 60  * 1000;
                    long newDelay = delay - beforeTimeMilis;
                    if (newDelay > 0) {
                        String sound = sharedPrefs.getString(PREF_ASR_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
                        message = beforeTimeMinute+" "+getResourceString(R.string.notif_before_pray)+" "+parayerName;
                        scheduleNotification(getNotification(title, message), id+10, Long.valueOf(newDelay).intValue(), sound);
                    }

                }
                break;
            case MAGHRIB_ID:
                if (!isNotificationDisable) {
                    String sound = null;
                    if (sharedPrefs.getBoolean(PREF_MAGHRIB_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM)) {
                        sound = sharedPrefs.getString(PREF_MAGHRIB_ONPRAY_SOUND_KEY, DEFAULT_SOUND);
                    }
                    scheduleNotification(getNotification(title, message), id, Long.valueOf(delay).intValue(), sound);
                }
                if (sharedPrefs.getBoolean(PREF_MAGHRIB_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM) && !isNotificationDisable) {
                    int beforeTimeMinute = Integer.valueOf(sharedPrefs.getString(PREF_MAGHRIB_BEFOREPRAY_NOTIFY_KEY, "0"));
                    long beforeTimeMilis = beforeTimeMinute * 60  * 1000;
                    long newDelay = delay - beforeTimeMilis;
                    if (newDelay > 0) {
                        String sound = sharedPrefs.getString(PREF_MAGHRIB_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
                        message = beforeTimeMinute+" "+getResourceString(R.string.notif_before_pray)+" "+parayerName;
                        scheduleNotification(getNotification(title, message), id+10, Long.valueOf(newDelay).intValue(), sound);
                    }
                }
                break;
            case ISHA_ID:
                if (!isNotificationDisable) {
                    String sound = null;
                    if (sharedPrefs.getBoolean(PREF_ISHA_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM)) {
                        sound = sharedPrefs.getString(PREF_ISHA_ONPRAY_SOUND_KEY, DEFAULT_SOUND);
                    }
                    scheduleNotification(getNotification(title, message), id, Long.valueOf(delay).intValue(), sound);
                }
                if (sharedPrefs.getBoolean(PREF_ISHA_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM) && !isNotificationDisable) {
                    int beforeTimeMinute = Integer.valueOf(sharedPrefs.getString(PREF_ISHA_BEFOREPRAY_NOTIFY_KEY, "0"));
                    long beforeTimeMilis = beforeTimeMinute * 60  * 1000;
                    long newDelay = delay - beforeTimeMilis;
                    if (newDelay > 0) {
                        String sound = sharedPrefs.getString(PREF_ISHA_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
                        message = beforeTimeMinute+" "+getResourceString(R.string.notif_before_pray)+" "+parayerName;
                        scheduleNotification(getNotification(title, message), id+10, Long.valueOf(newDelay).intValue(), sound);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void cancelNotification(int id) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    private void scheduleNotification(Notification notification, int id, int delay, String sound) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NOTIFICATION_SOUND, sound);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String title, String content) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Intent dismissIntent = new Intent(context, DismissButtonListener.class);
        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0,  dismissIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(contentIntent);
        builder.addAction(R.drawable.turn_notifications_off_button, getResourceString(R.string.notif_dismiss), pendingButtonIntent);
        return builder.build();
    }

    private Map<String, Object> getCurrentPrayer(List<PrayerTime> prayerTimes) throws ParseException {
        Log.i("Prayer", "Getting current prayer");
        Map<String, Object> map = new HashMap<>();
        Date dateFrom;
        Date dateTo;
        boolean solved = false;
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-d H:m");
        SimpleDateFormat dateOlnyFormat = new SimpleDateFormat("yyyy-MM-d");

        int i = 0;

        for (i = 0; i < prayerTimes.size() - 1; i++) {

            String prayTimeFrom = prayerTimes.get(i).getPrayTime();
            String prayNameFrom = prayerTimes.get(i).getPrayName();
            String prayIdFrom = prayerTimes.get(i).getPrayId();

            String prayTimeTo = prayerTimes.get(i + 1).getPrayTime();
            String prayNameTo = prayerTimes.get(i + 1).getPrayName();
            String prayIdTo = prayerTimes.get(i + 1).getPrayId();


            String dateFromStr = dateOlnyFormat.format(new Date()) + " " + prayTimeFrom + ":00";
            String dateToStr = dateOlnyFormat.format(new Date()) + " " + prayTimeTo + ":00";

            dateFrom = dateTimeFormat.parse(dateFromStr);
            dateTo = dateTimeFormat.parse(dateToStr);

            Log.d("Prayer", prayTimeFrom
                    + "-->" + prayTimeTo
                    + "-->" + dateFrom + "-->" + dateTo);

            if (new Date().compareTo(dateFrom) > 0 && new Date().compareTo(dateTo) < 0) {
                map.put("CURRENT",new PrayerTime(prayIdFrom,prayNameFrom, prayTimeFrom, dateFrom));
                map.put("NEXT", new PrayerTime(prayIdTo, prayNameTo, prayTimeTo, dateTo));
                solved = true;
                break;
            }
        }

        if (!solved) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 1);
            Date tomorrow = cal.getTime();

            String prayTimeFrom = prayerTimes.get(i).getPrayTime();
            String prayNameFrom = prayerTimes.get(i).getPrayName();
            String prayIdFrom = prayerTimes.get(i).getPrayId();

            String prayTimeTo = prayerTimes.get(0).getPrayTime();
            String prayNameTo = prayerTimes.get(0).getPrayName();
            String prayIdTo = prayerTimes.get(0).getPrayId();

            String dateFromStr = dateOlnyFormat.format(new Date()) + " " + prayTimeFrom + ":00";
            String dateToStr = dateOlnyFormat.format(tomorrow) + " " + prayTimeTo + ":00";

            dateFrom = dateTimeFormat.parse(dateFromStr);
            dateTo = dateTimeFormat.parse(dateToStr);

            map.put("CURRENT", new PrayerTime(prayIdFrom, prayNameFrom, prayTimeFrom, dateFrom));
            map.put("NEXT", new PrayerTime(prayIdTo, prayNameTo, prayTimeTo, dateTo));
        }

        return map;
    }

    private String getResourceString(int id) {
        return context.getResources().getString(id);
    }


}
