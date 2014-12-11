package com.pkjm.thaw.analyser;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.pkjm.thaw.camera2.Camera2BasicFragment;

public class ColorAnalyser {
    private Camera2BasicFragment fragment;

    public ColorAnalyser(Camera2BasicFragment fragment) {
        this.fragment = fragment;
    }

    private final int spanSize = 300;
    private final int stepSize = 30;
    private float[] hsv = new float[3];

    public int getCalcColor(byte[] udpout){
        int color = 0;

        if(!fragment.getTextureView().isAvailable()) return color;
        try {
            Bitmap bitmap = fragment.getTextureView().getBitmap();
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            double count = 0;
            double h=0,s=0,v=0,adj=1;

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

            int r = (color & 0x00FF0000) >> 16;
            int g = (color & 0x0000FF00) >> 8;
            int b = (color & 0x000000FF) >> 0;

            udpout[0] = (byte) r;
            udpout[1] = (byte) g;
            udpout[2] = (byte) b;

        }catch (Exception e){
            Log.d("thaw-calc-color", "can't get bitmap");
        }finally {
            return color;
        }
    }


}
