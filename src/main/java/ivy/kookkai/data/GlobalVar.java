package ivy.kookkai.data;

import ivy.kookkai.vision.ColorManager;

public final class GlobalVar {

	public static final int KOOKKAI_MARK = 3;

	public static int frameWidth;
	public static int frameHeight;

	public static void initVar() {
		ColorManager.initVar();
	}
}
