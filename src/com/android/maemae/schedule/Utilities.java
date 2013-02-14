package com.android.maemae.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.widget.Toast;

import com.android.maemae.schedule.database.ShiftDay;

public class Utilities {

	static public String[] dateSuffixes =
			//    0     1     2     3     4     5     6     7     8     9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//    10    11    12    13    14    15    16    17    18    19
		"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
		//    20    21    22    23    24    25    26    27    28    29
		"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
		//    30    31
		"th", "st" };


	static void toast(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * Describe the date in a more convenient form than just an int.
	 * 
	 * @param d
	 * @return
	 */
	static String describeDate(int d) {
		Calendar cal = Calendar.getInstance();

		//parse the date 
		int year = d / 10000 ;
		int month = (d - 10000 * year) / 100 ;
		int day = d - 10000 * year - 100 * month ;
		cal.set(year, month - 1, day);

		//display the date
		SimpleDateFormat sdf = new SimpleDateFormat("EEE. d", Locale.US); //TODO optimize that to reduce memory usage

		return sdf.format(cal.getTime()) + dateSuffixes[day] + " :";
	}

	/**
	 * Describe the date in a more convenient form than just an int.
	 * 
	 * @param d
	 * @return
	 */
	static String describeShiftDay(ShiftDay sDay) {
		Calendar cal = Calendar.getInstance();

		//parse the date
		int d = sDay.getDate();
		int year = d / 10000 ;
		int month = (d - 10000 * year) / 100 ;
		int day = d - 10000 * year - 100 * month ;
		cal.set(year, month - 1, day);

		//display the date
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE d", Locale.US); //TODO optimize that to reduce memory usage

		String text = sdf.format(cal.getTime()) + dateSuffixes[day] + " : ";

		// display the shifts
		boolean isOff = true;
		if(sDay.getAM() == 1) {
			text += "AM";
			isOff = false;
		}
		if (sDay.getPM() == 1) {
			if(isOff) {
				text += "PM";
				isOff = false;
			} else {
				text += ", PM";
			}
		}
		if (sDay.getNYT() == 1) {
			if(isOff) {
				text += "NYT";
				isOff = false;
			} else {
				text += ", NYT";
			}
		} 

		if(isOff) {
			text += " day off";
		}

		return text;
	}
}
