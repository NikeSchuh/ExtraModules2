package de.nike.extramodules2.utils;

import de.nike.extramodules2.utils.vectors.Vector2Float;

public class NikesMath {

    public static float lerp(float start, float end, float smoothSpeed) {
        return (end - start) * smoothSpeed + start;
    }
    public static double lerp(double start, double end, double smoothSpeed) {
        return (end - start) * smoothSpeed + start;
    }
}
