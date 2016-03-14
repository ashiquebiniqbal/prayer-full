package net.fajarachmad.prayer.common.util;

public class QiblaDirectionUtil {
	
	/**
	 * Return qibla direction in degrees from the north (clock-wise).
	 * 
	 * @param lat latitude in degrees
	 * @param lng longitude in degrees
	 * @return 0 means north, 90 means east, 270 means west, etc
	 */
	public static double qibla(double lat, double lng) {
		double lngA = 39.82616111;
		double latA = 21.42250833;
		double deg = atan2Deg(sinDeg(lngA - lng),
			cosDeg(lat) * tanDeg(latA)
			- sinDeg(lat) * cosDeg(lngA - lng));
		return deg >= 0 ? deg : deg + 360;
	}
	
	private static double sinDeg(double deg) {
		return Math.sin(Math.toRadians(deg));
	}
	
	private static double cosDeg(double deg) {
		return Math.cos(Math.toRadians(deg));
	}
	
	private static double tanDeg(double deg) {
		return Math.tan(Math.toRadians(deg));
	}
	
	private static double atan2Deg(double y, double x) {
		return Math.toDegrees(Math.atan2(y, x));
	}
}
