package misc;

public class MathF {
	
	public static final float PI = 3.1415926535897932384626f;
	public static final float PI_OVER_360 = 0.00872664625f;
	
	public static float degreesToRadians(float degrees) {
		return degrees * (float)(PI / 180d);
	}
	public static float cotan(float angle) {
		return (float)(1f / Math.sin(angle));
	}
}
