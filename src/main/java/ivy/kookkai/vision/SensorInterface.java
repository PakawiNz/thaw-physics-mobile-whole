package ivy.kookkai.vision;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInterface implements SensorEventListener {

	private SensorManager sensorManager;

    public SensorInterface(Activity activity) {

		sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			acceleroMeter_Handler(event);
		}
	}

	/**-------------------------------------> ACCELEROMETER <-------------------------------------*/

	private float[] acc = new float[3];

	private void acceleroMeter_Handler(SensorEvent event) {
		acc[0] = event.values[0];
		acc[1] = event.values[1];
		acc[2] = event.values[2];
	}
}
