package com.android.maemae.schedule.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * To get access to the DB (select, insert, drop ...).
 *
 */
public class ScheduleHelper {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "schedule.db";

	private static final String TABLE_SCHEDULE = "schedule";
	private static final String COL_DATE = "date";
	private static final int COL_DATE_NUM = 0;
	private static final String COL_AM = "am";
	private static final int COL_AM_NUM = 1;
	private static final String COL_PM = "pm";
	private static final int COL_PM_NUM = 2;
	private static final String COL_NYT = "nyt";
	private static final int COL_NYT_NUM = 3;

	private SQLiteDatabase db;
	private ScheduleSQLite dbInstance;

	public ScheduleHelper(Context context) {
		dbInstance = new ScheduleSQLite(context, DB_NAME, null, DB_VERSION);
	}

	public void open() {
		db = dbInstance.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDB() {
		return this.db;
	}

	public long insertShiftDay(ShiftDay day){
		ContentValues values = new ContentValues();
		values.put(COL_DATE, day.getDate());
		values.put(COL_AM, day.getAM());
		values.put(COL_PM, day.getPM());
		values.put(COL_NYT, day.getNYT());

		try {
			return db.insertOrThrow(TABLE_SCHEDULE, null, values);
		} catch (SQLiteException e) {
			// already exist in the db, so update instead
			int date = day.getDate();
			return db.update(TABLE_SCHEDULE, values, COL_DATE + " = " + date, null);
		}
	}

	public int updateShiftDay(int date, ShiftDay day) {
		ContentValues values = new ContentValues();
		values.put(COL_DATE, day.getDate());
		values.put(COL_AM, day.getAM());
		values.put(COL_PM, day.getPM());
		values.put(COL_NYT, day.getNYT());

		return db.update(TABLE_SCHEDULE, values, COL_DATE + " = " + date, null);
	}

	public int removeShiftDay(int date) {
		return db.delete(TABLE_SCHEDULE, COL_DATE + " = " + date, null);
	}

	public ShiftDay getShiftDay(int date) {
		Cursor c = db.query(TABLE_SCHEDULE, new String[] {COL_DATE, COL_AM, COL_PM, COL_NYT}, COL_DATE + " = " + date +"", null, null, null, null);
		return cursorToShiftDay(c);
	}

	/**
	 * Convert an Android cursor in ShiftDay
	 * @param c
	 * @return day
	 */
	private ShiftDay cursorToShiftDay(Cursor c) {
		//if no results
		if (c.getCount() == 0)
			return null;

		//else we parse and return the first element
		c.moveToFirst();
		ShiftDay day = new ShiftDay();
		day.setDate(c.getInt(COL_DATE_NUM));
		day.setAM(c.getInt(COL_AM_NUM));
		day.setPM(c.getInt(COL_PM_NUM));
		day.setNYT(c.getInt(COL_NYT_NUM));
		c.close();

		return day;
	}

	/** get a list of day for the given week. Add days off in the list. */
	public List<ShiftDay> getWeek(int[] days) {
		int dateBegin = days[0];
		int dateEnd = days[days.length-1];

		String whereCondition = COL_DATE + " BETWEEN " + dateBegin + " AND " + dateEnd + " ";
		Cursor c = db.query(TABLE_SCHEDULE, new String[] {COL_DATE, COL_AM, COL_PM, COL_NYT}, whereCondition, null, null, null, null);

		//initialize the list
		List<ShiftDay> week = new ArrayList<ShiftDay>();

		c.moveToFirst();
		boolean doNothing = false; // to prevent NullPointerException if c is null
		if(c.getCount() == 0)
			doNothing = true;

		//populate the list
		for(int i = 0; i < days.length; i++) {

			//if there is a matching day, we add it
			if( (!doNothing) && (!c.isAfterLast()) && (c.getInt(COL_DATE_NUM) == days[i]) ) {
				ShiftDay day = new ShiftDay();
				day.setDate(c.getInt(COL_DATE_NUM));
				day.setAM(c.getInt(COL_AM_NUM));
				day.setPM(c.getInt(COL_PM_NUM));
				day.setNYT(c.getInt(COL_NYT_NUM));				
				week.add(day);
				c.moveToNext();
			}
			//else we add a day off
			else {
				ShiftDay dayOff = new ShiftDay( days[i], 0, 0, 0);
				week.add(dayOff);	
			}

		}

		if(!doNothing)
			c.close();

		return week;
	}

	/** get all the db. */
	public List<ShiftDay> getAll() {

		Cursor c = db.query(TABLE_SCHEDULE, new String[] {COL_DATE, COL_AM, COL_PM, COL_NYT}, null, null, null, null, null);

		//initialize the list
		List<ShiftDay> allDays = new ArrayList<ShiftDay>();

		c.moveToFirst();
		boolean doNothing = false; // to prevent NullPointerException if c is null
		if(c.getCount() == 0)
			doNothing = true;

		//populate the list
		while( !c.isAfterLast() ) {

			if( (!doNothing) ) {
				ShiftDay day = new ShiftDay();
				day.setDate(c.getInt(COL_DATE_NUM));
				day.setAM(c.getInt(COL_AM_NUM));
				day.setPM(c.getInt(COL_PM_NUM));
				day.setNYT(c.getInt(COL_NYT_NUM));				
				allDays.add(day);
				c.moveToNext();
			}

		}

		if(!doNothing)
			c.close();

		return allDays;
	}

	/** Drop the db, and restore entirely the schedule. */
	public void restoreShiftDay(List<ShiftDay> days) {
		//drop the db
		db.delete(TABLE_SCHEDULE, null, null);

		//insert all the new values
		for(ShiftDay day : days) {
			ContentValues values = new ContentValues();
			values.put(COL_DATE, day.getDate());
			values.put(COL_AM, day.getAM());
			values.put(COL_PM, day.getPM());
			values.put(COL_NYT, day.getNYT());

			try {
				db.insertOrThrow(TABLE_SCHEDULE, null, values);
			} catch (SQLiteException e) {
				// already exist in the db, so update instead
				int date = day.getDate();
				db.update(TABLE_SCHEDULE, values, COL_DATE + " = " + date, null);
			}

		}
	}
}
