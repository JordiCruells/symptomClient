package org.jcruells.sm.client.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CheckInDao {
	
	private SQLiteDatabase db = null;
	
	
	public CheckInDao(SQLiteDatabase db) {
		this.db = db;
	}
	
	// Returns all check-ins records in the database
	public Cursor readCheckIns() {
		return db.query(PatientDatabaseOpenHelper.CHECKINS_TABLE,
			   //PatientDatabaseOpenHelper.columns, null, new String[] {}, null, null,null);
			   PatientDatabaseOpenHelper.all_columns, null, new String[] {}, null, null,null);
	}

}
