package ivy.kookkai.debugview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import ivy.kookkai.vision.ColorManager;

public class DebugImgView extends View {

	Bitmap img;
	Canvas imgCanvas;
	Paint pStroke;

	public DebugImgView(Context context) {
		super(context);

		pStroke = new Paint();
		pStroke.setColor(Color.WHITE);
		pStroke.setStyle(Paint.Style.STROKE);
		pStroke.setStrokeWidth(1);
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {

		int width = Math.abs(left - right);
		int height = Math.abs(top - bottom);
		
		img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		imgCanvas = new Canvas(img);
		Log.d("ivy_debug", "wM " + width + " wH " + height);
		Log.d("debug_image","initialized by onLayout");
	}

    public void drawPixel(int x, int y, int color) {
        img.setPixel(x, y, color);
    }

    public void drawImage(byte[] colorData){
        int outIndex = 0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++, outIndex++) {
                this.drawPixel(i, j, ColorManager.rColor[colorData[outIndex]]);
            }
        }
    }

    public byte[] initColorData(){
        return new byte[img.getWidth() * img.getHeight()];
    }

	@Override
	protected void onDraw(Canvas canvas) {

		if (img != null)
			canvas.drawBitmap(img, 0, 0, null);

	}
}
