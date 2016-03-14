package net.fajarachmad.prayer.prayertime.fragment;

import static net.fajarachmad.prayer.common.constant.AppConstant.ACTION;
import static net.fajarachmad.prayer.common.constant.AppConstant.ACTION_GET_PRAYER_TIME;
import static net.fajarachmad.prayer.common.constant.AppConstant.ACTION_STOP_SOUND;
import static net.fajarachmad.prayer.common.constant.AppConstant.APP_SETTING_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.ASR_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_BEFOREPRAY_ALARM;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_LANGUAGE;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_NOTIFY_TIME;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_ONPRAY_ALARM;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_SOUND;
import static net.fajarachmad.prayer.common.constant.AppConstant.DHUHR_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.FAJR_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.ISHA_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.LOCATION_SETTING_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.MAGHRIB_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.NOTIFICATION_SETTING_ID;
import static net.fajarachmad.prayer.common.constant.AppConstant.PRAYER_ICON_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ASR_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ASR_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ASR_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ASR_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ASR_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_DHUHR_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_DHUHR_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_DHUHR_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_DHUHR_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_DHUHR_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_FAJR_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_FAJR_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_FAJR_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_FAJR_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_FAJR_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ISHA_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ISHA_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ISHA_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ISHA_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ISHA_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_LANGUAGE_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_MAGHRIB_BEFOREPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_MAGHRIB_BEFOREPRAY_NOTIFY_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_MAGHRIB_BEFOREPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_MAGHRIB_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_MAGHRIB_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ONPRAY_ALARM_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_ONPRAY_SOUND_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.REQUEST_CODE_COMPASS_PAGE;
import static net.fajarachmad.prayer.common.constant.AppConstant.REQUEST_CODE_MOSQUE_FINDER_PAGE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.prayertime.adapter.PrayerItemAdapter;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerItemWrapper;
import net.fajarachmad.prayer.common.adapter.ScreenSlidePagerAdapter;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayertime.wrapper.Location;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerTime;
import net.fajarachmad.prayer.prayertime.service.PrayerTimeService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;

public class PrayerTimeFragment extends AbstractPrayerFragment {
	
	private SharedPreferences sharedPrefs;
	private static PrayerTimeFragment fragment;
	private List<String> tuningValues;
	private List<PrayerItemWrapper> prayerItems;
	private Gson gson;
	private Prayer prayer;
	private Map<String, Object> prayerNotifPreferenceMap;
	private Location newLocation;
	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;
	private int currentViewPagerPage = 0;
	private int numViewPagerPages = 1;
	
	public static PrayerTimeFragment getInstance() {
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new Gson();
		fragment = this;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		populateTuningValue();
		setHasOptionsMenu(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		showParentToolbar();
        View rootView = inflater.inflate(R.layout.prayertime, container, false);
        setLocale();
        stopAlarmSound();
		closeNotification();
		
		initViewPager(rootView);
        
        setActionButtonListener(rootView);
        
		String prayerJson = sharedPrefs.getString(Prayer.class.getName(), gson.toJson(new Prayer()));
		prayer = gson.fromJson(prayerJson, Prayer.class);
		
		//populate location information
		setLocationInformation(rootView);
		
		//populate prayer info
		populatePrayerInfo(prayer);
		
		//populate prayer times
		initPrayerItems();
		populatePrayerItemList(rootView);
		
		changeViewPagerAutomatically();
		
		sendRequestToCalculatePrayerTime(newLocation);
		
		((MainActivity)getActivity()).setActivityTitle(getActivity().getResources().getString(R.string.title_prayer_time));
		
		newLocation = null;
		return rootView;
    }
	
	private void initViewPager(View rootView) {
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) rootView.findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(0);
	}
	
