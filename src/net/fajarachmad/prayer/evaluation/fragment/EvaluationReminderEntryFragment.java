package net.fajarachmad.prayer.evaluation.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.adapter.CommonPopupListviewAdapter;
import net.fajarachmad.prayer.common.adapter.CommonPopupListviewDelegate;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.common.util.IDGenerator;
import net.fajarachmad.prayer.common.util.StringUtil;
import net.fajarachmad.prayer.common.view.CheckableImageButton;
import net.fajarachmad.prayer.common.wrapper.KeyValue;
import net.fajarachmad.prayer.evaluation.service.EvaluationService;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;
import net.fajarachmad.prayer.evaluation.wrapper.Reminder;
import net.fajarachmad.prayer.evaluation.wrapper.ReminderItemWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 3/5/2016.
 */
public class EvaluationReminderEntryFragment extends AbstractPrayerFragment {

    private TextView txtMessage;
    private TextView txtTime;
    private TextView txtTone;
    private CheckableImageButton btnSun;
    private CheckableImageButton btnMon;
    private CheckableImageButton btnTue;
    private CheckableImageButton btnWed;
    private CheckableImageButton btnThu;
    private CheckableImageButton btnFri;
    private CheckableImageButton btnSat;

    private KeyValue selectedTone;

    private EvaluationItemWrapper evaluationItem;
    private SimpleDateFormat dateFormatter;
    private EvaluationService evaluationService;
    private Context context;
    private Ringtone currentRingtone;
    private AlertDialog toneDialog = null;
    private List<KeyValue> toneList;
    private RingtoneManager ringtoneManager;
    private Gson gson;
    private Reminder data;
    private ReminderItemWrapper currentData;
    private Dialog dialog;

    public static EvaluationReminderEntryFragment newInstance(EvaluationItemWrapper evaluationItem) {
        EvaluationReminderEntryFragment f = new EvaluationReminderEntryFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString(EvaluationItemWrapper.class.getName(), gson.toJson(evaluationItem));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        evaluationService = EvaluationService.getInstance(getContext());
        context = getContext();
        gson = new Gson();
        evaluationItem = gson.fromJson(getArguments().getString(EvaluationItemWrapper.class.getName()), EvaluationItemWrapper.class);
        if (getArguments() != null) {
            currentData = gson.fromJson(getArguments().getString(ReminderItemWrapper.class.getName()), ReminderItemWrapper.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.evaluation_reminder_entry, container, false);
        setupInputElement(rootView);
        populateRingtoneList();
        String title = null;
        if (currentData == null) {
            title = getActivity().getResources().getString(R.string.add_new_evaluation_reminder);
        } else {
            title = getActivity().getResources().getString(R.string.edit_evaluation_reminder);
        }
        setCustomToolbar(rootView, title);
        setupTimePickerDialog(rootView);
        setOnClickListenerTxtSound(rootView);
        setOnClickBtnSave();
        return rootView;
    }

    private void populateRingtoneList() {
        toneList = new ArrayList<KeyValue>();
        ringtoneManager = new RingtoneManager(getContext());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String notificationId = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            notificationUri = notificationUri+"/"+notificationId;
            toneList.add(new KeyValue(notificationUri, notificationTitle));
        }
    }

    private void setOnClickListenerTxtSound(final View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getResources().getString(R.string.set_reminder_tone_title));
        builder.setView(R.layout.common_popup_listview);


