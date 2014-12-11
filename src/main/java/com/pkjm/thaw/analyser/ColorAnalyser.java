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
    private byte[] rgb = new byte[3];

    public int getCalcColor(byte[] udpout){
        int color = 0;

        if(!fragment.getTextureView().isAvailable()) return color;
        try {
            Bitmap bitmap = fragment.getTextureView().getBitmap();
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            double count = 0;
            double sumR=0,sumG=0,sumB=0,adj=1;

            int whitePixelCount = 0;
            int sumWhiteX = 0;
            int sumWhiteY = 0;

            // double maxrange = Math.sqrt(Math.pow(spanSize/2,2) + Math.pow(spanSize/2,2));

            for (int i = cx-spanSize; i < cx+spanSize; i += stepSize){
                for (int j = cy-spanSize; j < cy+spanSize; j += stepSize){
                    Color.colorToRGB(bitmap.getPixel(cx,cy),rgb);
                    // adj = maxrange - Math.sqrt(Math.pow(i-cx,2) + Math.pow(j-cy,2));
                    // adj /= maxrange;
                    sumR += rgb[0]*adj;
                    sumG += rgb[1]*adj;
                    sumB += rgb[2]*adj;
                    count += adj;

                    if (rgb[0] > 240 && rgb[1] > 240 && rgb[2] > 240) {
                        sumWhiteX += i - cx;
                        sumWhiteY += j - cy;
                    }
                }
            }

            int pixelAmount = spanSize * spanSize * 4;
            int state = 0;
            if (0.25 * pixelAmount < whitePixelCount) {
                state = 1;
            } else if (0.75 * pixelAmount < whitePixelCount) {
                state = 2;
            }

            rgb[0] = (byte)(sumR / count);
            rgb[1] = (byte)(sumG / count);
            rgb[2] = (byte)(sumB / count);

            color = Color.RGBToColor(rgb);

            udpout[0] = (byte) rgb[0];
            udpout[1] = (byte) rgb[1];
            udpout[2] = (byte) rgb[2];
            udpout[5] = (byte) sumWhiteX/count;
            udpout[6] = (byte) sumWhiteY/count;
            udpout[7] = (byte) pixelAmount;
            udpout[8] = (byte) state;
        }catch (Exception e){
            Log.d("thaw-calc-color", "can't get bitmap");
      color & 0x00FF0000) >> 16;
        rgb[1] = (color & 0x0000FF00) >> 8;
        rgb[2] = (color & 0x000000FF) >> 0;
    }

    private int RGBToColor(byte[] rgb){
        int r = (rgb[0]) << 16;
        int g = (rgb[1]) << 8;
        int b = (rgb[2]) << 0;
        return r & g & b & 0xFF000000;
    }
}
