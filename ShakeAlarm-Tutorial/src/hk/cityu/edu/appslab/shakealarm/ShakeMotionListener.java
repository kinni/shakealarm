package hk.cityu.edu.appslab.shakealarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeMotionListener implements SensorEventListener{

	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	private static final int SHAKE_THRESHOLD = 2;

	private OnShakeListener mOnShakeListener;

	// Interface to handle when the device has been shaked
	public interface OnShakeListener {
		public void onShake();
	}

	public ShakeMotionListener() {
		// Default values
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
	}

	public void setOnShakeListener(OnShakeListener listener) {
		this.mOnShakeListener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		float x = se.values[0];
		float y = se.values[1];
		float z = se.values[2];
		
		// The formula*
		mAccelLast = mAccelCurrent;
		mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
		float delta = mAccelCurrent - mAccelLast;
		// Perform low-cut filter
		mAccel = mAccel * 0.9f + delta;
		
		if (mAccel > SHAKE_THRESHOLD){
			// Call onShake method
			if (mOnShakeListener != null){
				mOnShakeListener.onShake();
			}
		}
		
	}

}
