package dev.hacksoar.utils.math;

import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
	
    public static float clamp(float value) {
        return (double) value < 0.0D ? 0.0F : ((double) value > 1.0D ? 1.0F : value);
    }
    
    public static float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }
    
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * clamp(t);
    }

    public static float getPercent(float val, float min, float max) {
        return (val - min) / (max - min);
    }
    
    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }
    
    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    
	public static double roundToPlace(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

    /**
     * Method which returns a double between two input numbers
     *
     * @param min minimal number
     * @param max maximal number
     * @return random between both numbers
     */
    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double round(final double value, final int places) {
        final BigDecimal bigDecimal = BigDecimal.valueOf(value);

        return bigDecimal.setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static double round(final double value, final int scale, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        } else {
            return new BigDecimal(floored)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }

    public static double roundWithSteps(final double value, final double steps) {
        double a = ((Math.round(value / steps)) * steps);
        a *= 1000;
        a = (int) a;
        a /= 1000;
        return a;
    }

    public static double lerp(final double a, final double b, final double c) {
        return a + c * (b - a);
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = x2 - x1;
        final double d1 = y2 - y1;
        final double d2 = z2 - z1;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Clamps a number, n, to be within a specified range
     * @param min The minimum permitted value of the input
     * @param max The maximum permitted value of the input
     * @param n The input number to clamp
     * @return The input, bounded by the specified minimum and maximum values
     */
    public static double clamp(double min, double max, double n) {
        return Math.max(min, Math.min(max, n));
    }

    public static boolean isInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }
}
