package net.fajarachmad.prayer.prayertime.fragment;

import static net.fajarachmad.prayer.common.constant.AppConstant.*;
import net.fajarachmad.prayer.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationInfoFragment extends Fragment{

	public static String LOCATION_NAME = "locationName";
	public static String ACTION_UPDATE_LOCATION = "actionUpdateLocation";
	
	private String locationName;
	private View view;
	private SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locationName = getArguments().getString("locationName");
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.prayertime_pager_locationinfo, container, false);
		
		TextView textView =  (TextView)view.findViewById(R.id.location_address);
		textView.setText(locationName);

		if (!sharedPrefs.getBoolean(PREF_AUTODETECT_LOCATION_KEY, DEFAULT_AUTO_DETECT_LOCATION)) {
			view.findViewById(R.id.auto_detect_location_icon).setVisibility(View.GONE);
		}

		return view;
	}
	
	public void updateLocation(String locationName) {
		TextView textView =  (TextView)view.findViewById(R.id.location_address);
		textView.setText(locationName);

		if (!sharedPrefs.getBoolean(PREF_AUTODETECT_LOCATION_KEY, DEFAULT_AUTO_DETECT_LOCATION)) {
			view.findViewById(R.id.auto_detect_location_icon).setVisibility(View.GONE);
		}
	}

	public TextView getLocationTextview() {
		return ((TextView) view.findViewById(R.id.location_address));
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(ACTION_UPDATE_LOCATION));
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String locationName = intent.getStringExtra(LOCATION_NAME);
			updateLocation(locationName);
		}
	};
}
