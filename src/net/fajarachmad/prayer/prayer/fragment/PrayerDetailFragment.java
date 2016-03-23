package net.fajarachmad.prayer.prayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.adapter.ScreenSlidePagerAdapter;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayer.adapter.PrayerDetailPagerAdapter;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerDetailFragment extends AbstractPrayerFragment {

    private ViewPager mPager;
    private PrayerDetailPagerAdapter mPagerAdapter;
    private Prayer prayer;
    private Gson gson;

    public static PrayerDetailFragment newInstance(Prayer prayer) {
        PrayerDetailFragment f = new PrayerDetailFragment();
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString(Prayer.class.getName(), gson.toJson(prayer));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        if (getArguments() != null) {
            prayer = gson.fromJson(getArguments().getString(Prayer.class.getName()), Prayer.class);
        }
        hideParentToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prayer_detail, container, false);
        initViewPager(view);
        setCustomToolbar(view, prayer.getTitle());
        return view;
    }

    private void initViewPager(View rootView) {
        mPager = (ViewPager) rootView.findViewById(R.id.prayer_detail_pager);
        mPagerAdapter = new PrayerDetailPagerAdapter(getChildFragmentManager(), prayer);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);
    }
}
