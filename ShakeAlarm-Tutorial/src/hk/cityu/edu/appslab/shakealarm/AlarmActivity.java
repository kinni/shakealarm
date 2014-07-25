package hk.cityu.edu.appslab.shakealarm;

import hk.cityu.edu.appslab.shakealarm.ShakeMotionListener.OnShakeListener;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ProgressBar;

public class AlarmActivity extends Activity {

	// UI elements
	private ProgressBar timeBar;

	// 1. System Classes
	
	// 1.1 to play alarm sound
	private MediaPlayer player;
	
	// 1.2 to vibrate the device
	private Vibrator vibrator;
	
	// 1.3 to manage sensor
	private SensorManager sensorManager;
	
	// 1.4 the listener for shake motion
	private ShakeMotionListener shakeMotionListener;

	// Variables
	private boolean alarmStopped;
	private int time;

	// Constants
	final static int MAX_TIME = 100;
	final static int SLEEP = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		// init
		alarmStopped = false;
		initTimeBar();
		initVibrator();
		initAlarmSound();
		initSensor();

	}

	private void initTimeBar() {
		timeBar = (ProgressBar) findViewById(R.id.time_bar);
		time = MAX_TIME;
		timeBar.setProgress(time);
		
		// Auto increase Time Bar value
		// Update UI
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!alarmStopped) {

					try {
						// Delay
						Thread.sleep(SLEEP);
						// Update the time bar
						time = time + 1;
						timeBar.setProgress(time);

						if (time > MAX_TIME) {
							time = MAX_TIME;
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		}).start();

	}

	private void initVibrator() {
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = {0,200,100};
		vibrator.vibrate(pattern, 0);
	}

	private void initAlarmSound() {
		player = MediaPlayer.create(this, R.raw.alarm_clock);
		player.setLooping(true);
		player.start();
	}

	private void initSensor() {
		shakeMotionListener = new ShakeMotionListener();
		shakeMotionListener.setOnShakeListener(new OnShakeListener(){

			@Override
			public void onShake() {
				// update time value
				time = time - 10;
				
				// check if stop condition has been reached
				if (time < 0){
					stopAlarm();
					alarmStopped = true;
				}
			}
			
		});
		
		// SensorManager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(shakeMotionListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	private void stopAlarm() {
		vibrator.cancel();
		player.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister Listener
		sensorManager.unregisterListener(shakeMotionListener);
		stopAlarm();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
