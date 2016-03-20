package net.fajarachmad.prayer.activity;


import static net.fajarachmad.prayer.common.constant.AppConstant.*;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.adapter.LocationAdapter;
import net.fajarachmad.prayer.common.util.AsyncGeocoderUtil;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationFragment;
import net.fajarachmad.prayer.prayertime.fragment.PrayerTimeFragment;
import net.fajarachmad.prayer.prayertime.service.AsyncPrayerTimeService;
import net.fajarachmad.prayer.prayertime.service.PrayerTimeService;
import net.fajarachmad.prayer.prayertime.wrapper.Location;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private SharedPreferences sharedPrefs;
    private AsyncPrayerTimeService prayertimeService;
    private Geocoder geocoder;
    private Dialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        prayertimeService = AsyncPrayerTimeService.getInstance(this);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        geocoder = new Geocoder(this, getResources().getConfiguration().locale);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.title_prayer_time));

        if (sharedPrefs.getBoolean(PREF_AUTODETECT_LOCATION_KEY, true)) {
            prayertimeService.updateCurrentLocation();
        }
        try {
            if (validateLocation()) {
                attachDefaultFragment();
            }
        } catch (Exception e) {
            //TODO: Show error
        }


        
        //populate default fragment

       /* getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, new PrayerTimeFragment())
        .commit();*/
    }

    private void attachDefaultFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PrayerTimeFragment())
                .commit();
    }

    private boolean validateLocation() throws Exception{
        final Location location = prayertimeService.getCurrentLocation();
        if (location == null) {
            if (prayertimeService.isNetworkAvailable()) {
                if (prayertimeService.isGPSAvailable()) {
                    prayertimeService.determineLocationFromGps();
                    attachDefaultFragment();
                } else {
                    showLocationSearchDialog();
                }

            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Network problem");
                builder.setMessage("Internet is not available");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        attachDefaultFragment();
                    }
                });
                builder.show().getWindow().setLayout(600, 500);;
            }
        }

        return location != null;
    }

    private void showLocationSearchDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        EditText txtSearch = new EditText(this);
        txtSearch.setHint("Search location");
        final ListView listView = new ListView(this);
        layout.addView(txtSearch);
        layout.addView(listView);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("Set your location");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog = builder.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Location selected = (Location) parent.getItemAtPosition(position);
                    selected.setTimezone(prayertimeService.getTimezone(selected.getLatitude(), selected.getLongitude()));
                    prayertimeService.save(selected);
                    dialog.cancel();
                    attachDefaultFragment();
                } catch (Exception e) {
                    //TODO: Show error
                }

            }
        });
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (cs.length() > 3) {
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            List<Location> locations = new ArrayList<Location>();
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(String.valueOf(objects[0]), 50);
                                for (Address address : addresses) {
                                    Location location = prayertimeService.convertToLocation(address);
                                    locations.add(location);
                                }

                            } catch (Exception e) {
                                //TODO: show error
                            }

                            return locations;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            List<Location> locations = (List<Location>) o;
                            LocationAdapter adapter = new LocationAdapter(
                                    getApplicationContext(),
                                    R.layout.prayertime_location_item,
                                    R.id.location_name, locations);
                            listView.setAdapter(adapter);
                        }
                    };

                    task.execute(cs);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addDrawerItems() {
        String[] osArray = { "Dashboard", "Prayer Time", "Self Evaluation", "Prayers" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	// update the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment;
                switch (position) {
        		case 1:
        			fragment = new PrayerTimeFragment();
        			mActivityTitle = getString(R.string.title_prayer_time);
        			break;
                case 2:
                    fragment = new EvaluationFragment();
                    mActivityTitle = getString(R.string.self_evaluation_title);
                    break;
        		default:
        			fragment = new PrayerTimeFragment();
        			break;
        		}
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
   	 
            case R.id.action_settings:
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new GlobalSettingFragment())
                    .addToBackStack(null)
                    .commit();*/
                startActivityForResult(new Intent(this, SettingsActivity.class), 1000);
            break;
 
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    @Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   		super.onActivityResult(requestCode, resultCode, data);

   		switch (requestCode) {
   		case APP_SETTING_ID:
   			Intent service = new Intent(getApplicationContext(), PrayerTimeService.class);
   			service.putExtra(ACTION, ACTION_GET_PRAYER_TIME);
   			startService(service);
   			break;
   		default:	
   			break;
   		}

   	}
    
    public void setActivityTitle(String title) {
    	this.mActivityTitle = title;
    	getSupportActionBar().setTitle(mActivityTitle);
    }

}