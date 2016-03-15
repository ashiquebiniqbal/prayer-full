package net.fajarachmad.prayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.prayertime.fragment.GlobalSettingFragment;

/**
 * Created by user on 3/15/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.settings_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        GlobalSettingFragment settingsFragment = new GlobalSettingFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, settingsFragment)
                .commit();
    }
}
