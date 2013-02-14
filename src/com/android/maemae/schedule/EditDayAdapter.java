package com.android.maemae.schedule;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.maemae.schedule.database.ShiftDay;
import com.android.maemae.schedule.week.WeekManager;

public class EditDayAdapter extends BaseAdapter {

	private List<ShiftDay> week;
	private final Activity context;

	public EditDayAdapter(Activity activity, List<ShiftDay> w) {
		this.context = activity;
		this.week = w;
	}

	public int getCount() {
		return this.week.size();
	}

	public Object getItem(int position) {
		return this.week.get(position);
	}

	public long getItemId(int position) {
		return this.week.get(position).getDate();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout rowLayout = null;
		if (convertView == null) {
			rowLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.week_item, parent, false);
		} else {
			rowLayout = (RelativeLayout) convertView;
		}

		ShiftDay day = week.get(position);

		// fill the TextView of the week_item layout  
		TextView date = (TextView) rowLayout.findViewById(R.id.day_date);
		date.setText(Utilities.describeDate(day.getDate()));

		Button am = (Button) rowLayout.findViewById(R.id.day_am);
		am.setTag(R.id.idType, R.drawable.btn_am_draw);
		am.setTag(R.id.idDay,day);
		am.setOnClickListener(buttonClickListener);
		if(day.getAM() == 1) {
			am.setBackgroundResource(R.drawable.btn_am_draw);
		} else {
			am.setBackgroundResource(R.drawable.btn_off_draw);
		}

		Button pm = (Button) rowLayout.findViewById(R.id.day_pm);
		pm.setTag(R.id.idType, R.drawable.btn_pm_draw);
		pm.setTag(R.id.idDay,day);
		pm.setOnClickListener(buttonClickListener);
		if(day.getPM() == 1) {
			pm.setBackgroundResource(R.drawable.btn_pm_draw);
		} else {
			pm.setBackgroundResource(R.drawable.btn_off_draw);
		}

		Button nyt = (Button) rowLayout.findViewById(R.id.day_nyt);
		nyt.setTag(R.id.idType, R.drawable.btn_nyt_draw);
		nyt.setTag(R.id.idDay,day);
		nyt.setOnClickListener(buttonClickListener);
		if(day.getNYT() == 1) {
			nyt.setBackgroundResource(R.drawable.btn_nyt_draw);
		} else {
			nyt.setBackgroundResource(R.drawable.btn_off_draw);
		}

		return rowLayout;
	}

	private OnClickListener buttonClickListener = new OnClickListener() {

		public void onClick(View v) {
			int type = (Integer) v.getTag(R.id.idType); //The id of the drawable of the button
			ShiftDay day = (ShiftDay) v.getTag(R.id.idDay);
			int value = 0;

			//AM
			if(type == R.drawable.btn_am_draw) {
				day.setAM( (day.getAM() + 1) % 2 ); //quicker way to change 0 to 1
				value = day.getAM();
			}
			//PM
			if(type == R.drawable.btn_pm_draw) {
				day.setPM( (day.getPM() + 1) % 2 );
				value = day.getPM();
			}
			//NYT
			if(type == R.drawable.btn_nyt_draw) {
				day.setNYT( (day.getNYT() + 1) % 2 );
				value = day.getNYT();
			}

			WeekManager.getInstance(null).updateDay(day);
			//draw it manually to improve performance, instead of using notifyDataSetChanged();
			//notifyDataSetChanged();
			if( value == 1) {
				v.setBackgroundResource(type);
			} else {
				v.setBackgroundResource(R.drawable.btn_off_draw);
			}

			//String text = "Shift modified.";
			//Utilities.toast(context, text);
		}
	};
}
