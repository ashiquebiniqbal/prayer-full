package net.fajarachmad.prayer.prayertime.wrapper;

import java.util.Map;

import android.graphics.drawable.Drawable;

import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerTime;

public class PrayerItemWrapper implements AppConstant {
	
	private String prayerId;
	private String prayerName;
	private String prayerTime;
	private boolean beforePrayerAlarm;
	private String beforePrayerTimeAlarm;
	private boolean onPrayerTimeAlarm;
	private Drawable prayerIcon;
	private Drawable onPrayerAlarmIcon;

	public PrayerItemWrapper(){}
	
	public PrayerItemWrapper(String prayerName, String prayerTime, String beforePrayerTimeAlarm, boolean onPrayerTimeAlarm) {
		this.prayerName = prayerName;
		this.prayerTime = prayerTime;
		this.beforePrayerTimeAlarm = beforePrayerTimeAlarm;
		this.onPrayerTimeAlarm = onPrayerTimeAlarm;
	}
	
	public PrayerItemWrapper(PrayerTime prayerTime, Map<String, Object> preferences) {
		this.prayerId = prayerTime.getPrayId();
		this.prayerName = prayerTime.getPrayName();
		this.prayerTime = prayerTime.getPrayTime();
		this.beforePrayerAlarm = (boolean) preferences.get(constructPrayerPrefKey(PREF_BEFOREPRAY_ALARM_KEY));
		this.onPrayerTimeAlarm = (boolean) preferences.get(constructPrayerPrefKey(PREF_ONPRAY_ALARM_KEY));
		this.beforePrayerTimeAlarm = preferences.get(constructPrayerPrefKey(PREF_BEFOREPRAY_NOTIFY_KEY)) +" "+ preferences.get("timeUnit");
		
		if (this.onPrayerTimeAlarm) {
			this.onPrayerAlarmIcon = (Drawable) preferences.get("iconOnprayerAlarmOn");
		} else {
			this.onPrayerAlarmIcon = (Drawable) preferences.get("iconOnprayerAlarmOff");
		}
		this.setPrayerIcon((Drawable) preferences.get(constructPrayerPrefKey(PRAYER_ICON_KEY)));
	}
	
	private String constructPrayerPrefKey(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(prayerId);
		sb.append("_");
		sb.append(key);
		return sb.toString();
	}
	
	public String getPrayerName() {
		return prayerName;
	}

	public void setPrayerName(String prayerName) {
		this.prayerName = prayerName;
	}

	public String getPrayerTime() {
		return prayerTime;
	}

	public void setPrayerTime(String prayerTime) {
		this.prayerTime = prayerTime;
	}

	public String getBeforePrayerTimeAlarm() {
		return beforePrayerTimeAlarm;
	}

	public void setBeforePrayerTimeAlarm(String beforePrayerTimeAlarm) {
		this.beforePrayerTimeAlarm = beforePrayerTimeAlarm;
	}

	public boolean isOnPrayerTimeAlarm() {
		return onPrayerTimeAlarm;
	}

	public void setOnPrayerTimeAlarm(boolean onPrayerTimeAlarm) {
		this.onPrayerTimeAlarm = onPrayerTimeAlarm;
	}

	public String getPrayerId() {
		return prayerId;
	}

	public void setPrayerId(String prayerId) {
		this.prayerId = prayerId;
	}

	public boolean isBeforePrayerAlarm() {
		return beforePrayerAlarm;
	}

	public void setBeforePrayerAlarm(boolean beforePrayerAlarm) {
		this.beforePrayerAlarm = beforePrayerAlarm;
	}

	public Drawable getOnPrayerAlarmIcon() {
		return onPrayerAlarmIcon;
	}

	public void setOnPrayerAlarmIcon(Drawable onPrayerAlarmIcon) {
		this.onPrayerAlarmIcon = onPrayerAlarmIcon;
	}

	public Drawable getPrayerIcon() {
		return prayerIcon;
	}

	public void setPrayerIcon(Drawable prayerIcon) {
		this.prayerIcon = prayerIcon;
	}
	
	

}
