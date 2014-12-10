package api_l.analyser;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInterface implements SensorEventListener {

	private SensorManager sensorManager;
    private double deviceAngle;

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

	private double[] acc = new double[3];

	private void acceleroMeter_Handler(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final double alpha = 0.8;
            // Isolate the force of gravity with the low-pass filter.
            acc[0] = alpha * acc[0] + (1 - alpha) * event.values[0];
            acc[1] = alpha * acc[1] + (1 - alpha) * event.values[1];
            acc[2] = alpha * acc[2] + (1 - alpha) * event.values[2];

            double easyAngleRad = Math.atan2(acc[1], acc[0]);
            final double beta = 0.8;
            deviceAngle = beta * deviceAngle + (1 - beta) * easyAngleRad;
        }
	}

    public String getAccelero(){
        return "x:" + acc[0] + " y:" + acc[1] + " z:" +acc[2];
    }

    public String getDeviceAngle() {
        return "" + String.format("%3.1f",Math.toDegrees(deviceAngle) + 180);
    }
}
