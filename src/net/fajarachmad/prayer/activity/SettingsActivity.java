package net.fajarachmad.prayer.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.prayertime.fragment.GlobalSettingFragment;

/**
 * Created by user on 3/15/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        GlobalSettingFragment settingsFragment = new GlobalSettingFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.setting_container, settingsFragment)
                .commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
