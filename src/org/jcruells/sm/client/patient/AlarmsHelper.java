package org.jcruells.sm.client.patient;

import java.util.Calendar;
import java.util.HashMap;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.data.Alarm;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class AlarmsHelper {

	
	private static HashMap<String, PendingIntent> alarms;
	private static Context ctx;
	private static AlarmManager alarmMgr;
	private static int requestCode;
	
	
	public AlarmsHelper(Context ctx) {
		this.ctx = ctx;
		alarmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
		alarms = new HashMap<String, PendingIntent>() ;
	}
	
	// Every time the app is started we will get the saved alarms 
	// and start each of them
	public void loadAlarms() {
		new GetAlarmsTask().execute();
	}
	
	public static void addAlarm(String time, int vibrate) {
		
		Log.d(App.DEBUG_TAG, "add alarm");
		 Intent i;
    	 PendingIntent alarmIntent;
    	 String[] hourAndMinutes;
    	 int hour, minutes;
    	 
		 hourAndMinutes = time.split(":");
		 hour = Integer.valueOf(hourAndMinutes[0]);
		 minutes = Integer.valueOf(hourAndMinutes[1]);
		 Log.d(App.DEBUG_TAG, "hour: " + hour + ", minutes: " + minutes);
		 
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(System.currentTimeMillis());
		 calendar.set(Calendar.HOUR_OF_DAY, hour);
		 calendar.set(Calendar.MINUTE, minutes);
		 calendar.set(Calendar.SECOND, 0);
		 
		 i = new Intent(ctx, AlarmReceiver.class);
		 i.putExtra(AlarmReceiver.VIBRATE, vibrate);
		 i.putExtra(AlarmReceiver.TIME, time);
		 requestCode ++;
		 alarmIntent = PendingIntent.getBroadcast(ctx, requestCode, i, 0);
		 
		 Log.d(App.DEBUG_TAG, "settin up alarm with request code " + requestCode);
		 
		 alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
			        AlarmManager.INTERVAL_DAY, alarmIntent);
		 
		 alarms.put(time, alarmIntent);
		
		 Log.d(App.DEBUG_TAG, alarms.size() + " alarms now");
		 
	}
	
	public static void updateAlarm(String time, String newTime, int vibrate) {
	
		Log.d(App.DEBUG_TAG, "update alarm");
		
		PendingIntent alarm = alarms.remove(time);
		
		if (alarm != null) {
			
			alarmMgr.cancel(alarm);
			addAlarm(newTime, vibrate);
			
		}
		
		Log.d(App.DEBUG_TAG, alarms.size() + " alarms now");
		
	}

	
	public static void deleteAlarm(String time) {

		Log.d(App.DEBUG_TAG, "delete alarm");
		
		PendingIntent alarm = alarms.remove(time);
		
		if (alarm != null) {			
			alarmMgr.cancel(alarm);
		}
		
		Log.d(App.DEBUG_TAG, alarms.size() + " alarms now");
	}
	
	
	
	
	private class GetAlarmsTask extends AsyncTask<Void, Integer, Cursor> {
		
	     protected Cursor doInBackground(Void... p) {
	    	 
	    	 Log.d(App.DEBUG_TAG, "in GetAlarmsTask doinbvackground");
	    	 
	    	 String recordId = String.valueOf(((App) ctx).getUser().getRecordNumber());
	    	 Uri alarmsUri = Uri.withAppendedPath(SymptomDataContract.ALARMS_URI, recordId);
	    	 String selection = SymptomDataContract.PATIENT_RECORD_ID + " = ? AND " + SymptomDataContract.ALARM_ACTIVATED + " = ?";
	    	 String[] selectionArgs = {recordId, String.valueOf(Alarm.ALARM_ACTIVATED)};
	    	 
	    	 return ctx.getContentResolver().query(alarmsUri, SymptomDataContract.ALARM_ALL_COLUMNS, selection, selectionArgs, null);
	     }
	     
	     protected void onPostExecute(Cursor c) {
	    	 
	    	 Log.d(App.DEBUG_TAG, "in GetAlarmsTask onpostexecute");
	    	
	    	 
	    	 // Wake all alarms retrieved by the cursor
	    	 if (c.moveToFirst()) {
	    		 
	    		 do {
	    			 
	    			 Log.d(App.DEBUG_TAG, "setting up alarm");
	    			 String time = c.getString(c.getColumnIndex(SymptomDataContract.ALARM_TIME));
	    			 int vibrate = (int) c.getLong(c.getColumnIndex(SymptomDataContract.ALARM_VIBRATE));
	    			 addAlarm(time, vibrate);
	    			 
	    		 } while (c.moveToNext());
	    		 c.close();
	    	 }
	    	 
	    	 Log.d(App.DEBUG_TAG, alarms.size() + " alarms now");
	         
	     }
	 }

	
}
