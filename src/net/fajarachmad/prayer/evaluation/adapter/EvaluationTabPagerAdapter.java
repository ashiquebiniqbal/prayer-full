package net.fajarachmad.prayer.evaluation.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;

import net.fajarachmad.prayer.evaluation.fragment.EvaluationReminderFragment;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationStatisticFragment;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItem;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationTabPagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs = 2;
    private EvaluationItemWrapper evaluationItem;
    private Gson gson;

    public EvaluationTabPagerAdapter(FragmentManager fm, EvaluationItemWrapper evaluationItem) {

        super(fm);
        this.evaluationItem = evaluationItem;
        gson = new Gson();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = EvaluationStatisticFragment.newInstance(evaluationItem);
                break;
            case 1:
                fragment = new EvaluationReminderFragment(evaluationItem);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
