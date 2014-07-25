package hk.cityu.edu.appslab.shakealarm;

import java.util.Calendar;
import java.util.Date;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity implements
		OnTimeSetListener {

	// UI elements
	private CheckBoxPreference alarmEnablePref;
	private Preference alarmTimePref;

	// System Classes
	private Calendar c;
	private Alarm alarm;
	private TimePickerDialog timePickerDialog;

	// Variables
	private int mHour, mMinute;
	private boolean alarmEnabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		alarmEnabled = false;
		alarm = new Alarm();

		alarmEnablePref = (CheckBoxPreference) findPreference(getString(R.string.key_alarm_status));
		alarmEnablePref.setChecked(false);
		alarmEnablePref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean enabled = (Boolean) newValue;
						if (enabled) {
							setAlarm();
						} else {
							cancelAlarm();
						}

						return true;
					}

				});

		alarmTimePref = findPreference(getString(R.string.key_time));
		alarmTimePref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						showTimePickerDialog();
						return true;
					}

				});

	}

	// Display the Time Picker Dialog
	private void showTimePickerDialog() {
		c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		timePickerDialog = new TimePickerDialog(SettingActivity.this, this,
				mHour, mMinute, false);
		timePickerDialog.show();

	}

	// Set the alarm
	private void setAlarm() {
		alarmEnabled = true;
		if (c == null) {
			c = Calendar.getInstance();
			c.add(Calendar.SECOND, 5);
		}
		alarm.set(SettingActivity.this, c.getTimeInMillis());
	}

	// Cancel the alarm
	private void cancelAlarm() {
		alarm.cancel(SettingActivity.this);
		alarmEnabled = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		c = Calendar.getInstance();
		boolean isNextDay = false;
		if (hourOfDay < mHour) {
			isNextDay = true;
		} else if (hourOfDay == mHour && minute <= mMinute) {
			isNextDay = true;
		}

		if (isNextDay)
			c.add(Calendar.DAY_OF_YEAR, 1);

		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);

		if (alarmEnabled) {
			// Cancel any pending alarm
			alarm.cancel(SettingActivity.this);

			// Set a new alarm
			alarm.set(SettingActivity.this, c.getTimeInMillis());

		}

		// Update preference summary
		alarmTimePref.setSummary(c.getTime().toLocaleString());

	}

}
