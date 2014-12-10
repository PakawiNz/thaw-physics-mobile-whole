package com.pkjm.thaw.analyser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.pkjm.thaw.Camera2BasicFragment;

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

    private final int spanSize = 40;
    private final int stepSize = 1;
    private float[] hsv = new float[3];
    private String getCalcColor(){
        int color = -0;
        double h=0,s=0,v=0,adj=1;

        try {
            Bitmap bitmap = fragment.getmTextureView().getBitmap();
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            double count = 0;
//            double maxrange = Math.sqrt(Math.pow(spanSize/2,2) + Math.pow(spanSize/2,2));

            for (int i = cx-spanSize; i < cx+spanSize; i += stepSize){
                for (int j = cy-spanSize; j < cy+spanSize; j += stepSize){
                    Color.colorToHSV(bitmap.getPixel(cx,cy),hsv);
//                    adj = maxrange - Math.sqrt(Math.pow(i-cx,2) + Math.pow(j-cy,2));
//                    adj /= maxrange;
                    h += hsv[0]*adj;
                    s += hsv[1]*adj;
                    v += hsv[2]*adj;
                    count += adj;
                }
            }

            hsv[0] = (float)(h / count);
            hsv[1] = (float)(s / count);
            hsv[2] = (float)(v / count);


            color = Color.HSVToColor(hsv);
            showColorBtn.setBackgroundColor(color);
        }catch (Exception e){
            Log.d("thaw-calc-color", "can't get bitmap");
        }finally {
            return String.format("%010d",color) + ",";
        }
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
