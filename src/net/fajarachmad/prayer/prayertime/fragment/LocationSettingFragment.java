package net.fajarachmad.prayer.prayertime.fragment;

import static net.fajarachmad.prayer.common.constant.AppConstant.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.common.util.AsyncGeocoderUtil;
import net.fajarachmad.prayer.common.util.AsyncTaskUtil;
import net.fajarachmad.prayer.common.util.GooglePlaceUtil;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideParentToolbar();
		gson = new Gson();
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

		progressBar.setVisibility(View.GONE);

		geocoder = new Geocoder(getContext(), new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE)));
		setInputSearchListener();
		setListViewListener();
		setButtonCurrentLocationListener(rootView);
		
		((MainActivity)getActivity()).setActivityTitle(getContext().getResources().getString(R.string.location_setting_title));

		return rootView;
	}

	private void setButtonCurrentLocationListener(View rootView) {
		rootView.findViewById(R.id.prayertime_location_btncurrentlocation).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						progressBar.setVisibility(View.VISIBLE);
						GPSTracker gpsTracker = new GPSTracker(getContext());
						if (isNetworkAvailable() && gpsTracker.getIsGPSTrackingEnabled()) {

							double latitude = gpsTracker.getLatitude();
							double longitude = gpsTracker.getLongitude();
							String country = gpsTracker.getCountryName(getContext());
							String city = gpsTracker.getLocality(getContext());
							String postalCode = gpsTracker.getPostalCode(getContext());
							String addressLine = gpsTracker.getAddressLine(getContext());

							Location location = new Location();
							location.setLatitude(latitude);
							location.setLongitude(longitude);
							location.setCountry(country);
							location.setCity(city);
							location.setAddressLine(addressLine);
							location.setPostalCode(postalCode);

							Log.i("Prayer", "Latitude: " + latitude);
							Log.i("Prayer", "Longitude: " + longitude);
							Log.i("Prayer", "Country: " + country);
							Log.i("Prayer", "City: " + city);

							List<Location> locations = new ArrayList<Location>();
							locations.add(location);

							adapter = new LocationAdapter(
									getContext(),
									R.layout.prayertime_location_item,
									R.id.location_name, locations);
							lv.setAdapter(adapter);

						} else {
							// can't get location
							// GPS or Network is not enabled
							// Ask user to enable GPS/network in settings
							gpsTracker.showSettingsAlert();
						}
						progressBar.setVisibility(View.GONE);

					}
				});
	}

	private void setListViewListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Location selected = (Location) parent.getItemAtPosition(position);
				Intent intent = new Intent();
				Gson gson = new Gson();
				intent.putExtra(Location.class.getName(), gson.toJson(selected));
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
				getFragmentManager().popBackStack();
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
				if (cs.length() > 3 && isNetworkAvailable()) {
					AsyncGeocoderUtil task = new AsyncGeocoderUtil(getActivity(), cs.toString(), ACTION_FOR_INTENT_CALLBACK);
					task.execute(geocoder);
					progressBar.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (!isNetworkAvailable()) {
					Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_network_alert), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String response = intent.getStringExtra(AsyncTaskUtil.HTTP_RESPONSE);
			List<Location> locations = gson.fromJson(response, List.class);

			adapter = new LocationAdapter(
					getContext(),
					R.layout.prayertime_location_item,
					R.id.location_name, locations);
			lv.setAdapter(adapter);

			progressBar.setVisibility(View.GONE);
		}
	};

	public class LocationAdapter extends ArrayAdapter<Location> {

		private List<Location> locations;

		public LocationAdapter(Context context, int resource,
				int textViewResourceId, List<Location> objects) {
			super(context, resource, textViewResourceId, objects);
			locations = objects;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.prayertime_location_item, null);
			}
			Location location = locations.get(position);
			if (location != null) {
				((TextView) view.findViewById(R.id.location_name))
						.setText(location.getCity() + ", "
								+ location.getCountry());
			}
			return view;
		}
	}

}
