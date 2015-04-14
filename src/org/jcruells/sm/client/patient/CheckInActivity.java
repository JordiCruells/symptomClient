package org.jcruells.sm.client.patient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import org.jcruells.sm.client.App;
import org.jcruells.sm.client.CallableTask;
import org.jcruells.sm.client.R;
import org.jcruells.sm.client.TaskCallback;
import org.jcruells.sm.client.data.CheckIn;
import org.jcruells.sm.client.data.SymptomDataContract;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.google.common.base.Joiner;

public class CheckInActivity extends Activity {

	
	private int action;
	private CheckIn checkin;
	
	public static final String CHECKIN = "checkin";
	public static final String ACTION = "action";
	public static final int ACTION_VIEW = 0;
	public static final int ACTION_EDIT = 1;
	public static final int ACTION_NEW = 2;	
	
	
	
	@InjectView(R.id.btnSaveCheckin)
	protected Button btnSaveCheckIn;
	
	@InjectView(R.id.selPainLevel)
	protected Spinner selPainLevel;
	
	@InjectView(R.id.answerMedication)
	protected TextView answerMedication;
		
	@InjectView(R.id.medicationTextAnswers)
	protected TextView medicationTextAnswers;
	
	@InjectView(R.id.selEating)
	protected Spinner selEating;
	
	
	@InjectView(R.id.btnChangePainInfo)
	protected Button btnChangePainInfo;

	@InjectView(R.id.btnChangeMedicationInfo)
	protected Button btnChangeMedicationInfo;
	
	@InjectView(R.id.btnChangeEatingInfo)
	protected Button btnChangeEatingInfo;
	
	@InjectView(R.id.btnChangeListMedicationAnswers)
	protected Button btnChangeListMedicationAnswers;
	
	
	
	private final int MEDICATION_ACTIVITY = 1;
	private final int MEDICATION_LIST_ACTIVITY = 2;
	
	private int painLevel;
	private int eatingLevel;
	private int medicationTaken = 1;
	private String time;
	private String date;
	private String formatedDateTime;
	private ArrayList<Integer> medicationAnswers;
	private String textMedicationAnswers="";
	
	private String answerListMedications=""; 
	private String answerListMedicationsNone="";
	private Resources resources;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		Log.d(App.DEBUG_TAG, "on create checin activity");
		
		resources = getResources();		
		answerListMedications = resources.getString(R.string.answer_list_medications);
		answerListMedicationsNone = resources.getString(R.string.answer_list_medications_none);
		
		setContentView(R.layout.checkin_activity);
		
		Log.d(App.DEBUG_TAG, "ButterKnife inject this");
		ButterKnife.inject(this);
	
		  
	    Log.d(App.DEBUG_TAG, "popuate spinner");

		//Populate the spinner for selecting the pain level
		ArrayAdapter<CharSequence> painLevelAdapter = ArrayAdapter.createFromResource(this, R.array.answers_pain_level, android.R.layout.simple_spinner_item);
		painLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selPainLevel.setAdapter(painLevelAdapter);
		
		
		Log.d(App.DEBUG_TAG, "popuate spinner 2");
		ArrayAdapter<CharSequence> eatingLevelAdapter = ArrayAdapter.createFromResource(this, R.array.answers_eating, android.R.layout.simple_spinner_item);
		eatingLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selEating.setAdapter(eatingLevelAdapter);
		
