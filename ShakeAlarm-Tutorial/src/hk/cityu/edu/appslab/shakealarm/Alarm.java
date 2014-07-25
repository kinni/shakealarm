package hk.cityu.edu.appslab.shakealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Alarm extends BroadcastReceiver {

	
	// Automatically triggered when receive signal from Android OS
	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Calling the AlarmActivity
		intent.setClass(context, AlarmActivity.class);
		context.startActivity(intent);
	}

	// Set Alarm
	public void set(Context context, long triggerAtMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
	}

	// Cancel Alarm
	public void cancel(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

}
