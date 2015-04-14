package org.jcruells.sm.client.patient;

import java.text.DateFormat;
import java.util.Date;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.data.SymptomDataContract;
import org.jcruells.sm.client.doctor.PatientMedicationListActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public class ChangedMedicationReceiver extends BroadcastReceiver{

	public static final String VIBRATE = "vibrate";
	public static final String TIME = "time";
	public static final int VIBRATE_YES = 1;
	public static final int VIBRATE_NO = 0;
	public static final String RECORD_ID ="recordId";
	
	public static final String CHANGED_MEDICATION_ALERT = "org.jcruells.sm.client.CHANGED_MEDICATION_ALERT";
	
	
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Text Elements
	private static String changedMedicationTickerText = null;
	private static String changedMedicationContentTitle = null;
	private static String changedMedicationContentText = null;
	
	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (changedMedicationTickerText == null) {
			Resources r = context.getResources();
			changedMedicationTickerText = r.getString(R.string.changed_medication_ticker_text);
			changedMedicationContentTitle = r.getString(R.string.changed_medication_content_title);
			changedMedicationContentText = r.getString(R.string.changed_medication_content_text);
		}
		
		int recordId = intent.getIntExtra(RECORD_ID,0);
		
		Log.d(App.DEBUG_TAG, "inside on receive");
		//final App app = (App) context; 
		mNotificationIntent = new Intent(context, PatientMedicationListActivity.class);
		mNotificationIntent.putExtra(PatientMedicationListActivity.RECORD_ID, recordId);
		mNotificationIntent.putExtra(PatientMedicationListActivity.NAME, "");
		mNotificationIntent.putExtra(PatientMedicationListActivity.ACTION, PatientMedicationListActivity.ACTION_VIEW);
		
		
		//mNotificationIntent.putExtra(CheckInActivity.ACTION, CheckInActivity.ACTION_NEW);
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(changedMedicationTickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(changedMedicationContentTitle)
				.setContentText(changedMedicationContentText).setContentIntent(mContentIntent)
				.setVibrate(mVibratePattern);
		
		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		
		Log.d(App.DEBUG_TAG,"Sending changed medication notification at:" + DateFormat.getDateTimeInstance().format(new Date()));
		
	}

}
