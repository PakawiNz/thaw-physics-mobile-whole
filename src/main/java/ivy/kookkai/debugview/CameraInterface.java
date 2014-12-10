package ivy.kookkai.debugview;

import ivy.kookkai.Network;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraInterface extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;

	public int frameWidth = 0;
	public int frameHeight = 0;

	private byte[] loadBuffer;
	private byte[] readyBuffer;
	private int colorStartIndex = 0;
	public byte[] cbcrBuffer;
	public byte[] yBuffer;
	public static int previewformat;

	// private CapImage capImage;

	public CameraInterface(Context context) {
		super(context);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mCamera = Camera.open();
		Camera.Parameters para = mCamera.getParameters();
		CameraInterface.previewformat = para.getPreviewFormat();
		
		para.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);

		Size s = para.getPreviewSize();
		
		mCamera.setParameters(para);

		Log.d("ivy_debug", "h" + s.height + "w" + s.width);

		frameHeight = s.height;
		frameWidth = s.width;
		
		loadBuffer = new byte[frameWidth * frameHeight * 3 / 2];  
		readyBuffer = new byte[frameWidth * frameHeight * 3 / 2]; 
		cbcrBuffer = new byte[frameWidth * frameHeight / 2];
		yBuffer = new byte[frameWidth * frameHeight];
		colorStartIndex = frameWidth * frameHeight;

	}

	public void surfaceCreated(SurfaceHolder holder) {

		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.setDisplayOrientation(90);
			mCamera.addCallbackBuffer(loadBuffer);
			mCamera.setPreviewCallbackWithBuffer(new onCapture());
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			mCamera.stopPreview();
			mCamera.release();
		} catch (Exception e) {

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

	}
	
	public byte[] getHSV() {
		System.arraycopy(readyBuffer, colorStartIndex, cbcrBuffer, 0, cbcrBuffer.length);
		System.arraycopy(readyBuffer, 0, Network.imgYUV, 0, readyBuffer.length);
		
		return cbcrBuffer;
	}

	public byte[] getCbCr() {
		System.arraycopy(readyBuffer, colorStartIndex, cbcrBuffer, 0, cbcrBuffer.length);
		System.arraycopy(readyBuffer, 0, Network.imgYUV, 0, readyBuffer.length);

		return cbcrBuffer;
	}
	
	public byte[] getYPrime(){
		System.arraycopy(readyBuffer, 0, yBuffer, 0, yBuffer.length);
		return yBuffer;
	}
	
	private class onCapture implements PreviewCallback {
		public void onPreviewFrame(byte[] data, Camera camera) {
			mCamera.addCallbackBuffer(readyBuffer);
			readyBuffer = data;
		}
	}
}
