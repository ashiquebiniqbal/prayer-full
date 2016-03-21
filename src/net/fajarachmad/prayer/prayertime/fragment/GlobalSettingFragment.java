package net.fajarachmad.prayer.prayertime.fragment;

import static net.fajarachmad.prayer.common.constant.AppConstant.*;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;


import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.activity.SettingsActivity;
import net.fajarachmad.prayer.common.view.SliderPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 3/2/2016.
 */
public class GlobalSettingFragment extends PreferenceFragment {

    private SharedPreferences sharedPrefs;
    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        fragment = this;
        addPreferencesFromResource(R.xml.setting_layout);


        List<String> values = new ArrayList<String>();

        for (int i = -60; i <= 60; i++) {
            values.add(String.valueOf(i));
        }

        ((SliderPreference)findPreference(PREF_TUNE_FAJR_KEY)).setSummary(values.toArray(new String[0]));
        ((SliderPreference)findPreference(PREF_TUNE_DHUHR_KEY)).setSummary(values.toArray(new String[0]));
        ((SliderPreference)findPreference(PREF_TUNE_ASR_KEY)).setSummary(values.toArray(new String[0]));
        ((SliderPreference)findPreference(PREF_TUNE_MAGHRIB_KEY)).setSummary(values.toArray(new String[0]));
        ((SliderPreference)findPreference(PREF_TUNE_ISHA_KEY)).setSummary(values.toArray(new String[0]));

        ((SliderPreference)findPreference(PREF_TUNE_FAJR_KEY)).setDialogMessage(findPreference(PREF_TUNE_FAJR_KEY).getSummary());
        ((SliderPreference)findPreference(PREF_TUNE_DHUHR_KEY)).setDialogMessage(findPreference(PREF_TUNE_DHUHR_KEY).getSummary());
        ((SliderPreference)findPreference(PREF_TUNE_ASR_KEY)).setDialogMessage(findPreference(PREF_TUNE_ASR_KEY).getSummary());
        ((SliderPreference)findPreference(PREF_TUNE_MAGHRIB_KEY)).setDialogMessage(findPreference(PREF_TUNE_MAGHRIB_KEY).getSummary());
        ((SliderPreference)findPreference(PREF_TUNE_ISHA_KEY)).setDialogMessage(findPreference(PREF_TUNE_ISHA_KEY).getSummary());

        Preference resetPref = findPreference(PREF_TUNE_RESET_KEY);
        resetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @SuppressLint("UseValueOf")
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                ((SliderPreference)findPreference(PREF_TUNE_FAJR_KEY)).setValue(DEFAULT_MANUAL_TUNE);
                ((SliderPreference)findPreference(PREF_TUNE_DHUHR_KEY)).setValue(DEFAULT_MANUAL_TUNE);
                ((SliderPreference)findPreference(PREF_TUNE_ASR_KEY)).setValue(DEFAULT_MANUAL_TUNE);
                ((SliderPreference)findPreference(PREF_TUNE_MAGHRIB_KEY)).setValue(DEFAULT_MANUAL_TUNE);
                ((SliderPreference)findPreference(PREF_TUNE_ISHA_KEY)).setValue(DEFAULT_MANUAL_TUNE);
                return false;
            }
        });

        findPreference(PREF_LANGUAGE_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                findPreference(PREF_LANGUAGE_KEY).setSummary(getValueByKey(R.array.languageValues, R.array.language, value.toString()));
                ((ListPreference)findPreference(PREF_LANGUAGE_KEY)).setValue(value.toString());
                setLocale();
                getActivity().recreate();
                return false;
            }
        });

        findPreference(PREF_CALULATION_METHOD_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                findPreference(PREF_CALULATION_METHOD_KEY).setSummary(getValueByKey(R.array.calulationMethodValues, R.array.calulationMethod, value.toString()));
                ((ListPreference)findPreference(PREF_CALULATION_METHOD_KEY)).setValue(value.toString());
                return false;
            }
        });

        findPreference(PREF_ASR_METHOD_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                findPreference(PREF_ASR_METHOD_KEY).setSummary(getValueByKey(R.array.ashMethodValues, R.array.asrMethod, value.toString()));
                ((ListPreference)findPreference(PREF_ASR_METHOD_KEY)).setValue(value.toString());
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

    private String getValueByKey(int id, int valueId, String key) {
        int i = -1;
        for (String cc: getResources().getStringArray(id)) {
            i++;
            if (cc.equals(key))
                break;
        }
        return getResources().getStringArray(valueId)[i];
    }

    private void setLocale() {
        Locale locale = new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,getContext().getResources().getDisplayMetrics());
    }
}
