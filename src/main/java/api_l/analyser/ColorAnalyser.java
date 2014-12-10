package api_l.analyser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import api_l.Camera2BasicFragment;

public class ColorAnalyser implements Runnable {
    private final Handler handler = new Handler();
    SensorInterface sensorInterface;
    String outString;
    TextView debugText;
    Button showColorBtn;
    Camera2BasicFragment fragment;

    UDPClient client;

    public ColorAnalyser(Activity activity,TextView debugText,Button showColorBtn, Camera2BasicFragment fragment) {
        this.sensorInterface = new SensorInterface(activity);
        this.debugText = debugText;
        this.showColorBtn = showColorBtn;
        this.fragment = fragment;
        this.client = new UDPClient(5012);
        client.add("192.168.1.38");
    }


    private String getCalcColor(){
        if (fragment.getmTextureView() != null) {
            int color;
            Bitmap bitmap = fragment.getmTextureView().getBitmap();
            color =  bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            showColorBtn.setBackgroundColor(color);
            return String.format("%010d",color) + ",";
        }
        return "No Texture Yet,";
    }

    public void run() {
        long timeStamp = System.currentTimeMillis();

        outString = "";

        outString += getCalcColor();
        outString += sensorInterface.getDeviceAngle() + ",";

        debugText.setText(outString);
        client.sendMessage(outString);

        long timeDiff = System.currentTimeMillis() - timeStamp;

        if (timeDiff < 90)
            handler.postDelayed(this, 100 - timeDiff);
        else
            handler.postDelayed(this, 10);
    }

    public void start() {
        handler.removeCallbacks(this);
        handler.postDelayed(this, 100);
    }

    public void stop() {
        handler.removeCallbacks(this);
    }
}
