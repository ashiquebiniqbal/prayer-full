package net.fajarachmad.prayer.prayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayer.adapter.PrayerItemAdapter;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;
import net.fajarachmad.prayer.prayer.wrapper.PrayerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerFragment extends AbstractPrayerFragment{

    private RecyclerView mRecyclerView;
    private List<Prayer> prayerList;
    private PrayerItemAdapter prayerItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(R.string.dailydoa);
        showParentToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prayer, container, false);
        initPrayerListItem(view);
        return view;
    }

    private void initPrayerListItem(View rootView) {
        populatePrayerData();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.prayer_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        prayerItemAdapter = new PrayerItemAdapter(this, prayerList);
        mRecyclerView.setAdapter(prayerItemAdapter);
    }

    private void populatePrayerData() {
        prayerList = new ArrayList<>();
        for (int i=0; i<=5; i++) {
            Prayer prayer = new Prayer();
            prayer.setTitle("Prayer " + i);
            prayer.setDescription("Description of prayer " + i);
            List<PrayerItem> items = new ArrayList<>();
            items.add(new PrayerItem("SubTitle","This is content of prayer "+i));
            items.add(new PrayerItem("SubTitle","This is content of prayer "+i));
            prayer.setPrayerItems(items);
            prayerList.add(prayer);
        }
    }
}
