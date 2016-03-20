package net.fajarachmad.prayer.common.adapter;

import java.util.HashMap;
import java.util.Map;

import net.fajarachmad.prayer.prayertime.fragment.LocationInfoFragment;
import net.fajarachmad.prayer.prayertime.fragment.PrayerInfoFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
	
	private static final int NUM_PAGES = 2;
	
	private String nextPrayerName;
	private String nextPrayerTime;
	private String remainigTime;
	private String upcomingPray;
	private String locationName;
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Fragment> fragmentMap = new HashMap<>();

	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
    public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = getLocationInfoFragment();
			break;
		case 1:
			fragment = getPrayerInfoFragment();
			break;
		default:
			break;
		}
		fragmentMap.put(position, fragment);
		return fragment;
	}
	
	private Fragment getPrayerInfoFragment() {
		PrayerInfoFragment fragment = new PrayerInfoFragment();
        Bundle args = new Bundle();
        args.putString("nextPrayerName", nextPrayerName);
        args.putString("nextPrayerTime", nextPrayerTime);
        args.putString("remainingTime", remainigTime);
        args.putString("upcomingPray", upcomingPray);
        fragment.setArguments(args);
        
        return fragment;
	}
	
	private Fragment getLocationInfoFragment() {
		LocationInfoFragment fragment = new LocationInfoFragment();
		Bundle args = new Bundle();
        args.putString("locationName", locationName);
        fragment.setArguments(args);
        
        return fragment;
	}

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	super.destroyItem(container, position, object);
    	fragmentMap.remove(position);
    }
    
    @Override
    public Object instantiateItem(ViewGroup content, int position) {
    	Fragment fragment = null;
    	switch (position) {
		case 0:
			fragment = (LocationInfoFragment)super.instantiateItem(content, position);
			break;
		case 1:
			fragment = (PrayerInfoFragment)super.instantiateItem(content, position);
			break;
		default:
			break;
		}
    	
    	fragmentMap.put(position, fragment);
    	return fragment;
    }
    
    public Fragment getFragment(int position){
    	return fragmentMap.get(position);
    }


	public void setNextPrayerName(String nextPrayerName) {
		this.nextPrayerName = nextPrayerName;
	}

	public void setNextPrayerTime(String nextPrayerTime) {
		this.nextPrayerTime = nextPrayerTime;
	}

	public void setRemainigTime(String remainigTime) {
		this.remainigTime = remainigTime;
	}

	public void setUpcomingPray(String upcomingPray) {
		this.upcomingPray = upcomingPray;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
    
}
