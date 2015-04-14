package org.jcruells.sm.client.doctor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Callable;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.CallableTask;
import org.jcruells.sm.client.LoginScreenActivity;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.TaskCallback;
import org.jcruells.sm.client.data.CheckIn;
import org.jcruells.sm.client.data.LocalDBSvcApi;
import org.jcruells.sm.client.data.Patient;
import org.jcruells.sm.client.data.SymptomDataContract;
import org.jcruells.sm.client.patient.AlarmsSetupActivity;
import org.jcruells.sm.client.patient.CheckinsListActivity;
import org.jcruells.sm.client.services.SyncService;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PatientCardActivity extends Activity {
	
	public static final String PATIENT = "patient";
	public static final String PERCENTAGE = "percentage";
	
	@InjectView(R.id.txtName)
	protected TextView txtName;
	
	@InjectView(R.id.txtAge)
	protected TextView txtAge;
	
	@InjectView(R.id.txtPatientID)
	protected TextView txtPatientID;
	
	@InjectView(R.id.txtLastCheckinAt)
	protected TextView lastCheckinAt;
	
	@InjectView(R.id.txtPainLevel)
	protected TextView txtPainLevel;	
	
	@InjectView(R.id.txtEatingDifficulty)
	protected TextView txtEatingDifficulty;
	
	@InjectView(R.id.painEvolution)
	protected LinearLayout painEvolution;
	
	@InjectView(R.id.btnCheckinsList)
	protected Button btnCheckinsList;
	
	@InjectView(R.id.btnChangeMedication)
	protected Button btnChangeMedication;
	
	protected Button btnSynchronize;	
	protected Button btnSetupAlarms;
	
	private App app;
	private Patient patient;
		
	private String[] painLevels;
	private String[] stoppedEatingLevels;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		final Resources r = getResources();
		painLevels = r.getStringArray(R.array.answers_pain_level);
		stoppedEatingLevels = r.getStringArray(R.array.answers_eating);
		
		Log.d(App.DEBUG_TAG, "oncreate patient record activity");
		
		Log.d(App.DEBUG_TAG, "DoctorActivity onCreate");
		//setContentView(R.layout.patient_card_activity);
		app = (App) getApplicationContext();
		patient = (Patient) getIntent().getSerializableExtra(PATIENT);
		
		
		ScrollView scrollView = (ScrollView) View.inflate(this,R.layout.patient_card_activity, null);
		if (app.isPatientRole()) {
				
				LinearLayout layout = (LinearLayout) scrollView.findViewById(R.id.patientCardLayout);
				btnSynchronize = new Button(this);
				btnSetupAlarms = new Button(this);
				
				Log.d(App.DEBUG_TAG, "btnSynchronize" + btnSynchronize);
				Log.d(App.DEBUG_TAG, "btnSetupAlarms" + btnSetupAlarms);
				
				btnSynchronize.setText(getResources().getString(R.string.action_synchronize));
				btnSynchronize.setLayoutParams(new LinearLayout.LayoutParams(
			            						 	ViewGroup.LayoutParams.WRAP_CONTENT,
			            						 	ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
				btnSetupAlarms.setText(getResources().getString(R.string.alarms_setup));
				btnSetupAlarms.setLayoutParams(new LinearLayout.LayoutParams(
			            						 	ViewGroup.LayoutParams.WRAP_CONTENT,
			            						 	ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
				
				Log.d(App.DEBUG_TAG, "add views ");
				
				LinearLayout layoutButtons = new LinearLayout(this);
				layoutButtons.setOrientation(LinearLayout.HORIZONTAL);
				layoutButtons.addView(btnSetupAlarms);
				layoutButtons.addView(btnSynchronize); 
				layout.addView(layoutButtons);
				
				Log.d(App.DEBUG_TAG, "add views after");
				
				btnSynchronize.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(App.DEBUG_TAG, "onclick synchronize");
						
						Intent intent = new Intent(PatientCardActivity.this, SyncService.class);
						
						Log.d(App.DEBUG_TAG, "start service");
						startService(intent);
					}
				});
				btnSetupAlarms.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(App.DEBUG_TAG, "onclick setup alarms");						
						Log.d(App.DEBUG_TAG, "OPen setup alarms");
						Intent intent = new Intent(PatientCardActivity.this, AlarmsSetupActivity.class);
						startActivity(intent);
					}
				});
				
				Log.d(App.DEBUG_TAG, "after set clicks listeners");
		 }
		    

		//setContentView(R.layout.patient_medications_list_activity);
		setContentView(scrollView);
		
		ButterKnife.inject(this);
		
		
		
		Log.d(App.DEBUG_TAG, "injected");
		Log.d(App.DEBUG_TAG, "app " + app);
		Log.d(App.DEBUG_TAG, "app is patient role" + app.isPatientRole());
			
		if (app.isPatientRole()) {
			Log.d(App.DEBUG_TAG, "in app is patient role");
			Log.d(App.DEBUG_TAG, "btnChangeMedication: " + btnChangeMedication);
			btnChangeMedication.setText(getResources().getString(R.string.view_medication));
		}   
		
		
		
		Log.d(App.DEBUG_TAG, "patient" + patient.getPatientName());
		
		txtName.setText(patient.getPatientName() + " " + patient.getPatientLastName());
		
		Log.d(App.DEBUG_TAG, "age");
		Log.d(App.DEBUG_TAG, "is " + patient.getAge() );
		
		txtAge.setText(String.valueOf(patient.getAge()));
		
		Log.d(App.DEBUG_TAG, "recorid");
		txtPatientID.setText(String.valueOf(patient.getRecordId()));
		
		Log.d(App.DEBUG_TAG, "lastcheckindate");
		lastCheckinAt.setText(patient.getLastCheckinDatetime());
		
		Log.d(App.DEBUG_TAG, "painlevel");
		txtPainLevel.setText(painLevels[patient.getPainLevel()]);
		
		Log.d(App.DEBUG_TAG, "stopped eating level");
		txtEatingDifficulty.setText(stoppedEatingLevels[patient.getStoppedEatingLevel()]);;
		
		Log.d(App.DEBUG_TAG, "end on create");
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		LocalDBSvcApi localDB = new LocalDBSvcApi(this);
		patient = localDB.getPatientFromLocalDB(patient.getRecordId());
		
		Log.d(App.DEBUG_TAG, "patient" + patient.getPatientName());
		
		txtName.setText(patient.getPatientName() + " " + patient.getPatientLastName());
		
		Log.d(App.DEBUG_TAG, "age");
		Log.d(App.DEBUG_TAG, "is " + patient.getAge() );
		
		txtAge.setText(String.valueOf(patient.getAge()));
		
		Log.d(App.DEBUG_TAG, "recorid");
		txtPatientID.setText(String.valueOf(patient.getRecordId()));
		
		Log.d(App.DEBUG_TAG, "lastcheckindate");
		lastCheckinAt.setText(patient.getLastCheckinDatetime());
		
		Log.d(App.DEBUG_TAG, "painlevel");
		txtPainLevel.setText(painLevels[patient.getPainLevel()]);
		
		Log.d(App.DEBUG_TAG, "stopped eating level");
		txtEatingDifficulty.setText(stoppedEatingLevels[patient.getStoppedEatingLevel()]);;
		
		
		Log.d(App.DEBUG_TAG, "PatientRecordActivity onResume");
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		Log.d(App.DEBUG_TAG, "-----------display evolution -----------------------");
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		
		
		displayEvolution(patient.getRecordId());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		Log.d(App.DEBUG_TAG, "-----------display evolution -----------------------");
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		Log.d(App.DEBUG_TAG, "----------------------------------------------------");
		
		
		displayEvolution(patient.getRecordId());
	}
	
	@OnClick(R.id.btnCheckinsList)
	public void openCheckinsList() {
		
		Intent i = new Intent(this, CheckinsListActivity.class);
		i.putExtra(CheckinsListActivity.RECORD_ID, patient.getRecordId());
		
		Log.d(App.DEBUG_TAG, "start activity check ins list with recordId " + patient.getRecordId());
		startActivity(i);
	
	}
	
	@OnClick(R.id.btnChangeMedication)
	public void changeMedication() {
		
		Intent i = new Intent(this, PatientMedicationListActivity.class);
		if (app.isDoctorRole()) {
			i.putExtra(PatientMedicationListActivity.ACTION, PatientMedicationListActivity.ACTION_EDIT);
		} else {
			i.putExtra(PatientMedicationListActivity.ACTION, PatientMedicationListActivity.ACTION_VIEW);
		}		
		i.putExtra(PatientMedicationListActivity.RECORD_ID, patient.getRecordId());
		i.putExtra(PatientMedicationListActivity.NAME, patient.getPatientName() + " " + patient.getPatientLastName());
		Log.d(App.DEBUG_TAG, "start activity check ins list with recordId " + patient.getRecordId());
		startActivity(i);
	
	}
	
	
	private void displayEvolution(final int recordId) {
		
		Log.d(App.DEBUG_TAG, "in display evlution");
		
		CallableTask.invoke(new Callable<ArrayList<ContentValues>>() {
			@Override
			public ArrayList<ContentValues> call() throws Exception {
				return getEvolution(recordId);
			}
		}, new TaskCallback<ArrayList<ContentValues>>() {
			@Override
			public void success(ArrayList<ContentValues> result) {
				Log.d(App.DEBUG_TAG, "success");
				
				painEvolution.removeAllViews();
				Log.d(App.DEBUG_TAG, "remove all views");
				int painEvolutionWidth = (int) (painEvolution.getWidth());
				
				Log.d(App.DEBUG_TAG, "painEvoultionWidth = " + painEvolutionWidth);
				Log.d(App.DEBUG_TAG, "result.size() " + result.size());
				
				//Iterator<ContentValues> i  =  result.iterator();
				//while (i.hasNext()) {
				int i;
				for (i = 0; i<result.size(); i++ ) {
					Log.d(App.DEBUG_TAG, "itereate i " + i);
					addPainLevelShape(painEvolutionWidth, result.get(i));
				}
			
			}
			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
				
				
			}
		});
	}
	
	
	private ArrayList<ContentValues> getEvolution(final int recordId) {
		Log.d(App.DEBUG_TAG, "in getEvolution");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar c = Calendar.getInstance();
		Date currentDateTime = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, -2);
		String fromDateTime = sdf.format(c.getTime());
		Log.d(App.DEBUG_TAG, "gettin checkins from time " + fromDateTime
				+ " and for record id " + patient.getRecordId());
		
		ContentResolver cr = getContentResolver();
		String selection = SymptomDataContract.PATIENT_RECORD_ID + " = ? AND " +
						   SymptomDataContract.CHECK_IN_DATETIME + " >= '" + fromDateTime + "'";
		String[] conditions = {String.valueOf(recordId)};
		String order = SymptomDataContract.CHECK_IN_DATETIME + " DESC";
		Cursor cursor = cr.query(SymptomDataContract.CHECKINS_URI, 
				SymptomDataContract.CHECKINS_ALL_COLUMNS,
				selection, conditions, order);
		CheckIn checkIn;
		ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
		if (cursor.moveToFirst()) {
			do {
				checkIn = new CheckIn(
						cursor.getLong(cursor.getColumnIndex(SymptomDataContract.ID)), 
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PATIENT_RECORD_ID)), 
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.CHECK_IN_DATETIME)), 
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.PAIN_LEVEL)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.MEDICATION_TAKEN)), 
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_DATETIME)), 
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.MEDICATION_ANSWERS)), 
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.STOPPED_EATING_LEVEL)),
						cursor.getString(cursor.getColumnIndex(SymptomDataContract.SERVER_TIMESTAMP)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNC_ACTION)),
						cursor.getInt(cursor.getColumnIndex(SymptomDataContract.SYNCED))
						);
				
				checkIns.add(checkIn);
			} while (cursor.moveToNext());
			
		}
		
		
		Log.d(App.DEBUG_TAG, "created list wit " + checkIns.size());
		
		//Create a list of content values containing each the pain level and the duration in minutes 
		ArrayList<ContentValues> evolution = new ArrayList<ContentValues>(); 
		int i;
		int size = checkIns.size();
		int last = size-1;
		//int acumulatedMinutes = 0;
		Date toTime = null;
		Date fromTime = null;
		float difMinutes;
		float percentage,accumulatedPercentage = 0;
		final float TOTAL_MINUTES = 48 * 60; // Last 48 hours
		ContentValues values;
		//boolean stop = false;
		
		try {
			for (i=0; i<size; i++) {
				Log.d(App.DEBUG_TAG, "iteration " + i);
				checkIn = checkIns.get(i);
				if (i == 0) {
					toTime = currentDateTime;
				} else {
					toTime = fromTime;
				}
				fromTime = sdf.parse(checkIn.getCheckinDatetime());
				
				/*if ( (acumulatedMinutes + difMinutes ) > TOTAL_MINUTES) {
					difMinutes = TOTAL_MINUTES - acumulatedMinutes;
					stop = true;
				} else {
					acumulatedMinutes += difMinutes;
				}*/
				
				Log.d(App.DEBUG_TAG, "toTime.getTime() " + toTime.getTime());
				Log.d(App.DEBUG_TAG, "fromTime.getTime() " + fromTime.getTime());
				if (i == last) {
					//difMinutes = TOTAL_MINUTES - acumulatedMinutes;
					percentage = 1f - accumulatedPercentage;
				} else {
					difMinutes = (toTime.getTime() - fromTime.getTime()) / (60 * 1000);
					Log.d(App.DEBUG_TAG, "diff mniutes " + difMinutes);
					percentage = difMinutes / TOTAL_MINUTES;
					accumulatedPercentage += percentage;
				}
				
				values = new ContentValues();
				values.put(SymptomDataContract.PAIN_LEVEL,  + checkIn.getPainLevel());
				//values.put(SymptomDataContract.PAIN_MINUTES, difMinutes);
				values.put(PERCENTAGE, percentage);
				
				Log.d(App.DEBUG_TAG, " put percentage "  + percentage);
				Log.d(App.DEBUG_TAG, " put painlevel "  + checkIn.getPainLevel());
				evolution.add(values);
				
				/*if (stop) {
					break;
				}*/
				
			}
			
			
		} catch (ParseException e) {
			Log.d(App.DEBUG_TAG,"could not parse all dates");
		}
		
		Collections.reverse(evolution);
		
		return evolution;
	}
	
	
	private void addPainLevelShape(int painEvolutionWidth, ContentValues values) {
		
		Log.d(App.DEBUG_TAG, "in addPainLevelShape " + values.getAsInteger(SymptomDataContract.PAIN_LEVEL) + "-" + values.getAsFloat(PERCENTAGE));
		final int painLevel = values.getAsInteger(SymptomDataContract.PAIN_LEVEL);
		final int[] colors = {Color.WHITE, Color.GREEN, Color.CYAN, Color.RED};
		final float percentage = values.getAsFloat(PERCENTAGE);
		final int width = (int) (percentage * painEvolutionWidth);
		final int height = 15 * painLevel;
		final int color = colors[painLevel];
		
		Log.d(App.DEBUG_TAG, "painLevel " + painLevel);
		Log.d(App.DEBUG_TAG, "width " + width);
		Log.d(App.DEBUG_TAG, "height " + height);
		
		
		
		
		// Create Rect Shape
		ShapeDrawable rectShape = new ShapeDrawable(new RectShape());
		rectShape.getPaint().setColor(color);
		rectShape.setIntrinsicHeight(height);
		rectShape.setIntrinsicWidth(width);

		// Put Rect Shape into an ImageView
		ImageView rectView = new ImageView(getApplicationContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
		layoutParams.gravity=Gravity.BOTTOM;
		rectView.setLayoutParams(layoutParams);
		rectView.setImageDrawable(rectShape);
		//rectView.setPadding(padding, padding, padding, padding);

		Log.d(App.DEBUG_TAG, "image view created");
		
		// Specify placement of ImageView within RelativeLayout
		//LinearLayout.LayoutParams rectViewLayoutParams = new LinearLayout.LayoutParams(width,50);
		//rectViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		//rectViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//rectView.setLayoutParams(new LinearLayout.LayoutParams(width,50));
		painEvolution.addView(rectView);
		Log.d(App.DEBUG_TAG, "image view aded");
		
	}

}
