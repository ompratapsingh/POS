package com.mak.pos.Utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Build.VERSION_CODES;

import com.mak.App;

@SuppressLint("NewApi")
public class PreferencesManager {

	private static String PREF_NAME = "com.example.app.PREF_NAME";
	public static final String PREF_MAIN_LOGIN = "main login";
	private static final String KEY_NOTIFICATIONS = "notifications";

	private static PreferencesManager sInstance;
	private final SharedPreferences mPref;
	Location location;
	double latitude; // latitude
	double longitude; // longitude
	public String premiumdate;

	private PreferencesManager(Context context) {
		PREF_NAME = App.getContext().getPackageName() + ".PREFNAME";
	  	mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

	}

	public static synchronized void initializeInstance(Context context) {
		if (sInstance == null) {
			sInstance = new PreferencesManager(context);
		}
	}

	public static synchronized PreferencesManager getInstance() {
		if (sInstance == null) {
			throw new IllegalStateException(
					PreferencesManager.class.getSimpleName()
							+ " is not initialized, call initializeInstance(..) method first.");
		}
		return sInstance;
	}

	public void addNotification(String notification) {

		// get old notifications
		String oldNotifications = getNotifications();

		if (oldNotifications != null) {
			oldNotifications += "|" + notification;
		} else {
			oldNotifications = notification;
		}

		mPref.edit().putString(KEY_NOTIFICATIONS, oldNotifications);
		mPref.edit().commit();
	}

	public String getNotifications() {
		return mPref.getString(KEY_NOTIFICATIONS, null);
	}


	public void setInt(String KEY, int Values) {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO)
			mPref.edit().putInt(KEY, Values).commit();
		else
			mPref.edit().putInt(KEY, Values).apply();
	}

	public int getInt(String KEY) {
		return mPref.getInt(KEY, 0);
	}

	public int getInt(String KEY, int defaultValue)
	{
		return mPref.getInt(KEY,defaultValue);
	}

	public void setFloat(String KEY, float Values) {

		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO) {
			mPref.edit().putFloat(KEY, Values).commit();
		} else {
			mPref.edit().putFloat(KEY, Values).apply();
		}
	}

	public float getFloat(String KEY) {
		return mPref.getFloat(KEY, 0.0f);

	}

	public void setString(String KEY, String Values) {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO)
			mPref.edit().putString(KEY, Values).commit();
		else
			mPref.edit().putString(KEY, Values).apply();
	}

	public void setDouble(String KEY, double values) {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO)
			mPref.edit().putLong(KEY, Double.doubleToRawLongBits(values))
					.commit();
		else
			mPref.edit().putLong(KEY, Double.doubleToRawLongBits(values))
					.apply();

	}

	public void setBoolean(String KEY, boolean Values) {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO)
			mPref.edit().putBoolean(KEY, Values).commit();
		else
			mPref.edit().putBoolean(KEY, Values).apply();
	}

	public boolean getBoolean(String KEY) {

		return mPref.getBoolean(KEY, false);
	}

	public boolean getBoolean(String KEY, boolean defaultValue) {

		return mPref.getBoolean(KEY, defaultValue);
	}

	public String getValue(String KEY) {
		return mPref.getString(KEY, null);
	}

	public double getDouble(String KEY, double Values) {

		return Double.longBitsToDouble(mPref.getLong(KEY,
				Double.doubleToLongBits(Values)));

	}



	public void remove(String key) {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO) {
			mPref.edit().remove(key).commit();
		} else {
			mPref.edit().remove(key).apply();
		}
	}

	public boolean clear() {
		if (Build.VERSION.SDK_INT <= VERSION_CODES.FROYO) {
			return mPref.edit().clear().commit();
		} else {
			mPref.edit().clear().apply();
			return true;
		}
	}
	public  double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	public  double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();

		}
		return longitude;
	}

	public String getPremiumdate() {
		return premiumdate;
	}

	public void setPremiumdate(String premiumdate) {
		this.premiumdate = premiumdate;
	}
}