        builder.setPositiveButton(getContext().getResources().getString(R.string.set), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toneDialog.hide();
                if (currentRingtone != null && currentRingtone.isPlaying()) {
                    currentRingtone.stop();
                }
            }
        });

        builder.setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toneDialog.hide();
                if (currentRingtone != null && currentRingtone.isPlaying()) {
                    currentRingtone.stop();
                }
            }
        });

        view.findViewById(R.id.evaluation_reminder_entry_txtsound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toneDialog = builder.show();

                ListView dialogListview = (ListView)toneDialog.findViewById(R.id.common_popup_listview);

                CommonPopupListviewAdapter adapter = new CommonPopupListviewAdapter(getContext(), R.layout.common_popup_listview, toneList);

                adapter.setDelegate(new CommonPopupListviewDelegate() {
                    @Override
                    public void onClickListItem(KeyValue selected, int position) {
                        if (currentRingtone != null && currentRingtone.isPlaying()) {
                            currentRingtone.stop();
                        }
                        currentRingtone = ringtoneManager.getRingtone(context, Uri.parse(selected.getKey()));
                        if (currentRingtone != null) {
                            currentRingtone.play();
                        }
                        selectedTone = selected;
                        txtTone.setText(selected.getValue());
                    }
                });
                dialogListview.setAdapter(adapter);
            }
        });
    }

    private void setupTimePickerDialog(final View view) {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String time = new StringBuilder().append(pad(hourOfDay))
                        .append(":").append(pad(minute)).toString();
                txtTime.setText(time);
            }
        }, hour, minute, false );
        view.findViewById(R.id.evaluation_reminder_entry_txttime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void setupInputElement(View view) {
        txtMessage = (TextView)view.findViewById(R.id.evaluation_reminder_entry_txtmessage);
        txtTime = (TextView) view.findViewById(R.id.evaluation_reminder_entry_txttime);
        txtTone = (TextView) view.findViewById(R.id.evaluation_reminder_entry_txtsound);
        btnSun = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_sun);
        btnMon = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_mon);
        btnTue = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_tue);
        btnWed = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_wed);
        btnThu = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_thu);
        btnFri = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_fri);
        btnSat = (CheckableImageButton) view.findViewById(R.id.evaluation_reminder_entry_btn_sat);

        //default value
        btnMon.setChecked(true);
        btnTue.setChecked(true);
        btnWed.setChecked(true);
        btnThu.setChecked(true);
        btnFri.setChecked(true);

        if (currentData != null) {
            txtMessage.setText(currentData.getMessage());
            txtTime.setText(currentData.getTime());
            txtTone.setText(currentData.getTone());
            btnSun.setChecked(currentData.isRepeatSun());
            btnMon.setChecked(currentData.isRepeatMon());
            btnTue.setChecked(currentData.isRepeatTue());
            btnWed.setChecked(currentData.isRepeatWed());
            btnThu.setChecked(currentData.isRepeatThu());
            btnFri.setChecked(currentData.isRepeatFri());
            btnSat.setChecked(currentData.isRepeatSat());
            if (!StringUtil.isBlank(currentData.getTone())) {
                selectedTone = new KeyValue(currentData.getToneURI(), currentData.getTone());
            }
        }

    }

    private void convertElementToObject() {
        data = new Reminder();
        if (currentData != null) {
            data.setId(currentData.getId());
        }

        String message = txtMessage.getText() != null ? txtMessage.getText().toString() : null;
        String time = txtTime.getText() != null ? txtTime.getText().toString() : null;
        data.setEvaluationId(evaluationItem.getId());
        data.setMessage(message);
        data.setTime(time);
        data.setTone(selectedTone != null ? selectedTone.getValue() : null);
        data.setToneUri(selectedTone != null ? selectedTone.getKey() : null);
        data.setRepeatSun(btnSun.isChecked());
        data.setRepeatMon(btnMon.isChecked());
        data.setRepeatTue(btnTue.isChecked());
        data.setRepeatWed(btnWed.isChecked());
        data.setRepeatThu(btnThu.isChecked());
        data.setRepeatFri(btnFri.isChecked());
        data.setRepeatSat(btnSat.isChecked());

    }

    private void setOnClickBtnSave() {
        toolbar.inflateMenu(R.menu.entry_menu_withdelete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_save:
                        if (validate()) {
                            convertElementToObject();
                            evaluationService.save(data);
                            getFragmentManager().popBackStack();
                        }
                        break;

                    case R.id.action_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you sure want to delete this reminder ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                evaluationService.delete(currentData.getEvaluationId(), currentData.getId());
                                getFragmentManager().popBackStack();
                                dialog.hide();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getFragmentManager().popBackStack();
                                dialog.hide();
                            }
                        });
                        dialog = builder.show();
                        break;
                }
                return false;
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;

        if (StringUtil.isBlank(txtMessage.getText().toString())) {
            isValid = false;
            txtMessage.setError("Reminder message should not be empty");
        }

        if (StringUtil.isBlank(txtTime.getText().toString())) {
            isValid = false;
            txtTime.setError("Reminder time should not be empty");
        }

        return isValid;
    }




}
