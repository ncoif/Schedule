package com.android.maemae.schedule;

import java.util.List;

import android.app.Activity;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.maemae.schedule.database.ShiftDay;

public class DayAdapter extends BaseAdapter {

	private List<ShiftDay> week;
	private final Activity context;

	public DayAdapter(Activity activity, List<ShiftDay> w) {
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
		am.setTag(R.id.idDay,day);
		am.setOnTouchListener(buttonTouchListener);
		am.setOnClickListener(buttonClickListener);
		if(day.getAM() == 1) {
			am.setBackgroundResource(R.drawable.btn_am_draw);
		} else {
			am.setBackgroundResource(R.drawable.btn_off_draw);
		}

		Button pm = (Button) rowLayout.findViewById(R.id.day_pm);
		pm.setTag(R.id.idDay,day);
		pm.setOnTouchListener(buttonTouchListener);
		pm.setOnClickListener(buttonClickListener);
		if(day.getPM() == 1) {
			pm.setBackgroundResource(R.drawable.btn_pm_draw);
		} else {
			pm.setBackgroundResource(R.drawable.btn_off_draw);
		}

		Button nyt = (Button) rowLayout.findViewById(R.id.day_nyt);
		nyt.setTag(R.id.idDay,day);
		nyt.setOnTouchListener(buttonTouchListener);
		nyt.setOnClickListener(buttonClickListener);
		if(day.getNYT() == 1) {
			nyt.setBackgroundResource(R.drawable.btn_nyt_draw);
		} else {
			nyt.setBackgroundResource(R.drawable.btn_off_draw);
		}

		return rowLayout;
	}

	private OnTouchListener buttonTouchListener = new OnTouchListener() {

		public final float[] colorMatrixButtonSelected = new float[] { 2, 0, 0, 0, 2, 0,
				2, 0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0 };

		public final float[] colorMatrixButtonNotSelected = new float[] { 1, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(colorMatrixButtonSelected));
				v.setBackgroundDrawable(v.getBackground());
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(colorMatrixButtonNotSelected));
				v.setBackgroundDrawable(v.getBackground());
			}
			return false;
		}
	};

	private OnClickListener buttonClickListener = new OnClickListener() {

		public void onClick(View v) {
			ShiftDay day = (ShiftDay) v.getTag(R.id.idDay);
			String text = Utilities.describeShiftDay(day);
			Utilities.toast(context, text);
		}
	};

}
