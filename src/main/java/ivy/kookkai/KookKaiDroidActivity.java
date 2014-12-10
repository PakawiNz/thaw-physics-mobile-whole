package ivy.kookkai;

import ivy.kookkai.debugview.CameraInterface;
import ivy.kookkai.debugview.DebugImgView;
import ivy.kookkai.vision.SensorInterface;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KookKaiDroidActivity extends Activity {
    /** Called when the activity is first created. */

    MainlLoop main;

    DebugImgView debugImgview;
    CameraInterface cameraInterface;
    SensorInterface sensorInterface;

    TextView debugText, headingText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.d("ivy_debug", "start!!!");

        sensorInterface = new SensorInterface(this);

        createGUI();

        headingText = (TextView) findViewById(R.id.headingText);
        debugText = (TextView) findViewById(R.id.debugText);

        main = new MainlLoop(cameraInterface, sensorInterface, debugImgview, debugText, new CheckBox(this));
	}

	private void createGUI() {
		LinearLayout rootHorizontalLayout = (LinearLayout) findViewById(R.id.upper_view);

        cameraInterface = new CameraInterface(this);
		cameraInterface.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		debugImgview = new DebugImgView(this);
		debugImgview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		FrameLayout cameraFrame = new FrameLayout(this);
		cameraFrame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		cameraFrame.addView(cameraInterface);
		cameraFrame.addView(debugImgview);

        rootHorizontalLayout.addView(cameraFrame);
	}

	@Override
	public void onStart() {
		super.onStart();
		main.start();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		main.stop();
	}

}