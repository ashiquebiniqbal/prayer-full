package net.fajarachmad.prayer.evaluation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.evaluation.adapter.EvaluationTabPagerAdapter;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationDetailFragment extends AbstractPrayerFragment {

    private EvaluationItemWrapper selectedData;
    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        gson = new Gson();
        hideParentToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.evaluation_detail, container, false);
        setHasOptionsMenu(true);

        String selectedDataStr = getArguments().getString(EvaluationItemWrapper.class.getName());
        selectedData = gson.fromJson(selectedDataStr, EvaluationItemWrapper.class);

        String subtitle = new StringBuilder()
                .append(getContext().getResources().getString(R.string.progress))
                .append(": ").append(selectedData.getProgressString()).toString();
        setCustomToolbar(rootView, selectedData.getGoalName(), subtitle);
        setupTab(rootView);

        return rootView;
    }

    private void setupTab(View view) {
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.evaluation_detail_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Statistic"));
        tabLayout.addTab(tabLayout.newTab().setText("Reminder"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.evaluation_detail_pager);
        final EvaluationTabPagerAdapter adapter = new EvaluationTabPagerAdapter(getChildFragmentManager(), selectedData);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

