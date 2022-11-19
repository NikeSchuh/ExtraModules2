package de.nike.extramodules2.utils;

import de.nike.extramodules2.utils.vectors.Vector2Float;

import java.awt.*;

public class NikesMath {

    public static float lerp(float start, float end, float smoothSpeed) {
        return (end - start) * smoothSpeed + start;
    }
    public static double lerp(double start, double end, double smoothSpeed) {
        return (end - start) * smoothSpeed + start;
    }

    public static int minutesToTicks(float minutes) {
        int seconds = Math.round(minutes * 60);
        return seconds * 20;
    }

    public static int fade(Color from, Color to, int alpha, double ratio) {
        int red = (int)Math.abs((ratio * to.getRed()) + ((1 - ratio) * from.getRed()));
        int green = (int)Math.abs((ratio * to.getGreen()) + ((1 - ratio) * from.getGreen()));
        int blue = (int)Math.abs((ratio * to.getBlue()) + ((1 - ratio) * from.getBlue()));
        return new Color(red, green, blue, alpha).getRGB();
    }
}
