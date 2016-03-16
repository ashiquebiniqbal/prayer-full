package net.fajarachmad.prayer.evaluation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.evaluation.adapter.EvaluationStatisticCalendarAdapter;
import net.fajarachmad.prayer.evaluation.service.EvaluationService;
import net.fajarachmad.prayer.evaluation.wrapper.Achievment;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItem;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationStatisticFragment extends AbstractPrayerFragment {

    private EvaluationItemWrapper evaluationItem;
    private Gson gson;
    private SimpleDateFormat dateFormatter;
    private AlertDialog dialog;

    private EditText txtAchievment;
    private TextView txtTargetUnit;
    private EvaluationStatisticCalendarFragment calendarFragment;
    private EvaluationService evaluationService;
    private Achievment achievment;
    private Fragment parentFragment;
    private RadioGroup rdgAcheivement;
    private RadioButton rdAchevementYes;
    private RadioButton rdAchevementNo;
    private Date selectedCalendarDate;

    public static EvaluationStatisticFragment newInstance(EvaluationItemWrapper evaluationItem) {
        EvaluationStatisticFragment fragment = new EvaluationStatisticFragment();

        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString(EvaluationItemWrapper.class.getName(), gson.toJson(evaluationItem));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        evaluationItem = gson.fromJson(getArguments().getString(EvaluationItemWrapper.class.getName()), EvaluationItemWrapper.class);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        evaluationService = EvaluationService.getInstance(getContext());
        parentFragment = getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evaluation_statistic, container, false);
        setupCalendar();
        return view;
    }

    private void setupCalendar() {
        calendarFragment = new EvaluationStatisticCalendarFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
        calendarFragment.setArguments(args);

        if (evaluationItem.getStartDate() != null) {
            calendarFragment.setMinDate(evaluationItem.getStartDate());
        }

        Map<String, Object> achievmentMap = evaluationService.getAchievmentMap(evaluationItem.getId());
        Map<String, Object> extraData = calendarFragment.getExtraData();
        extraData.put(EvaluationStatisticCalendarAdapter.ACHIEVMENT_LIST, achievmentMap);

        calendarFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(final Date date, View view) {
                achievment = evaluationService.getAchievmentByDate(evaluationItem.getId(), date);
                selectedCalendarDate = date;
                calendarFragment.clearSelectedDates();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                StringBuilder title = new StringBuilder();
                if (evaluationItem.getEntryType().equals(EvaluationItem.ENTRY_TYPE_FREE)) {
                    title.append("How many ");
                    title.append('"');
                    title.append(evaluationItem.getTargetUnit());
                    title.append('"');
                    title.append(" you have achieved today ?");
                    builder.setView(R.layout.evaluation_statistic_calendar_entry_free);
                } else {
                    title.append("Have you completed your target today ?");
                    builder.setView(R.layout.evaluation_statistic_calendar_entry_yesno);
                }

                if (achievment != null) {
                    builder.setNegativeButton(getResources().getString(R.string.remove), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dg, int which) {

                            evaluationService.delete(achievment);
                            refreshCalendarView();
                            dialog.hide();
                            calendarFragment.setSelectedDate(selectedCalendarDate);
                        }
                    });
                }


                builder.setPositiveButton(getContext().getResources().getString(R.string.set), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String achievmentInput = null;
                        if (evaluationItem.getEntryType().equals(EvaluationItem.ENTRY_TYPE_FREE)) {
                            achievmentInput = txtAchievment.getText().toString();
                        } else {
                            if (rdgAcheivement.getCheckedRadioButtonId() == R.id.evalluation_statistic_calendar_entry_yes) {
                                achievmentInput = "1";
                            } else {
                                achievmentInput = "0";
                            }
                        }

                        if (achievmentInput != null) {
                            if (achievment == null) {
                                achievment = new Achievment();
                            }
                            achievment.setEvaluationId(evaluationItem.getId());
                            achievment.setDate(date);
                            achievment.setAchievment(achievmentInput);
                            achievment.setTargentUnit(evaluationItem.getTargetUnit());
                            achievment.setEntryType(evaluationItem.getEntryType());
                            evaluationService.save(achievment);

                            refreshCalendarView();
                        }
                        dialog.hide();
                        calendarFragment.setSelectedDate(selectedCalendarDate);
                    }
                });

                builder.setNeutralButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.hide();
                        calendarFragment.setSelectedDate(selectedCalendarDate);
                    }
                });

                builder.setTitle(title.toString());
                dialog = builder.show();
                if (evaluationItem.getEntryType().equals(EvaluationItem.ENTRY_TYPE_FREE)) {
                    txtAchievment = (EditText) dialog.findViewById(R.id.evaluation_statistic_calendar_entry_txttarget);
                    txtTargetUnit = (TextView) dialog.findViewById(R.id.evaluation_statistic_calendar_entry_targetunit);
                    txtTargetUnit.setText(evaluationItem.getTargetUnit());

                    if (achievment != null) {
                        txtAchievment.setText(achievment.getAchievment());
                    }
                } else {
                    rdgAcheivement = (RadioGroup) dialog.findViewById(R.id.evalluation_statistic_calendar_entry_rdg);
                    rdAchevementYes = (RadioButton) dialog.findViewById(R.id.evalluation_statistic_calendar_entry_yes);
                    rdAchevementNo = (RadioButton) dialog.findViewById(R.id.evalluation_statistic_calendar_entry_no);
                    if (achievment != null) {
                        if (achievment.getAchievment().equals("1")) {
                            rdAchevementYes.setChecked(true);
                            rdAchevementNo.setChecked(false);
                        } else {
                            rdAchevementYes.setChecked(false);
                            rdAchevementNo.setChecked(true);
                        }

                    }
                }


            }
                                             }

        );

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.evaluation_statistic_calendar, calendarFragment);
        t.commit();
        }

    private void refreshCalendarView() {
        Map<String, Object> achievmentMap = evaluationService.getAchievmentMap(evaluationItem.getId());
        Map<String, Object> extraData = calendarFragment.getExtraData();
        extraData.put(EvaluationStatisticCalendarAdapter.ACHIEVMENT_LIST, achievmentMap);

        calendarFragment.refreshView();

        float currentProgress = evaluationService.calculateCurrentProgress(evaluationItem.getId(), evaluationItem.getTarget());
        String subtitle = new StringBuilder()
                .append(getContext().getResources().getString(R.string.progress))
                .append(": ").append(currentProgress).append("%").toString();
        ((Toolbar) parentFragment.getView().findViewById(R.id.evaluation_detail_toolbar)).setSubtitle(subtitle);
    }
}
