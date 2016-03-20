package net.fajarachmad.prayer.prayertime.fragment;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.receiver.PrayerTimeReceiver;
import net.fajarachmad.prayer.prayertime.service.AsyncPrayerTimeService;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerTime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;

public class PrayerInfoFragment extends Fragment {
	
	private String nextPrayerName;
	private String nextPrayerTime;
	private String remainigTime;
	private String upcomingPray;
	private CountDownTimer timer;
	private View view;
	private Fragment fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nextPrayerName = getArguments().getString("nextPrayerName");
		nextPrayerTime = getArguments().getString("nextPrayerTime");
		remainigTime = getArguments().getString("remainingTime");
		upcomingPray = getArguments().getString("upcomingPray");
		fragment = this;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.prayertime_pager_prayerinfo, container, false);
		
		((TextView) view.findViewById(R.id.next_prayer)).setText(nextPrayerName);
		((TextView) view.findViewById(R.id.next_pray_time)).setText(nextPrayerTime);
		((TextView) view.findViewById(R.id.remaining_time)).setText(remainigTime);
		((TextView) view.findViewById(R.id.upcoming_prayer)).setText(upcomingPray);
		return view;
	}
	
	public void updateValue(String nextPrayerName, String nextPrayerTime,  String upcomingPray) {
		((TextView) view.findViewById(R.id.next_prayer)).setText(nextPrayerName);
		((TextView) view.findViewById(R.id.next_pray_time)).setText(nextPrayerTime);
		((TextView) view.findViewById(R.id.upcoming_prayer)).setText(upcomingPray);
	}
	
	public void updateRemainingTime(String value) {
		((TextView) view.findViewById(R.id.remaining_time)).setText(value);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, new IntentFilter(AsyncPrayerTimeService.ACTION_UPDATE_NEXT_PRAYER));
	}

	private void showRemainingTime(final PrayerTime prayerTime) {
		Date current = new Date();
		long deadline = prayerTime.getPrayDate().getTime() - current.getTime();

		if (timer != null) {
			timer.cancel();
		}

		timer = new CountDownTimer(deadline, 60000) {

			@Override
			public void onFinish() {
				Looper.myLooper().quit();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				long mnt = Double.valueOf(
						Math.floor(millisUntilFinished / 1000 / 60) % 60)
						.longValue();
				long hrs = Double
						.valueOf(
								Math.floor(millisUntilFinished
										/ (1000 * 60 * 60) % 24)).longValue();

				String text = String.valueOf(hrs) + " " +getResources().getString(R.string.pref_hours)+" "
						+ String.valueOf(mnt) + " "+getResources().getString(R.string.pref_minutes)+" "+getResources().getString(R.string.left_until)
						+ " "+prayerTime.getPrayName();

				updateRemainingTime(text);
			}

		}.start();
		Looper.loop();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String prayerTimeStr = intent.getStringExtra(PrayerTime.class.getName());
			Gson gson = new Gson();
			PrayerTime prayerTime = gson.fromJson(prayerTimeStr, PrayerTime.class);
			try {
				updateValue(prayerTime.getPrayName(), prayerTime.getPrayTime(), getContext().getResources().getString(R.string.upcoming_prayer));
				showRemainingTime(prayerTime);
			} catch (Exception e) {
				
			}

		}
	};
	
}
