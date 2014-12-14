package com.pkjm.thaw.analyser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.pkjm.thaw.camera2.Camera2BasicFragment;

/**
 * Created by PakawiNz on 11/12/2557.
 */
public class Analyser implements Runnable {

    private final Handler handler = new Handler();
    private SensorInterface sensorInterface;
    private ColorAnalyser colorAnalyser;
    private TextView debugText;
    private Button showColorBtn;
    private boolean check_debug;

    private byte[] udpout = new byte[10];
    private UDPClient client;

    public Analyser(Activity activity,Camera2BasicFragment fragment,
        TextView debugText,Button showColorBtn,
        boolean check_debug,String ipv4,int port) {

        this.sensorInterface = new SensorInterface(activity);
        this.colorAnalyser = new ColorAnalyser(fragment);
        this.debugText = debugText;
        this.showColorBtn = showColorBtn;

        this.check_debug = check_debug;
        this.client = new UDPClient(port);
        this.client.add(ipv4);
    }

    private static int byte2uint(byte b) {
        return b & 0xFF;
    }

    private boolean isBlock = false;
    public boolean swapBlState(){
        isBlock = !isBlock;
        return isBlock;
    }

    private String outString;
    public void run() {
        long timeStamp = System.currentTimeMillis();

        outString = "";

        int color = colorAnalyser.getCalcColor(udpout);
        int angle = sensorInterface.getDeviceAngle(udpout);
        udpout[9] = (byte)(isBlock ? 1 : 0);

        for (Drawable drawable : showColorBtn.getCompoundDrawables()){
            if (drawable == null) continue;

            if (check_debug)
                drawable.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN );
            else
                drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN );
        }
        if (check_debug)
            showColorBtn.setBackgroundColor(color);

        outString += String.format("[%03d,%03d,%03d],",
                byte2uint(udpout[0]),byte2uint(udpout[1]),byte2uint(udpout[2]));
        outString += String.format("[%02d%02d],",
                byte2uint(udpout[3]),byte2uint(udpout[4]));
        outString += "" + isBlock + ",";

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
