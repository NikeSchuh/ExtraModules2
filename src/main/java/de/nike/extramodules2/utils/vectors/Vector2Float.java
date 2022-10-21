package de.nike.extramodules2.utils.vectors;

import java.awt.*;
import java.util.Objects;

public class Vector2Float {

    public float x;
    public float y;

    public Vector2Float(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public static Vector2Float Of(Point point) {
        return new Vector2Float(point.x, point.y);
    }

    public Vector2Float Multiply(float value) {
        return new Vector2Float(x * value, y * value);
    }

    public Vector2Float Add(Vector2Float Vector2Float) {
        return new Vector2Float(x + Vector2Float.x, y + Vector2Float.y);
    }

    public Vector2Float Minus(Vector2Float Vector2Float) {
        return new Vector2Float(x - Vector2Float.x, y - Vector2Float.y);
    }

    public float Mag() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public static Vector2Float Lerp(Vector2Float start, Vector2Float end, float smoothSpeed) {
        return end.Minus(start).Multiply(smoothSpeed).Add(start);
    }

    public Vector2Float Add(float xAdd, float yAdd) {
        return new Vector2Float(x + xAdd, y + yAdd);
    }

    public float DistanceHyp(Vector2Float other) {
        return (float) Math.hypot(other.x - x, other.y - y);
    }

    public float Distance(Vector2Float other) {
        return (float) Math.sqrt((other.y - y) * (other.y - y) + (other.x - x) * (other.x - x));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2Float Vector2Float = (Vector2Float) o;
        return Float.compare(Vector2Float.x, x) == 0 && Float.compare(Vector2Float.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2Float Normalize() {
        return new Vector2Float((x/Mag()), ((y/Mag())));
    }
    
}
