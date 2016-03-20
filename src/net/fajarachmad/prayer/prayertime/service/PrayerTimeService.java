package net.fajarachmad.prayer.prayertime.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.prayertime.wrapper.Location;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerTime;
import net.fajarachmad.prayer.common.notification.DismissButtonListener;
import net.fajarachmad.prayer.common.notification.NotificationPublisher;
import net.fajarachmad.prayer.common.receiver.PrayerTimeReceiver;
import net.fajarachmad.prayer.common.util.GPSTracker;
import net.fajarachmad.prayer.common.util.HttpRequestUtil;
import net.fajarachmad.prayer.common.util.PrayTime;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.google.gson.Gson;

public class PrayerTimeService extends IntentService implements AppConstant {

	private SharedPreferences sharedPrefs;
	private List<String> tuningValues;
	private CountDownTimer timer;
	private AlarmManager alarmManager;
	private static MediaPlayer mediaPlayer;
	
	private Prayer prayer;
	private Gson gson;

	public PrayerTimeService() {
		super("PrayerTimeService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		gson = new Gson();
		setLocale();
	}
	
	private void setLocale() {
		Locale locale = new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE)); 
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		String action = intent.getStringExtra(ACTION);
		
		setLocale();
		
		switch (action) {
		case ACTION_PLAY_SOUND:
			playAlarmSound(intent);
			break;
		case ACTION_STOP_SOUND:
			stopAlarmSound(intent);
		default:
			break;
		}
		
	}

	private void playAlarmSound(Intent intent) {
		String sound = intent.getStringExtra(NOTIFICATION_SOUND);
		Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/" + sound);
		mediaPlayer = MediaPlayer.create(getApplicationContext(), alarmSound);
		mediaPlayer.start();
	}
	
	private void stopAlarmSound(Intent intent) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

}
