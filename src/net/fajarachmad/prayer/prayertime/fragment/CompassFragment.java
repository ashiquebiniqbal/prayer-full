package net.fajarachmad.prayer.prayertime.fragment;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import net.fajarachmad.prayer.common.util.QiblaDirectionUtil;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

public class CompassFragment extends AbstractPrayerFragment implements SensorEventListener {

	private SharedPreferences sharedPrefs;
	// define the display assembly compass picture
	private ImageView image;
	private ImageView arrow;

	// record the compass picture angle turned
	private float currentDegree = 0f;
	private double qiblaDirection = 0d;
	private float currentQiblaegree = 0f;
	// device sensor manager
	private SensorManager mSensorManager;

	TextView txtLocationName;
	TextView txtHeading;
	
	private Gson gson;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		gson = new Gson();
		hideParentToolbar();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.prayertime_compass, container, false);

		String prayerJson = sharedPrefs.getString(Prayer.class.getName(), gson.toJson(new Prayer()));
		Prayer prayer = gson.fromJson(prayerJson, Prayer.class);

		// our compass image 
        image = (ImageView) rootView.findViewById(R.id.prayertime_compass_bg);
        arrow = (ImageView) rootView.findViewById(R.id.prayertime_compass_arrow);
        
        // TextView that will tell the user what degree is he heading
        txtLocationName = (TextView) rootView.findViewById(R.id.prayertime_compass_locationname);
		txtHeading = (TextView) rootView.findViewById(R.id.prayertime_compass_heading);
 
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
		
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
		
		setCustomToolbar(rootView, getContext().getResources().getString(R.string.compass_title));
		txtLocationName.setText(new StringBuilder().append(prayer.getLocation().getCity()).append(", ").append(prayer.getLocation().getCountry()));
		qiblaDirection = QiblaDirectionUtil.qibla(prayer.getLocation() .getLatitude(), prayer.getLocation().getLongitude());
		return rootView;
	}
	
	private void getQiblaDirection() {
		
		// get the angle around the z-axis rotated
		float degree = currentDegree + Double.valueOf(qiblaDirection).floatValue();

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentQiblaegree, degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		arrow.startAnimation(ra);
		currentQiblaegree = degree;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		// for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onPause() {
		super.onPause();
		 // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		BigDecimal bd = new BigDecimal(Float.toString(degree));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

		BigDecimal bdq = new BigDecimal(qiblaDirection);
		bdq = bdq.setScale(2, BigDecimal.ROUND_HALF_UP);

		txtHeading.setText(new StringBuilder()
				.append(getResources().getString(R.string.north))
				.append(" ")
				.append(bd.floatValue())
				.append(getResources().getString(R.string.degrees))
				.append(", ")
				.append(getResources().getString(R.string.qibla))
				.append(": ")
				.append(bdq.floatValue())
				.append(getResources().getString(R.string.degrees)));

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
		currentDegree = -degree;
		getQiblaDirection();
	}
}
