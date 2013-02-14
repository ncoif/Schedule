package com.android.maemae.schedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create the SQLite DB.
 */
public class ScheduleSQLite extends SQLiteOpenHelper {

	private static final String TABLE_SCHEDULE = "schedule";
	private static final String COL_DATE = "date"; //primary key
	private static final String COL_AM = "am";
	private static final String COL_PM = "pm";
	private static final String COL_NYT = "nyt";

	private static final String CREATE_DB = "CREATE TABLE " + TABLE_SCHEDULE + " ("
			+ COL_DATE + " INTEGER PRIMARY KEY, "
			+ COL_AM + " TINYINT, " + COL_PM + " TINYINT, "+ COL_NYT + " TINYINT);";

	public ScheduleSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//FIXME
	}
}
