package com.android.maemae.schedule.database;

/**
 * Create a day, as it will be store in the DB.
 */
public class ShiftDay {
	private int mDate; //YYYMMDD
	private int mAM;
	private int mPM;
	private int mNYT;

	public ShiftDay() {}

	public ShiftDay(int date, int am, int pm, int nyt) {
		this.mDate = date;
		this.mAM = am;
		this.mPM = pm;
		this.mNYT = nyt;
	}

	public int getDate() {
		return this.mDate;
	}
	public void setDate(int date) {
		this.mDate = date;
	}

	public int getAM() {
		return this.mAM;
	}
	public void setAM(int am) {
		this.mAM = am;
	}

	public int getPM() {
		return this.mPM;
	}
	public void setPM(int pm) {
		this.mPM = pm;
	}

	public int getNYT() {
		return this.mNYT;
	}
	public void setNYT(int nyt) {
		this.mNYT = nyt;
	}

}
