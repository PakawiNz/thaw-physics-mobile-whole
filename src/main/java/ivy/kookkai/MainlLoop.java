package ivy.kookkai;

import ivy.kookkai.data.GlobalVar;
import ivy.kookkai.debugview.CameraInterface;
import ivy.kookkai.debugview.DebugImgView;
import ivy.kookkai.vision.SensorInterface;
import android.graphics.Color;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.TextView;


public class MainlLoop implements Runnable {

	private final Handler handler = new Handler();
	private CameraInterface camInterface;
	private SensorInterface sensorInterface;
	private DebugImgView debugImg;
	private boolean running;

	private TextView debugText;
	private String outString;

	private CheckBox drawColorCheck;

	public MainlLoop(CameraInterface cam,SensorInterface sens, DebugImgView dimg, TextView dtext, CheckBox drawColor) {
		camInterface = cam;
		sensorInterface = sens;
		debugImg = dimg;
		debugText = dtext;
		drawColorCheck = drawColor;

		GlobalVar.initVar();
		GlobalVar.frameHeight = camInterface.frameWidth / 2;
		GlobalVar.frameWidth = camInterface.frameHeight / 2;
	}

	public void start() {
		handler.removeCallbacks(this);
		handler.postDelayed(this, 100);
		running = true;
		Network.createThread();
	}

	public void stop() {
		handler.removeCallbacks(this);
		running = false;
		Network.destroyThread();
	}

	public void run() {
		long timeStamp = System.currentTimeMillis();

		outString = "ROBOT [MK" + GlobalVar.KOOKKAI_MARK + "]" + "\n";

        byte[] testColoring = debugImg.initColorData();
        for (int i = 1; i < testColoring.length; i++ ){
            testColoring[i] = (byte)((i/20)%4 + 1);
        }

        if (true){
            debugImg.drawImage(testColoring);
            debugImg.invalidate();
        }

		// NOTE: execute Control
		debugText.setText(outString);

		long timeDiff = System.currentTimeMillis() - timeStamp;
	
		if (running) {
			if (timeDiff < 90)
				handler.postDelayed(this, 100 - timeDiff);
			else
				handler.postDelayed(this, 10);
		}
	}

}
