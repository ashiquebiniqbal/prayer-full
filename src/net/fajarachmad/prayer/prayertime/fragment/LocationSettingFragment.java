package net.fajarachmad.prayer.prayertime.fragment;

import static net.fajarachmad.prayer.common.constant.AppConstant.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.adapter.LocationAdapter;
import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.common.util.AsyncGeocoderUtil;
import net.fajarachmad.prayer.common.util.AsyncTaskUtil;
import net.fajarachmad.prayer.common.util.GooglePlaceUtil;
import net.fajarachmad.prayer.prayertime.service.AsyncPrayerTimeService;
import net.fajarachmad.prayer.prayertime.wrapper.GooglePlace;
import net.fajarachmad.prayer.prayertime.wrapper.Location;
import net.fajarachmad.prayer.common.util.GPSTracker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;

public class LocationSettingFragment extends AbstractPrayerFragment {

	private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";

	private SharedPreferences sharedPrefs;
	// List view
    private ListView lv;
     
    // Listview Adapter
    private LocationAdapter adapter;
     
    // Search EditText
    private EditText inputSearch;
    
    private Geocoder geocoder;
	private Gson gson;
	private ProgressBar progressBar;
	private AsyncPrayerTimeService prayertimeService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideParentToolbar();
		gson = new Gson();
		prayertimeService = AsyncPrayerTimeService.getInstance(getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.prayertime_location, container, false);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					getFragmentManager().popBackStack();
					return true;
				}
				return false;
			}
		});

		setCustomToolbar(rootView, getContext().getResources().getString(R.string.location_setting_title));

		// Listview Data
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		lv = (ListView) rootView.findViewById(R.id.prayertime_location_listview);
		inputSearch = (EditText) rootView.findViewById(R.id.prayertime_location_inputSearch);
		progressBar = (ProgressBar) rootView.findViewById(R.id.prayertime_location_progressbar);

		hideProgress();

		geocoder = new Geocoder(getContext(), new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE)));
		setInputSearchListener();
		setListViewListener();
		setButtonCurrentLocationListener(rootView);
		
		((MainActivity)getActivity()).setActivityTitle(getContext().getResources().getString(R.string.location_setting_title));

		return rootView;
	}

	private void showProgress() {
		lv.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideProgress() {
		lv.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}



	private void setButtonCurrentLocationListener(View rootView) {
		rootView.findViewById(R.id.prayertime_location_btncurrentlocation).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showProgress();
						GPSTracker gpsTracker = new GPSTracker(getContext());
						if (isNetworkAvailable()) {
							if (gpsTracker.getIsGPSTrackingEnabled()) {

								try {
									Location location = prayertimeService.determineLocationFromGps();
									List<Location> locations = new ArrayList<Location>();
									locations.add(location);

									adapter = new LocationAdapter(
											getContext(),
											R.layout.prayertime_location_item,
											R.id.location_name, locations);
									lv.setAdapter(adapter);
									hideProgress();
								} catch (Exception e) {
									//TODO: show error
								}

							} else {
								gpsTracker.showSettingsAlert();
								hideProgress();
							}
						} else {
							Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_network_alert), Toast.LENGTH_SHORT).show();
							hideProgress();

						}



					}
				});
	}

	private void setListViewListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				try {

					Location selected = (Location) parent.getItemAtPosition(position);
					selected.setTimezone(prayertimeService.getTimezone(selected.getLatitude(), selected.getLongitude()));
					Intent intent = new Intent();
					Gson gson = new Gson();
					intent.putExtra(Location.class.getName(), gson.toJson(selected));
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
					getFragmentManager().popBackStack();

				} catch (Exception e) {
					//TODO: show error messa
				}

			}

		});
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void setInputSearchListener() {
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
				// When user changed the Text
				if (cs.length() > 3) {
					if (isNetworkAvailable()) {
						AsyncTask task = new AsyncTask() {
							@Override
							protected Object doInBackground(Object[] objects) {
								try {
									List<Address> addresses = ((Geocoder) objects[0]).getFromLocationName(String.valueOf(objects[1]), 50);
									List<Location> locations = new ArrayList<>();
									for (Address address : addresses) {
										locations.add(prayertimeService.convertToLocation(address));
									}
									return locations;
								} catch (Exception e) {
									//TODO: show error
								}

								return null;
							}

							@Override
							protected void onPostExecute(Object o) {
								super.onPostExecute(o);
								List<Location> locations = (List<Location>) o;
								adapter = new LocationAdapter(
										getContext(),
										R.layout.prayertime_location_item,
										R.id.location_name, locations);
								lv.setAdapter(adapter);
								hideProgress();
							}
						};
						task.execute(geocoder, cs.toString());
						showProgress();
					} else {
						Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_network_alert), Toast.LENGTH_SHORT).show();
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

}
