package org.jcruells.sm.client.doctor;

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

public class PatientAlertReceiver extends BroadcastReceiver{

	public static final String VIBRATE = "vibrate";
	public static final String TIME = "time";
	public static final int VIBRATE_YES = 1;
	public static final int VIBRATE_NO = 0;
	
	public static final String INCOMING_PATIENT_ALERT = "org.jcruells.sm.client.INCOMING_PATIENT_ALERT";
	
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Text Elements
	private static String patientAlertTickerText = null;
	private static String patientAlertContentTitle = null;
	private static String patientAlertContentText = null;
	
	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (patientAlertTickerText == null) {
			Resources r = context.getResources();
			patientAlertTickerText = r.getString(R.string.patient_alert_ticker_text);
			patientAlertContentTitle = r.getString(R.string.patient_alert_content_title);
			patientAlertContentText = r.getString(R.string.patient_alert_content_text);
		}
		
		/*int vibrate = intent.getIntExtra(VIBRATE, VIBRATE_NO );
		String time = intent.getStringExtra(TIME);*/
		
		Log.d(App.DEBUG_TAG, "inside on receive");
		
		mNotificationIntent = new Intent(context, PatientsAlarmsListActivity.class);
		//mNotificationIntent.putExtra(PatientsAlarmsListActivity.ACTION, CheckInActivity.ACTION_NEW);
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(patientAlertTickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(patientAlertContentTitle)
				.setContentText(patientAlertContentText).setContentIntent(mContentIntent)
				.setVibrate(mVibratePattern);
		

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		
		Log.d(App.DEBUG_TAG,"Sending patient alarm notification at:" + DateFormat.getDateTimeInstance().format(new Date()));
		
	}

}
