package net.fajarachmad.prayer.common.notification;

import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.prayertime.service.PrayerTimeService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DismissButtonListener extends BroadcastReceiver implements AppConstant{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		
        Intent serviceIntent = new Intent(context, PrayerTimeService.class);
        serviceIntent.putExtra(ACTION, ACTION_STOP_SOUND);
        context.startService(serviceIntent);
		
	}

}
