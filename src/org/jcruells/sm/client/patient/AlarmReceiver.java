package org.jcruells.sm.client.patient;

import java.text.DateFormat;
import java.util.Date;

import org.jcruells.sm.client.App;


import org.jcruells.sm.client.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

	public static final String VIBRATE = "vibrate";
	public static final String TIME = "time";
	public static final int VIBRATE_YES = 1;
	public static final int VIBRATE_NO = 0;
	
	
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Text Elements
	private static String alarmTickerText = null;
	private static String alarmContentTitle = null;
	private static String alarmContentText = null;
	
	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (alarmTickerText == null) {
			Resources r = context.getResources();
			alarmTickerText = r.getString(R.string.alarm_ticker_text);
			alarmContentTitle = r.getString(R.string.alarm_content_title);
			alarmContentText = r.getString(R.string.alarm_content_text);
		}
		
		int vibrate = intent.getIntExtra(VIBRATE, VIBRATE_NO );
		String time = intent.getStringExtra(TIME);
		
		Log.d(App.DEBUG_TAG, "inside on receive");
		
		mNotificationIntent = new Intent(context, CheckInActivity.class);
		mNotificationIntent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_NEW);
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(alarmTickerText + time)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(alarmContentTitle)
				.setContentText(alarmContentText).setContentIntent(mContentIntent);
		

		if (vibrate == VIBRATE_YES) {
			notificationBuilder.setVibrate(mVibratePattern);
		}

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		
		Log.d(App.DEBUG_TAG,"Sending notification at:" + DateFormat.getDateTimeInstance().format(new Date()));
		
	}

}
