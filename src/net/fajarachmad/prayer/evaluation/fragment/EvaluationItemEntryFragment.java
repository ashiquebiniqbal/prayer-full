package net.fajarachmad.prayer.evaluation.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.common.util.ResourceUtil;
import net.fajarachmad.prayer.evaluation.service.EvaluationService;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItem;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 3/5/2016.
 */
public class EvaluationItemEntryFragment extends AbstractPrayerFragment {

    private TextView txtGoalname;
    private RadioGroup rdgEntryType;
    private TextView txtTarget;
    private TextView txtTargetUnit;
    private Spinner txtTargetFrequency;
    private RadioButton rdbEntryTypeYesNo;
    private RadioButton rdbEntryTypeFree;

    private SimpleDateFormat dateFormatter;
    private TextView txtEndDate;
    private EvaluationService evaluationService;
    private EvaluationItemWrapper currentData;
    private Gson gson;

    public static EvaluationItemEntryFragment newInstance(EvaluationItemWrapper data) {
        EvaluationItemEntryFragment f = new EvaluationItemEntryFragment();
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString(EvaluationItemWrapper.class.getName(), gson.toJson(data));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideParentToolbar();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        evaluationService = EvaluationService.getInstance(getContext());
        gson = new Gson();
        if (getArguments() != null) {
            currentData = gson.fromJson(getArguments().getString(EvaluationItemWrapper.class.getName()), EvaluationItemWrapper.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.evaluation_item_entry, container, false);
        ((MainActivity)getActivity()).setActivityTitle(getActivity().getResources().getString(R.string.self_evaluation_title));

        setupInputElement(rootView);

        setCustomToolbar(rootView, getContext().getResources().getString(R.string.new_goal));
        setBtnSaveOnClickListener();
        setEndDateOnClickListener(rootView);
        return rootView;
    }

    private void setupInputElement(View view) {
        txtGoalname = (TextView)view.findViewById(R.id.evalluation_entry_txt_goalname);
        rdgEntryType = (RadioGroup) view.findViewById(R.id.evalluation_entry_type);
        txtTarget = (TextView) view.findViewById(R.id.evalluation_entry_txt_target);
        txtTargetUnit = (TextView) view.findViewById(R.id.evalluation_entry_txt_targetunit);
        txtTargetFrequency = (Spinner) view.findViewById(R.id.evalluation_entry_txt_frequency);
        txtEndDate = (TextView)view.findViewById(R.id.evaluation_entry_enddate);
        rdbEntryTypeFree = (RadioButton) view.findViewById(R.id.evaluation_entry_type_free);
        rdbEntryTypeYesNo = (RadioButton) view.findViewById(R.id.evaluation_entry_type_yesno);

        if (currentData != null) {
            txtGoalname.setText(currentData.getGoalName());
            txtTarget.setText(currentData.getTarget());
            txtTargetUnit.setText(currentData.getTargetUnit());
            if (currentData.getEntryType().equals(EvaluationItem.ENTRY_TYPE_FREE)) {
                rdbEntryTypeFree.setChecked(true);
                rdbEntryTypeYesNo.setChecked(false);
            } else {
                rdbEntryTypeFree.setChecked(false);
                rdbEntryTypeYesNo.setChecked(true);
            }
            txtEndDate.setText(currentData.getDueDateString());
        }
        txtTargetFrequency.setSelection(2);
    }

    private void setEndDateOnClickListener(View view) {
        Calendar newCalendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtEndDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private EvaluationItem convertElementInputToObject() {
        EvaluationItem data = new EvaluationItem();
        if (currentData != null) {
            data.setId(currentData.getId());
        }

        String goalName = txtGoalname.getText().toString();
        String target = txtTarget.getText().toString();
        String targetUnit = txtTargetUnit.getText().toString();
        String endDate = txtEndDate.getText().toString();
        String targetFrequency = txtTargetFrequency.getSelectedItem().toString();

        String targetFreqCode = ResourceUtil.getValueByKey(getContext(), R.array.evaluationFrequency, R.array.evaluationFrequencyValue, targetFrequency);

        if (rdgEntryType.getCheckedRadioButtonId() == R.id.evaluation_entry_type_yesno) {
            data.setEntryType(EvaluationItem.ENTRY_TYPE_YES_NO);
        } else {
            data.setEntryType(EvaluationItem.ENTRY_TYPE_FREE);
        }

        data.setGoalName(goalName);
        data.setTarget(target);
        data.setTargetUnit(targetUnit);
        data.setTargetFrequency(targetFreqCode);

        if (endDate != null) {
            try {
                data.setEndDate(dateFormatter.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        data.setStartDate(new Date());
        data.setCreationDate(new Date());

        return data;
    }

    private void setBtnSaveOnClickListener() {
        toolbar.inflateMenu(R.menu.entry_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_save) {
                    evaluationService.save(convertElementInputToObject());
                    getFragmentManager().popBackStack();
                }

                return false;
            }
        });
    }

}
