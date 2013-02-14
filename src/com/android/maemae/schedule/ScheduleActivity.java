package com.android.maemae.schedule;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.maemae.schedule.database.ShiftDay;
import com.android.maemae.schedule.week.WeekManager;

public class ScheduleActivity extends ListActivity {

	private Button buttonPrevious;
	private Button buttonNext;
	private TextView weekName;

	private List<ShiftDay> week;
	private DayAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_activity);

		//if there is a INT_BEGIN_DISPLAY, then we change the calendar
		Intent intent = getIntent();
		int dateBeginDisplay = intent.getIntExtra("INT_BEGIN_DISPLAY", 0);
		if(dateBeginDisplay == 0) {
			WeekManager.getInstance(getApplicationContext()).setBegin();
		}
		else {
			WeekManager.getInstance(null).setBegin(dateBeginDisplay);
		}

		//display the week_name (i.e. week from when to when)
		weekName = (TextView) findViewById(R.id.week_name);
		weekName.setText(WeekManager.getInstance(null).toString());
		View separator = (View) findViewById(R.id.separator);
		separator.setBackgroundResource(R.color.customBlue);

		// set all the day in the listview
		week = WeekManager.getInstance(null).getWeekSchedule();
		adapter = new DayAdapter(this, week);
		setListAdapter(adapter);
		//registerForContextMenu(getListView());

		buttonPrevious = (Button) findViewById(R.id.button_previous);
		buttonPrevious.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				Intent nextIntent = new Intent(getApplicationContext(),	ScheduleActivity.class);
				nextIntent.putExtra("INT_BEGIN_DISPLAY", WeekManager.getInstance(null).previousWeek() );
				startActivity(nextIntent);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();
			}
		});

		buttonNext = (Button) findViewById(R.id.button_next);
		buttonNext.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				Intent nextIntent = new Intent(getApplicationContext(),	ScheduleActivity.class);
				nextIntent.putExtra("INT_BEGIN_DISPLAY", WeekManager.getInstance(null).nextWeek() );
				startActivity(nextIntent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.schedule_option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.schedule_option_edit:			
			Intent intentEdit = new Intent(ScheduleActivity.this,EditScheduleActivity.class);
			intentEdit.putExtra("INT_BEGIN_DISPLAY", WeekManager.getInstance(null).getFirstDay() );
			startActivity(intentEdit);
			finish();
			return true;

		case R.id.schedule_option_about:			
			Intent intentAbout = new Intent(ScheduleActivity.this, AboutActivity.class);
			startActivity(intentAbout);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}