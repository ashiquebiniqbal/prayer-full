package net.fajarachmad.prayer.evaluation.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.evaluation.adapter.EvaluationItemAdapter;
import net.fajarachmad.prayer.evaluation.service.EvaluationService;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationFragment extends AbstractPrayerFragment {

    private List<EvaluationItemWrapper> evaluationItems;

    private EvaluationService evaluationService;
    private  EvaluationItemAdapter mAdapter;
    private Dialog dialog;
    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        evaluationService = EvaluationService.getInstance(getContext());
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showParentToolbar();
        View rootView = inflater.inflate(R.layout.evaluation, container, false);
        ((MainActivity)getActivity()).setActivityTitle(getActivity().getResources().getString(R.string.self_evaluation_title));
        resetActionBar();
        populateEvaluationItemList(rootView);
        setButtonNewListener(rootView);

        return rootView;
    }

    private void setButtonNewListener(View view) {
        view.findViewById(R.id.evaluation_btn_newentry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new EvaluationItemEntryFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void resetActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
    }

    private void populateEvaluationItemList(View rootView) {
        initEvaluationData();
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.evaluation_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EvaluationItemAdapter(this, evaluationItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.evaluation_rv);
        registerForContextMenu(mRecyclerView);
    }

    private void initEvaluationData() {
        evaluationItems = new ArrayList<EvaluationItemWrapper>();
        evaluationItems.addAll(evaluationService.getListData());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.evaluation_rv) {
            EvaluationItemWrapper selected = evaluationItems.get(mAdapter.getSelectedPosition());
            menu.setHeaderTitle(selected.getGoalName());
            menu.add(Menu.NONE, 21, Menu.NONE, getContext().getResources().getString(R.string.edit));
            menu.add(Menu.NONE, 22, Menu.NONE, getContext().getResources().getString(R.string.delete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final EvaluationItemWrapper selected = evaluationItems.get(mAdapter.getSelectedPosition());
        if (item.getItemId() == 22) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure want to delete this goal ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    evaluationService.delete(selected.getId());
                    populateEvaluationItemList(getView());
                    dialog.hide();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.hide();
                }
            });
            dialog = builder.show();
        } else if (item.getItemId() == 21) {
            Fragment fragment = new EvaluationItemEntryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(EvaluationItemWrapper.class.getName(), gson.toJson(selected));
            fragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        return super.onContextItemSelected(item);
    }
}