		String actionString = null;
		Resources res = getResources();
		
		
		Log.d(App.DEBUG_TAG, "get bundle");
		Bundle extras = getIntent().getExtras();
		
		
		if (extras != null) {
		    
			action = extras.getInt(ACTION);
		    
			Log.d(App.DEBUG_TAG, "get chekin");
			// Get the checkin if it's not new
		    if (action == ACTION_EDIT || action == ACTION_VIEW) {
		    	checkin = (CheckIn) extras.getSerializable(CHECKIN);
		    	
		    	Log.d(App.DEBUG_TAG, "get medicationAnswers list");
		        medicationAnswers = checkin.getMedicationAnswersArrayList();
		        
		        if ( medicationAnswers.size() > 0) {
					Log.d(App.DEBUG_TAG, "buildMedicationsInitialMessage");
					buildMedicationsInitialMessage();
				} else {
					medicationTextAnswers.setText(answerListMedicationsNone);
				}
		             
		    } else {
		    	medicationAnswers = new ArrayList<Integer>() ;
		    }
		   
			Log.d(App.DEBUG_TAG, "intialize ui");
			//Initialize the user interface
			switch(action) {
				case ACTION_EDIT:
					selEating.setSelection(checkin.getStoppedEatingLevel());
					selPainLevel.setSelection(checkin.getPainLevel());
					setTextPainMedication(checkin.getMedicationTaken(), formatedTime(checkin.getMedicationDatetime()));
					actionString = res.getString(R.string.action_edit);
					break;
				case ACTION_NEW:
					actionString = res.getString(R.string.action_new);
					break;
				case ACTION_VIEW:
					selEating.setSelection(checkin.getStoppedEatingLevel());
					selPainLevel.setSelection(checkin.getPainLevel());
					btnChangeListMedicationAnswers.setEnabled(false);
					btnChangeMedicationInfo.setEnabled(false);
					btnChangePainInfo.setEnabled(false);
					btnChangeEatingInfo.setEnabled(false);
					selEating.setEnabled(false);
					selPainLevel.setEnabled(false);
					setTextPainMedication(checkin.getMedicationTaken(), formatedTime(checkin.getMedicationDatetime()));
					btnSaveCheckIn.setVisibility(View.INVISIBLE);				
					actionString = res.getString(R.string.action_view);
				default:
					actionString = res.getString(R.string.action_view);
					break;
			}
		}
		

		setTitle(String.format(res.getString(R.string.title_activity_checkin), actionString));
		
