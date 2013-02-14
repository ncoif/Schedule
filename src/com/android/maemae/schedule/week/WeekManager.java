package com.android.maemae.schedule.week;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.android.maemae.schedule.Utilities;
import com.android.maemae.schedule.database.ScheduleHelper;
import com.android.maemae.schedule.database.ShiftDay;

public class WeekManager {	
	private static final int WEEK_LENGTH = 7; // how many days in a week ?
	private static final int WEEK_FIRST_DAY = Calendar.MONDAY; // what is the first day of the week ?
	private static final SimpleDateFormat sdfForSQL = new SimpleDateFormat("yyyyMMdd", Locale.US);

	private static WeekManager instance = null;

	private boolean isInitialized;

	private Calendar beginWeek;
	private int firstDay;

	private ScheduleHelper sched;
	private List<ShiftDay> week;

	public static synchronized WeekManager getInstance(Context ctx) {
		if (instance == null)
			instance = new WeekManager(ctx);
		return instance;
	}

	/** Initialize at the current time. */
	private WeekManager(Context context) {
		sched = new ScheduleHelper(context);
		this.isInitialized = false;
	}

	/** Get the first day of the week. */
	public int getFirstDay() {
		return this.firstDay;
	}

	/** Set the beginning of the week at the current time. */
	public void setBegin() {
		// get the current calendar
		this.beginWeek = Calendar.getInstance();
		beginWeek.setFirstDayOfWeek(WEEK_FIRST_DAY);

		//set it to the first day of the week
		beginWeek.set(Calendar.DAY_OF_WEEK, beginWeek.getFirstDayOfWeek());

		initializeIntDate();
		initializeWeek();
		this.isInitialized = true;
	}

	/** Set the beginning of the week using the given date (yyyyMMdd) */
	public void setBegin(int d) {
		if(this.firstDay == d) {
			//date not modified, no need to change anything
		}
		else {
			// get year, month, day from int
			int year = d / 10000 ;
			int month = (d - 10000 * year) / 100 ;
			int day = d - 10000 * year - 100 * month ;

			// set the calendar
			beginWeek.set(year, month - 1, day);
			beginWeek.set(Calendar.DAY_OF_WEEK, beginWeek.getFirstDayOfWeek());

			initializeIntDate();
			initializeWeek();
			this.isInitialized = true;
		}
	}

	/** Get the schedule for the current week. */
	public List<ShiftDay> getWeekSchedule() {
		if( !this.isInitialized ) {
			setBegin();
		}

		return week;
	}

	/** Describe this week. */
	public String toString() {
		if( !this.isInitialized ) {
			setBegin();
		}

		Calendar endWeek = (Calendar) beginWeek.clone();
		endWeek.add(Calendar.DAY_OF_MONTH, WEEK_LENGTH - 1 );
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d", Locale.US);	

		String result = sdf.format(beginWeek.getTime()) + Utilities.dateSuffixes[beginWeek.get(Calendar.DATE)];
		result += " - " + sdf.format(endWeek.getTime()) + Utilities.dateSuffixes[endWeek.get(Calendar.DATE)] + " :";

		return result;
	}

	/** Change for the next week. 
	 * 
	 * @return the firstDayInt
	 */
	public int nextWeek() {
		beginWeek.add(Calendar.DAY_OF_MONTH, WEEK_LENGTH);

		initializeIntDate();
		initializeWeek();

		return firstDay;
	}

	/** Change for the previous week. 
	 * 
	 * @return the firstDayInt
	 */
	public int previousWeek() {
		beginWeek.add(Calendar.DAY_OF_MONTH, - WEEK_LENGTH);

		initializeIntDate();
		initializeWeek();

		return firstDay;
	}

	private void initializeIntDate() {
		this.firstDay = Integer.parseInt( sdfForSQL.format(beginWeek.getTime()) );
	}

	private void initializeWeek() {
		int[] days = new int[WEEK_LENGTH] ;

		Calendar tmp = (Calendar) beginWeek.clone();
		for(int i = 0; i < WEEK_LENGTH; i++) {
			days[i] = Integer.parseInt( sdfForSQL.format(tmp.getTime()) );
			tmp.add(Calendar.DAY_OF_MONTH, 1);
		}

		sched.open();
		this.week = sched.getWeek(days);
		sched.close();
	}

	public void updateDay(ShiftDay day) {
		sched.open();
		sched.insertShiftDay(day);
		sched.close();
	}

}
