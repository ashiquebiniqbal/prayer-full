package net.fajarachmad.prayer.prayer.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import net.fajarachmad.prayer.prayer.fragment.PrayerDetailItemFragment;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;
import net.fajarachmad.prayer.prayertime.fragment.LocationInfoFragment;
import net.fajarachmad.prayer.prayertime.fragment.PrayerInfoFragment;

import java.util.HashMap;
import java.util.Map;

public class PrayerDetailPagerAdapter extends FragmentStatePagerAdapter{

	private static final int NUM_PAGES = 2;

	private Prayer prayer;

	public PrayerDetailPagerAdapter(FragmentManager fm, Prayer prayer) {
		super(fm);
		this.prayer = prayer;
	}

	@Override
    public Fragment getItem(int position) {
		return PrayerDetailItemFragment.newInstance(prayer.getPrayerItems().get(position));
	}
	
    @Override
    public int getCount() {
        return prayer.getPrayerItems().size();
    }
    
    @Override
    public Object instantiateItem(ViewGroup content, int position) {
    	return super.instantiateItem(content, position);
    }
    

    
}
