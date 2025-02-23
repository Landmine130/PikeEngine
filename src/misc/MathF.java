package misc;

public class MathF {
	
	public static final float PI = 3.1415926535897932f;
	public static final float PI_OVER_2 = 1.57079632679f;
	public static final float PI_OVER_4 = 0.78539816339f;
	public static final float PI_OVER_360 = 0.00872664625f;
	
	public static float toRadians(float degrees) {
		return (float)Math.toRadians(degrees);
	}
	public static float cotan(float angle) {
		return (float)(1f / Math.tan(angle));
	}
	public static float sin(float angle) {
		return (float)Math.sin(angle);
	}
	public static float cos(float angle) {
		return (float)Math.cos(angle);
	}
	public static float tan(float angle) {
		return (float)Math.tan(angle);
	}
	public static float atan(float f) {
		return (float)Math.atan(f);
	}
	public static float atan2(float f, float f2) {
		return (float)Math.atan2(f, f2);
	}
	public static float sqrt(float f) {
		return (float)Math.sqrt(f);
	}
	public static float asin(float f) {
		return (float)Math.asin(f);
	}

}
