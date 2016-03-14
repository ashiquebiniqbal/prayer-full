package net.fajarachmad.prayer.prayertime.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayertime.wrapper.GooglePlace;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import net.fajarachmad.prayer.common.util.AsyncTaskUtil;
import net.fajarachmad.prayer.common.util.GooglePlaceUtil;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static net.fajarachmad.prayer.common.constant.AppConstant.API_KEY;
import static net.fajarachmad.prayer.common.constant.AppConstant.DEFAULT_LANGUAGE;
import static net.fajarachmad.prayer.common.constant.AppConstant.GOOGLE_NEARBY_PLACE_API;
import static net.fajarachmad.prayer.common.constant.AppConstant.PREF_LANGUAGE_KEY;

public class MosqueFinderFragment extends AbstractPrayerFragment {

    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";

	private GoogleMap googleMap;
	private MapView mapView;
	private boolean mapsSupported = true;
	private Prayer prayer;
	private SharedPreferences sharedPrefs;
    private Geocoder geocoder;
    private GooglePlaceAdapter mArrayAdapter;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideParentToolbar();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.prayertime_mosquefinder,
				container, false);
		setCustomToolbar(rootView, getContext()
                .getResources().getString(R.string.mosque_finder_title));
		mapView = (MapView) rootView.findViewById(R.id.map);
		mListView = (ListView) rootView.findViewById(R.id.mosque_list);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        geocoder = new Geocoder(getContext(), new Locale(sharedPrefs.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE)));
        Gson gson = new Gson();
        String prayerJson = sharedPrefs.getString(Prayer.class.getName(), gson.toJson(new Prayer()));
        prayer = gson.fromJson(prayerJson, Prayer.class);
        getNearbyMosque();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MapsInitializer.initialize(getContext());

		if (mapView != null) {
			mapView.onCreate(savedInstanceState);
		}
		initializeMap();
	}

    private void getNearbyMosque() {
        try {
            HttpGet httpGet = new HttpGet(new URI(constructGoogleNearbyPlaceApi(prayer.getLocation().getLatitude(), prayer.getLocation().getLongitude())));
            AsyncTaskUtil task = new AsyncTaskUtil(getActivity(), ACTION_FOR_INTENT_CALLBACK);
            task.execute(httpGet);
        } catch (Exception e) {

        }

    }

    private String constructGoogleNearbyPlaceApi(double latitude, double longitude) {
        StringBuffer api = new StringBuffer();
        api.append(GOOGLE_NEARBY_PLACE_API);
        api.append("location=");
        api.append(latitude);
        api.append(",");
        api.append(longitude);
        api.append("&radius=500&type=mosque&key=");
        api.append(API_KEY);

        return api.toString();
    }

	private void initializeMap() {
		if (googleMap == null && mapsSupported) {
			mapView = (MapView) getView().findViewById(R.id.map);
			googleMap = mapView.getMap();
            googleMap.setMyLocationEnabled(true);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(prayer.getLocation().getLatitude(), prayer.getLocation().getLongitude())).zoom(15).build();


			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		initializeMap();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
	}

	@Override
	public void onPause() {
        super.onPause();
		mapView.onPause();
        getActivity().unregisterReceiver(receiver);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

    private void addMapMarker(List<GooglePlace> googlePlaces){
        for (GooglePlace googlePlace: googlePlaces) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(googlePlace.getGeometry().getLocation().getLat(), googlePlace.getGeometry().getLocation().getLng()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markerOptions.title(googlePlace.getName());
            googleMap.addMarker(markerOptions);
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String response = intent.getStringExtra(AsyncTaskUtil.HTTP_RESPONSE);
            List<GooglePlace> googlePlaces = GooglePlaceUtil.parseGoogleParse(response);

            mArrayAdapter = new GooglePlaceAdapter(getContext(), R.layout.prayertime_mosquefinder_item, R.id.mosque_name, googlePlaces);
            mListView.setAdapter(mArrayAdapter);
            addMapMarker(googlePlaces);
        }
    };

    public class GooglePlaceAdapter extends ArrayAdapter<GooglePlace> {

        private List<GooglePlace> locations;

        public GooglePlaceAdapter(Context context, int resource,
                               int textViewResourceId, List<GooglePlace> objects) {
            super(context, resource, textViewResourceId, objects);
            locations = objects;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.prayertime_mosquefinder_item, null);
            }
            GooglePlace location = locations.get(position);
            if (location != null) {
                ((TextView) view.findViewById(R.id.mosque_name)).setText(location.getName());
                ((TextView) view.findViewById(R.id.mosque_address_line)).setText(location.getVicinity());
                ImageView imageView = (ImageView)view.findViewById(R.id.mosque_icon);
                /*try {
                    URL url = new URL(location.getIcon());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    imageView.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    Log.e("error", "error", e);
                }*/
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.prayer_mosque));

            }
            return view;
        }
    }

}