		Log.d(App.DEBUG_TAG, "action: " + action);
		
			
		
		
		
		
		Log.d(App.DEBUG_TAG, "exit oncreate");		
		
	}	
	
	@OnClick(R.id.btnSaveCheckin)
	public void saveCheckin() {
		Log.d(App.DEBUG_TAG,"SAVE CHECKIN");		
		Log.d(App.DEBUG_TAG,"painLevel: " + painLevel);
		Log.d(App.DEBUG_TAG,"eatingLevel: " + eatingLevel);
		Log.d(App.DEBUG_TAG,"medicationTaken: " + medicationTaken);
		Log.d(App.DEBUG_TAG,"time: " + time);
		Log.d(App.DEBUG_TAG,"date: " + date);
		Log.d(App.DEBUG_TAG,"formatedDateTime: " + formatedDateTime);
		Log.d(App.DEBUG_TAG,"listAnswers: " + medicationAnswers);		
		Log.d(App.DEBUG_TAG,"textAnswers: " + textMedicationAnswers);
		
		
		Log.d(App.DEBUG_TAG,"CREATE CONTENT VALUES");	
		// Insert the new check-in
		ContentValues mNewCheckIn = new ContentValues();
		mNewCheckIn.put(SymptomDataContract.PATIENT_RECORD_ID , ((App)getApplicationContext()).getUser().getRecordNumber());
		mNewCheckIn.put(SymptomDataContract.PAIN_LEVEL , painLevel);
		mNewCheckIn.put(SymptomDataContract.MEDICATION_TAKEN , medicationTaken);
		mNewCheckIn.put(SymptomDataContract.MEDICATION_DATETIME , date + " " + time);
		
		Log.d(App.DEBUG_TAG, "answers: " + Joiner.on(";").join(medicationAnswers));
		
		mNewCheckIn.put(SymptomDataContract.MEDICATION_ANSWERS , Joiner.on(";").join(medicationAnswers));
		mNewCheckIn.put(SymptomDataContract.STOPPED_EATING_LEVEL , eatingLevel);
		mNewCheckIn.put(SymptomDataContract.SYNC_ACTION , SymptomDataContract.SYNC_INSERT);
		mNewCheckIn.put(SymptomDataContract.SYNCED , SymptomDataContract.STATE_NOT_SYNCED);
		mNewCheckIn.put(SymptomDataContract.CHECK_IN_DATETIME , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		
		Log.d(App.DEBUG_TAG,"EXECUTE INSERT");
		Uri mNewUri = getContentResolver().insert(
				SymptomDataContract.CHECKINS_URI,  
						    mNewCheckIn                        
					  );
		
		Log.d(App.DEBUG_TAG,"INSERT EXECUTED");
		
		if (mNewUri != null) {
			Toast.makeText(this, resources.getString(R.string.status_OK_inserted_checkin), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, resources.getString(R.string.status_KO_inserted_checkin), Toast.LENGTH_LONG).show();
		}

		finish();
	}
	
	@OnClick(R.id.answerMedication)
	public void clickAnswerMedication() {
		changeMedicationInfo();
	}
	
	@OnClick(R.id.medicationTextAnswers)
	public void clickMedicationTextAnswers() {
		changeListMedicationAnswers();
	}
	
	@OnClick(R.id.btnChangePainInfo)
	public void changePainInfo() {
		
		selPainLevel.performClick();
	
	}
	
	@OnClick(R.id.btnChangeEatingInfo)
	public void changeEatingInfo() {
		
		selEating.performClick();
	
	}
	@OnClick(R.id.btnChangeMedicationInfo)
	public void changeMedicationInfo() {
		Log.d(App.DEBUG_TAG, "changeMedicationInfo painlevel " + painLevel);
		Intent intent = new Intent(this, CheckinMedicationActivity.class);
		intent.putExtra("date",date);
		intent.putExtra("time",time);
		intent.putExtra("medicationTaken",medicationTaken);
		intent.putExtra("formatedDateTime",formatedDateTime);
		startActivityForResult(intent, MEDICATION_ACTIVITY);
		Log.d(App.DEBUG_TAG, "changeMedicationInfo return painlevel " + painLevel);
	}
	
	@OnClick(R.id.btnChangeListMedicationAnswers)
	public void changeListMedicationAnswers() {
		
		Intent intent = new Intent(this, CheckinMedicationListActivity.class);
		
		intent.putIntegerArrayListExtra("medicationAnswers", medicationAnswers);
		startActivityForResult(intent, MEDICATION_LIST_ACTIVITY);
	}
	

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d(App.DEBUG_TAG, "on activity result in");
		Log.d(App.DEBUG_TAG, "resultCode: " + resultCode);
		
		
		if (resultCode == RESULT_OK) {
			
			Bundle extras = data.getExtras();
			switch(requestCode) {
				case MEDICATION_ACTIVITY:	
					medicationTaken = extras.getInt("medicationTaken", medicationTaken);
					time = extras.getString("time", time);
					date = extras.getString("date", date);
					formatedDateTime = extras.getString("formatedDateTime", formatedDateTime);
					Log.d(App.DEBUG_TAG, "on activity result " + medicationTaken + "-" + time + "-" + date);
					
					setTextPainMedication(medicationTaken, formatedDateTime);
					
					break;
					
				case MEDICATION_LIST_ACTIVITY:
					
					medicationAnswers = extras.getIntegerArrayList("medicationAnswers");
					textMedicationAnswers = extras.getString("textMedicationAnswers");
					
					Log.d(App.DEBUG_TAG, "on activity result medication list activity -> " + textMedicationAnswers);
					Log.d(App.DEBUG_TAG, "on activity result medication list activity -> " + medicationAnswers);
					
					if (medicationAnswers.size() > 0) { 
						medicationTextAnswers.setText(String.format(answerListMedications, textMedicationAnswers));
					} else {
						medicationTextAnswers.setText(answerListMedicationsNone);
					}
					break;
				default:
			
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(App.DEBUG_TAG, "CheckIn onResume");
		
		
		    
		    Log.d(App.DEBUG_TAG, "enable listeners");
		    // Enable listeners if we are editing
		    if (action == ACTION_EDIT || action == ACTION_NEW) {
		    	
			    Log.d(App.DEBUG_TAG, "sel pain level listener");
				 
				selPainLevel.setOnItemSelectedListener(
						new AdapterView.OnItemSelectedListener() {
							
							
							@Override
							public void onItemSelected(AdapterView<?> parent, View view,
									int pos, long id) {
								painLevel = pos;
							}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								painLevel = 0;
							}
						}
						
				);
				
				Log.d(App.DEBUG_TAG, "sel eating level listener");
				selEating.setOnItemSelectedListener(
						new AdapterView.OnItemSelectedListener() {
							
							
							@Override
							public void onItemSelected(AdapterView<?> parent, View view,
									int pos, long id) {
								eatingLevel = pos;
							}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								eatingLevel = 0;
							}
						}
						
				);
		    }
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (action == ACTION_EDIT || action == ACTION_NEW) {
		    	
			   Log.d(App.DEBUG_TAG, "disable listeneres");
			   selPainLevel.setOnItemSelectedListener(null);
			   selEating.setOnItemSelectedListener(null);
		}
	}
	
	
	private void setTextPainMedication(int medicationTaken, String formatedDateTime) {
	
		if (medicationTaken > 0) {
			answerMedication.setText((String.format(getResources().getString(R.string.answer_medication_yes), formatedDateTime)));
		} else {
			answerMedication.setText(getResources().getString(R.string.answer_medication_no));
		}
	}
			
	
	private String formatedTime(String dateTime) {
		
		Log.d(App.DEBUG_TAG, "formatedTime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		try {
			d = sdf.parse(dateTime);
		} catch(Exception e) {
			Log.d(App.DEBUG_TAG, "Exception whe parsin date at CheckinActivity- formatedTime");
			return "";
		}
		
		final String stringTime = new SimpleDateFormat(" HH:mm").format(d);
		final DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		final String stringDate =  df.format(d); 
		
		Log.d(App.DEBUG_TAG, "stringTime " + stringTime + " stringDate " + stringDate);
		
		return new StringBuilder(stringDate).append(stringTime).toString();
		
	}

	
	private void buildMedicationsInitialMessage() {

		CallableTask.invoke(new Callable<Cursor>() {
			@Override
			public Cursor call() throws Exception {
				final String listDelimiter = resources.getString(R.string.list_delimiter);
				final String endListDelimiter = resources.getString(R.string.end_list_delimiter);
				
				int size = medicationAnswers.size(), i = 0;
				StringBuilder selectionBuilder = new StringBuilder( SymptomDataContract.ID + " IN (");
				for (i=0; i<size; i++) { 
					selectionBuilder.append(medicationAnswers.get(i));
					if (i < (size-1)) { 
						selectionBuilder.append(", ");
					} 
				}
				selectionBuilder.append(")");
				
				Log.d(App.DEBUG_TAG, "selection + criteria = " + selectionBuilder.toString());
				Log.d(App.DEBUG_TAG, "query medications");

				
				return getContentResolver().query(SymptomDataContract.MEDICATIONS_URI, SymptomDataContract.MEDICATION_ALL_COLUMNS, selectionBuilder.toString(), null, null);
			}
		}, 
		new TaskCallback<Cursor>() {
			@Override
			public void success(Cursor result) {
				// Fetch the cursor and for
				Log.d(App.DEBUG_TAG, "query medications success");
				
				final String listDelimiter = resources.getString(R.string.list_delimiter);
				final String endListDelimiter = resources.getString(R.string.end_list_delimiter);
				
				//answer_list_medications
				
				
				StringBuilder sb = new StringBuilder(); 
				int rowBeforeLast = result.getCount() - 2;
				int position;
				
				Log.d(App.DEBUG_TAG, "rowBeforeLast " + rowBeforeLast);
				
				if (result.moveToFirst()) {
					
					do {
						
						Log.d(App.DEBUG_TAG, "result.getPosition() " + result.getPosition());
						Log.d(App.DEBUG_TAG, "medication " + result.getString(result.getColumnIndex(SymptomDataContract.MEDICATION_NAME)));
						
						sb.append(result.getString(result.getColumnIndex(SymptomDataContract.MEDICATION_NAME)));
						position = result.getPosition();
						if (position == rowBeforeLast) {
							Log.d(App.DEBUG_TAG, "sb.append(endListDelimiter) ");
							sb.append(endListDelimiter);
						} else {
							if (position < rowBeforeLast) {
								sb.append(listDelimiter);
								Log.d(App.DEBUG_TAG, "sb.append(listDelimiter) ");
							}
						}
					}
					while (result.moveToNext());
				}
				
				result.close();
				
				medicationTextAnswers.setText(String.format(answerListMedications, sb.toString()));
			}
			
			@Override
			public void error(Exception e) {
				Log.d(App.DEBUG_TAG, "Error while getting list of medications");
			}
		});
	}

}
