package net.fajarachmad.prayer.prayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;
import net.fajarachmad.prayer.prayer.wrapper.PrayerItem;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerDetailItemFragment extends AbstractPrayerFragment{

    private PrayerItem prayerItem;

    public static PrayerDetailItemFragment newInstance(PrayerItem prayerItem) {
        PrayerDetailItemFragment f = new PrayerDetailItemFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString(PrayerItem.class.getName(), gson.toJson(prayerItem));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        if (getArguments() != null) {
            prayerItem = gson.fromJson(getArguments().getString(PrayerItem.class.getName()), PrayerItem.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prayer_detail_item, container, false);

        TextView content = (TextView)view.findViewById(R.id.prayer_detail_item_content);
        content.setText(prayerItem.getContent());
        return view;
    }
}
