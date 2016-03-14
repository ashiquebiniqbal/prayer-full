package net.fajarachmad.prayer.evaluation.fragment;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import net.fajarachmad.prayer.evaluation.adapter.EvaluationStatisticCalendarAdapter;

/**
 * Created by user on 3/12/2016.
 */
public class EvaluationStatisticCalendarFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new EvaluationStatisticCalendarAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }
}
