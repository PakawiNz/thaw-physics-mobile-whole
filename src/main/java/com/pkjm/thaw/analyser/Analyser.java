package com.pkjm.thaw.analyser;

import android.app.Activity;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.pkjm.thaw.camera2.Camera2BasicFragment;

/**
 * Created by PakawiNz on 11/12/2557.
 */
public class Analyser implements Runnable {

    private final Handler handler = new Handler();
    SensorInterface sensorInterface;
    ColorAnalyser colorAnalyser;
    String outString;
    TextView debugText;
    Button showColorBtn;

    private byte[] udpout = new byte[10];
    UDPClient client;

    public Analyser(Activity activity,Camera2BasicFragment fragment,TextView debugText,Button showColorBtn) {
        this.sensorInterface = new SensorInterface(activity);
        this.colorAnalyser = new ColorAnalyser(fragment);
        this.debugText = debugText;
        this.showColorBtn = showColorBtn;
        this.client = new UDPClient(6437);
        client.add("192.168.1.33");
    }

    public static int byte2uint(byte b) {
        return b & 0xFF;
    }

    public void run() {
        long timeStamp = System.currentTimeMillis();

        outString = "";

        int color = colorAnalyser.getCalcColor(udpout);
        int angle = sensorInterface.getDeviceAngle(udpout);

        showColorBtn.setBackgroundColor(color);

        outString += String.format("[%03d,%03d,%03d],",
                byte2uint(udpout[0]),byte2uint(udpout[1]),byte2uint(udpout[2]));
        outString += String.format("[%02d%02d],",
                byte2uint(udpout[3]),byte2uint(udpout[4]));

        debugText.setText(outString);
        client.sendMessage(udpout);

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
