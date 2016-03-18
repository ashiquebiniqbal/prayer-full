package net.fajarachmad.prayer.evaluation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.evaluation.adapter.EvaluationReminderItemAdapter;
import net.fajarachmad.prayer.evaluation.service.EvaluationService;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItem;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;
import net.fajarachmad.prayer.evaluation.wrapper.ReminderItemWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationReminderFragment extends AbstractPrayerFragment {

    private List<ReminderItemWrapper> reminderItems;
    private Fragment fragment;
    private EvaluationItemWrapper evaluationItem;
    private Gson gson;
    private EvaluationService evaluationService;


    public EvaluationReminderFragment(){}

    public static EvaluationReminderFragment newInstance(EvaluationItemWrapper evaluationItem) {
        EvaluationReminderFragment f = new EvaluationReminderFragment();
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString(EvaluationItemWrapper.class.getName(), gson.toJson(evaluationItem));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        gson = new Gson();
        evaluationService = EvaluationService.getInstance(getContext());
        if (getArguments() != null) {
            evaluationItem = gson.fromJson(getArguments().getString(EvaluationItemWrapper.class.getName()), EvaluationItemWrapper.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.evaluation_reminder, container, false);

        populateEvaluationReminderItemList(rootView);
        setNewEntryOnClickListener(rootView);
        return rootView;
    }

    private void setNewEntryOnClickListener(View rootView) {
        rootView.findViewById(R.id.evaluation_reminder_btn_newentry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragment().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, EvaluationReminderEntryFragment.newInstance(evaluationItem))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void populateEvaluationReminderItemList(View rootView) {
        initEvaluationReminderData();
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.evaluation_reminder_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EvaluationReminderItemAdapter mAdapter = new EvaluationReminderItemAdapter(this, reminderItems, evaluationItem);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initEvaluationReminderData() {
        reminderItems = new ArrayList<ReminderItemWrapper>();
        reminderItems.addAll(evaluationService.getListReminder(evaluationItem.getId()));
    }
}
