package net.fajarachmad.prayer.common.receiver;

import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.prayertime.fragment.PrayerTimeFragment;
import net.fajarachmad.prayer.prayertime.wrapper.Prayer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

public class PrayerTimeReceiver extends BroadcastReceiver implements AppConstant{
	
	public static final String PROCESS_RESPONSE = Intent.ACTION_VIEW;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getStringExtra(ACTION);
		
		switch (action) {
		case ACTION_PRAYER_TIME_CHANGED:
			break;
		case ACTION_REMAINING_TIME_CHANGED:
			onRemainingTimeChanged(intent);
			break;

		default:
			break;
		}
	}
	
	private void onRemainingTimeChanged(Intent intent) {
		String remainingTime = intent.getStringExtra("remainingTime");
		if (remainingTime != null) {
			if (PrayerTimeFragment.getInstance() != null) {
				PrayerTimeFragment.getInstance().updateRemainingTime(remainingTime);
			}
		}
	}
	

}