	private void changeViewPagerAutomatically() {
		final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
            	if (currentViewPagerPage == numViewPagerPages + 1) {
                    currentViewPagerPage = 0;
                }
            	mPager.setCurrentItem(currentViewPagerPage, true);
                currentViewPagerPage++;
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);
	}
	
	private void setActionButtonListener(final View rootView) {
		rootView.findViewById(R.id.btn_mosque_finder).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				((FloatingActionsMenu) rootView.findViewById(R.id.prayertime_floatingactionmenu)).collapseImmediately();
				MosqueFinderFragment mosqueFinderFragment = new MosqueFinderFragment();
				mosqueFinderFragment.setTargetFragment(PrayerTimeFragment.this, REQUEST_CODE_MOSQUE_FINDER_PAGE);
				 fragment.getFragmentManager().beginTransaction()
	                .replace(R.id.container, mosqueFinderFragment).addToBackStack(null)
	                .commit();
				
			}
		});
		
		rootView.findViewById(R.id.btn_compass).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				((FloatingActionsMenu) rootView.findViewById(R.id.prayertime_floatingactionmenu)).collapseImmediately();
				CompassFragment compassFragment = new CompassFragment();
				compassFragment.setTargetFragment(PrayerTimeFragment.this, REQUEST_CODE_COMPASS_PAGE);
				 fragment.getFragmentManager().beginTransaction()
	                .replace(R.id.container, compassFragment).addToBackStack(null)
	                .commit();
				
			}
		});
	}
	
	private void sendRequestToCalculatePrayerTime(Location newLocation) {
		Intent service = new Intent(getContext(), PrayerTimeService.class);
		service.putExtra(ACTION, ACTION_GET_PRAYER_TIME);
		if (newLocation != null) {
			service.putExtra(Location.class.getName(), gson.toJson(newLocation));
		}
		getContext().startService(service);
	}
	
	
	private void initPrayerItems() {
		populatePrayerNotificationPrefMap();
		prayerItems = new ArrayList<PrayerItemWrapper>();
		if (prayer != null && prayer.getPrayerTimes() != null) {
			for (PrayerTime prayerTime : prayer.getPrayerTimes()) {
				prayerItems.add(new PrayerItemWrapper(prayerTime, prayerNotifPreferenceMap));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void populatePrayerNotificationPrefMap () {
		prayerNotifPreferenceMap = new HashMap<String, Object>();
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(FAJR_ID, PREF_BEFOREPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_FAJR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(FAJR_ID, PREF_BEFOREPRAY_NOTIFY_KEY), sharedPrefs.getString(PREF_FAJR_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(FAJR_ID, PREF_ONPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_FAJR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(FAJR_ID, PRAYER_ICON_KEY), getContext().getResources().getDrawable(R.drawable.ic_fajr));
		
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(DHUHR_ID, PREF_BEFOREPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_DHUHR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(DHUHR_ID, PREF_BEFOREPRAY_NOTIFY_KEY), sharedPrefs.getString(PREF_DHUHR_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(DHUHR_ID, PREF_ONPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_DHUHR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(DHUHR_ID, PRAYER_ICON_KEY), getContext().getResources().getDrawable(R.drawable.ic_dhuhr));
		
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ASR_ID, PREF_BEFOREPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_ASR_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ASR_ID, PREF_BEFOREPRAY_NOTIFY_KEY), sharedPrefs.getString(PREF_ASR_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ASR_ID, PREF_ONPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_ASR_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ASR_ID, PRAYER_ICON_KEY), getContext().getResources().getDrawable(R.drawable.ic_ashar));
		
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(MAGHRIB_ID, PREF_BEFOREPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_MAGHRIB_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(MAGHRIB_ID, PREF_BEFOREPRAY_NOTIFY_KEY), sharedPrefs.getString(PREF_MAGHRIB_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(MAGHRIB_ID, PREF_ONPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_MAGHRIB_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(MAGHRIB_ID, PRAYER_ICON_KEY), getContext().getResources().getDrawable(R.drawable.ic_maghrib));
		
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ISHA_ID, PREF_BEFOREPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_ISHA_BEFOREPRAY_ALARM_KEY, DEFAULT_BEFOREPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ISHA_ID, PREF_BEFOREPRAY_NOTIFY_KEY), sharedPrefs.getString(PREF_ISHA_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ISHA_ID, PREF_ONPRAY_ALARM_KEY), sharedPrefs.getBoolean(PREF_ISHA_ONPRAY_ALARM_KEY, DEFAULT_ONPRAY_ALARM));
		prayerNotifPreferenceMap.put(constructPrayerPrefKey(ISHA_ID, PRAYER_ICON_KEY), getContext().getResources().getDrawable(R.drawable.ic_isha));
		
		prayerNotifPreferenceMap.put("iconOnprayerAlarmOn", getContext().getResources().getDrawable(R.drawable.volume_high));
		prayerNotifPreferenceMap.put("iconOnprayerAlarmOff", getContext().getResources().getDrawable(R.drawable.volume_mute));
		prayerNotifPreferenceMap.put("timeUnit", getContext().getResources().getString(R.string.pref_minutes));
	}
	
	private String constructPrayerPrefKey(String prayerId, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(prayerId);
		sb.append("_");
		sb.append(key);
		return sb.toString();
	}
	
	private void populatePrayerItemList(View rootView) {
		RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.prayer_item_rv);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PrayerItemAdapter mAdapter = new PrayerItemAdapter(this, prayerItems);
        mRecyclerView.setAdapter(mAdapter);
        
	}
	
	private void closeNotification() {
		NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
	
	private void stopAlarmSound() {
		Intent service = new Intent(getContext(), PrayerTimeService.class);
		service.putExtra(ACTION, ACTION_STOP_SOUND);
		getContext().startService(service);
	}
	
	private void populateTuningValue () {
		tuningValues = new ArrayList<String>();
		for (int i = -60; i <= 60; i++) {
			tuningValues.add(String.valueOf(i));
		}
	}
	
	private void setLocale() {
		Locale locale = new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE)); 
	    Locale.setDefault(locale);
	    Configuration config = new Configuration();
	    config.locale = locale;
	    getContext().getResources().updateConfiguration(config,getContext().getResources().getDisplayMetrics());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case APP_SETTING_ID:
			sendRequestToCalculatePrayerTime(newLocation);
			break;
		case NOTIFICATION_SETTING_ID:
			setNotificationSetting();
			sendRequestToCalculatePrayerTime(newLocation);
			break;
		case LOCATION_SETTING_ID:
			String locationJson = data.getStringExtra(Location.class.getName());
			newLocation = gson.fromJson(locationJson, Location.class);
			break;
		}
	}
	
	private void setNotificationSetting() {
		String notifPrayId = sharedPrefs.getString("NotifPrayId", null);
		
		Boolean prefOnPrayAlarm = sharedPrefs.getBoolean(PREF_ONPRAY_ALARM_KEY, false);
		String prefSoundOnPray = sharedPrefs.getString(PREF_ONPRAY_SOUND_KEY, null);
		Boolean prefBeforePrayAlarm = sharedPrefs.getBoolean(PREF_BEFOREPRAY_ALARM_KEY, false);
		String prefNotifyBefore = sharedPrefs.getString(PREF_BEFOREPRAY_NOTIFY_KEY, DEFAULT_NOTIFY_TIME);
		String prefSoundBeforePray = sharedPrefs.getString(PREF_BEFOREPRAY_SOUND_KEY, DEFAULT_SOUND);
		
		Editor editor = sharedPrefs.edit();
		
		switch (notifPrayId) {
		case FAJR_ID:
			editor.putBoolean(PREF_FAJR_ONPRAY_ALARM_KEY, prefOnPrayAlarm);
			editor.putString(PREF_FAJR_ONPRAY_SOUND_KEY, prefSoundOnPray);
			editor.putBoolean(PREF_FAJR_BEFOREPRAY_ALARM_KEY, prefBeforePrayAlarm);
			editor.putString(PREF_FAJR_BEFOREPRAY_NOTIFY_KEY, prefNotifyBefore);
			editor.putString(PREF_FAJR_BEFOREPRAY_SOUND_KEY, prefSoundBeforePray);
			break;
		
		case DHUHR_ID:
			editor.putBoolean(PREF_DHUHR_ONPRAY_ALARM_KEY, prefOnPrayAlarm);
			editor.putString(PREF_DHUHR_ONPRAY_SOUND_KEY, prefSoundOnPray);
			editor.putBoolean(PREF_DHUHR_BEFOREPRAY_ALARM_KEY, prefBeforePrayAlarm);
			editor.putString(PREF_DHUHR_BEFOREPRAY_NOTIFY_KEY, prefNotifyBefore);
			editor.putString(PREF_DHUHR_BEFOREPRAY_SOUND_KEY, prefSoundBeforePray);
			break;
		
		case ASR_ID:
			editor.putBoolean(PREF_ASR_ONPRAY_ALARM_KEY, prefOnPrayAlarm);
			editor.putString(PREF_ASR_ONPRAY_SOUND_KEY, prefSoundOnPray);
			editor.putBoolean(PREF_ASR_BEFOREPRAY_ALARM_KEY, prefBeforePrayAlarm);
			editor.putString(PREF_ASR_BEFOREPRAY_NOTIFY_KEY, prefNotifyBefore);
			editor.putString(PREF_ASR_BEFOREPRAY_SOUND_KEY, prefSoundBeforePray);
			break;
		
		case MAGHRIB_ID:
			editor.putBoolean(PREF_MAGHRIB_ONPRAY_ALARM_KEY, prefOnPrayAlarm);
			editor.putString(PREF_MAGHRIB_ONPRAY_SOUND_KEY, prefSoundOnPray);
			editor.putBoolean(PREF_MAGHRIB_BEFOREPRAY_ALARM_KEY, prefBeforePrayAlarm);
			editor.putString(PREF_MAGHRIB_BEFOREPRAY_NOTIFY_KEY, prefNotifyBefore);
			editor.putString(PREF_MAGHRIB_BEFOREPRAY_SOUND_KEY, prefSoundBeforePray);
			break;
			
		case ISHA_ID:
			editor.putBoolean(PREF_ISHA_ONPRAY_ALARM_KEY, prefOnPrayAlarm);
			editor.putString(PREF_ISHA_ONPRAY_SOUND_KEY, prefSoundOnPray);
			editor.putBoolean(PREF_ISHA_BEFOREPRAY_ALARM_KEY, prefBeforePrayAlarm);
			editor.putString(PREF_ISHA_BEFOREPRAY_NOTIFY_KEY, prefNotifyBefore);
			editor.putString(PREF_ISHA_BEFOREPRAY_SOUND_KEY, prefSoundBeforePray);
			break;
		default:
			break;
		}
		
		editor.commit();
	}
	
	
	
    public void renderPrayerValue(Prayer prayer) {
		if (getView() != null) {
			this.prayer = prayer; 
			setLocationInformation(getView());
			if (prayer.getPrayerTimes() != null) {
				initPrayerItems();
				populatePrayerItemList(getView());
				populatePrayerInfo(prayer);
			}
		}
	}
    
    private void populatePrayerInfo(Prayer prayer) {
    	String nextPrayerName = prayer.getNextPrayer() != null ? prayer.getNextPrayer().getPrayName() : "";
		String nextPrayerTime = prayer.getNextPrayer() != null ? prayer.getNextPrayer().getPrayTime() : "";
		
		PrayerInfoFragment page = (PrayerInfoFragment) mPagerAdapter.getFragment(1);
		if (page == null) {
			mPagerAdapter.setNextPrayerName(nextPrayerName);
			mPagerAdapter.setNextPrayerTime(nextPrayerTime);
			mPagerAdapter.setUpcomingPray(getResources().getString(R.string.upcoming_prayer));
		} else {
			page.updateValue(nextPrayerName, nextPrayerTime, getResources().getString(R.string.upcoming_prayer));
		}
    }
    
    private void setLocationInformation(View rootView) {
    	String locationName = prayer.getLocation().getCity()+", "+prayer.getLocation().getCountry();
    	LocationInfoFragment page = (LocationInfoFragment) mPagerAdapter.getFragment(0);
		if (page == null) {
			mPagerAdapter.setLocationName(locationName);
		} else {
			page.updateLocation(locationName);
		}
    	//((TextView) rootView.findViewById(R.id.location_address)).setText(prayer.getLocation().getCity()+", "+prayer.getLocation().getCountry());
    }
    
    public void updateRemainingTime(String value) {
    	if (getView() != null) {
    		PrayerInfoFragment page = (PrayerInfoFragment) mPagerAdapter.getFragment(1);
    		if (page == null) {
    			mPagerAdapter.setRemainigTime(value);
    		} else {
    			page.updateRemainingTime(value);
    		}
    	}
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.add(Menu.NONE, 12, Menu.NONE, getContext().getResources().getString(R.string.location_setting_title)).setIcon(R.drawable.prayer_pin_location_icon)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case 12:
    		LocationSettingFragment locationSettingFragment = new LocationSettingFragment();
			locationSettingFragment.setTargetFragment(PrayerTimeFragment.this, LOCATION_SETTING_ID);
			 fragment.getFragmentManager().beginTransaction()
                .replace(R.id.container, locationSettingFragment).addToBackStack(null)
                .commit();
    		break;
        }


    	return super.onOptionsItemSelected(item);
    }
}
