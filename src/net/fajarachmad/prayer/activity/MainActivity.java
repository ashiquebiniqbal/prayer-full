package net.fajarachmad.prayer.activity;

import static net.fajarachmad.prayer.common.constant.AppConstant.ACTION;
import static net.fajarachmad.prayer.common.constant.AppConstant.ACTION_GET_PRAYER_TIME;
import static net.fajarachmad.prayer.common.constant.AppConstant.APP_SETTING_ID;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationFragment;
import net.fajarachmad.prayer.prayertime.fragment.GlobalSettingFragment;
import net.fajarachmad.prayer.prayertime.fragment.PrayerTimeFragment;
import net.fajarachmad.prayer.prayertime.service.PrayerTimeService;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        
        //populate default fragment
        getSupportActionBar().setTitle(getString(R.string.title_prayer_time));
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, new PrayerTimeFragment())
        .commit();
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