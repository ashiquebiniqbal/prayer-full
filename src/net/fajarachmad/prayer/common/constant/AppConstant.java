package net.fajarachmad.prayer.common.constant;

public interface AppConstant {
	
	String GOOGLE_TIMEZONE_API = "https://maps.googleapis.com/maps/api/timezone/json?";
	String GOOGLE_NEARBY_PLACE_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	String API_KEY = "AIzaSyAZVavLEgDEwXa-iOwRu_hmnco7X-YbNBI";
	
	int NOTIFICATION_SETTING_ID = 2;
	int APP_SETTING_ID = 1;
	int LOCATION_SETTING_ID = 3;
	int REQUEST_CODE_COMPASS_PAGE = 4;
	int REQUEST_CODE_MOSQUE_FINDER_PAGE = 5;
	
	String ACTION = "action";
	String ACTION_PLAY_SOUND = "net.fajarachmad.prayer.play_sound";
	String ACTION_STOP_SOUND = "net.fajarachmad.prayer.stop_sound";
	String ACTION_GET_PRAYER_TIME = "net.fajarachmad.prayer.get_prayer_time";
	String ACTION_PRAYER_TIME_CHANGED = "net.fajarachmad.prayer.prayer_time_changed";
	String ACTION_REMAINING_TIME_CHANGED = "net.fajarachmad.prayer.remaining_time_changed";
	
	String NOTIFICATION_SOUND = "notificationSound";
	
	String FAJR_ID = "fajr";
	String ASR_ID = "asr";
	String DHUHR_ID = "dhuhr";
	String MAGHRIB_ID = "maghrib";
	String ISHA_ID = "isha";
	String SUNSET_ID = "sunset";
	String SUNRISE_ID = "sunrise";
	
	String PRAYER_ICON_KEY = "prayerIconKey";
	
	String DEFAULT_SOUND = "azan1";
	String DEFAULT_NOTIFY_TIME = "5";
	String DEFAULT_LANGUAGE = "en";
	String DEFAULT_CALC_METHOD = "3";
	String DEFAULT_ASR_METHOD = "0";
	float DEFAULT_MANUAL_TUNE = (float) 0.5;
	double DEFAULT_LATITUDE = -6.2087634;
	double DEFAULT_LONGITUDE = 106.84559899999999;
	int DEFAULT_TIMEZONE = 7;
	boolean DEFAULT_ONPRAY_ALARM = true;
	boolean DEFAULT_BEFOREPRAY_ALARM = true;
	boolean DEFAULT_AUTO_DETECT_LOCATION = true;
	
	String PREF_LANGUAGE_KEY = "prefLanguage";
	String PREF_CALULATION_METHOD_KEY = "prefCalculationMethod";
	String PREF_ASR_METHOD_KEY = "prefAsrMethod";
	String PREF_DISABLED_NOTIFICATION_KEY = "prefDisabledNotification";
	String PREF_AUTODETECT_LOCATION_KEY = "prefAutoDetectLocation";
	
	String PREF_TUNE_FAJR_KEY = "tune_fajr";
	String PREF_TUNE_DHUHR_KEY = "tune_dhuhr";
	String PREF_TUNE_ASR_KEY = "tune_asr";
	String PREF_TUNE_MAGHRIB_KEY = "tune_maghrib";
	String PREF_TUNE_ISHA_KEY = "tune_isha";
	String PREF_TUNE_RESET_KEY = "tune_reset";
	
	String PREF_ONPRAY_ALARM_KEY = "prefOnPrayAlarm";
	String PREF_ONPRAY_SOUND_KEY = "prefSoundOnPray";
	String PREF_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarm";
	String PREF_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBefore";
	String PREF_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePray";
	
	String PREF_FAJR_ONPRAY_ALARM_KEY = "prefOnPrayAlarmFajr";
	String PREF_FAJR_ONPRAY_SOUND_KEY = "prefSoundOnPrayFajr";
	String PREF_FAJR_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarmFajr";
	String PREF_FAJR_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBeforeFajr";
	String PREF_FAJR_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePrayFajr";
	
	String PREF_DHUHR_ONPRAY_ALARM_KEY = "prefOnPrayAlarmDhuhr";
	String PREF_DHUHR_ONPRAY_SOUND_KEY = "prefSoundOnPrayDhuhr";
	String PREF_DHUHR_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarmDhuhr";
	String PREF_DHUHR_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBeforeDhuhr";
	String PREF_DHUHR_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePrayDhuhr";
	
	String PREF_ASR_ONPRAY_ALARM_KEY = "prefOnPrayAlarmAsr";
	String PREF_ASR_ONPRAY_SOUND_KEY = "prefSoundOnPrayAsr";
	String PREF_ASR_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarmAsr";
	String PREF_ASR_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBeforeAsr";
	String PREF_ASR_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePrayAsr";
	
	String PREF_MAGHRIB_ONPRAY_ALARM_KEY = "prefOnPrayAlarmMaghrib";
	String PREF_MAGHRIB_ONPRAY_SOUND_KEY = "prefSoundOnPrayMaghrib";
	String PREF_MAGHRIB_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarmMaghrib";
	String PREF_MAGHRIB_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBeforeMaghrib";
	String PREF_MAGHRIB_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePrayMaghrib";
	
	String PREF_ISHA_ONPRAY_ALARM_KEY = "prefOnPrayAlarmIsha";
	String PREF_ISHA_ONPRAY_SOUND_KEY = "prefSoundOnPrayIsha";
	String PREF_ISHA_BEFOREPRAY_ALARM_KEY = "prefBeforePrayAlarmIsha";
	String PREF_ISHA_BEFOREPRAY_NOTIFY_KEY = "prefNotifyBeforeIsha";
	String PREF_ISHA_BEFOREPRAY_SOUND_KEY = "prefSoundBeforePrayIsha";
}
