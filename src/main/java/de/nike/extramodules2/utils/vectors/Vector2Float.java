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


    public static Vector2Float of(Point point) {
        return new Vector2Float(point.x, point.y);
    }

    public Vector2Float multiply(float value) {
        return new Vector2Float(x * value, y * value);
    }

    public Vector2Float add(Vector2Float Vector2Float) {
        return new Vector2Float(x + Vector2Float.x, y + Vector2Float.y);
    }

    public Vector2Float minus(Vector2Float Vector2Float) {
        return new Vector2Float(x - Vector2Float.x, y - Vector2Float.y);
    }

    public float mag() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public static Vector2Float lerp(Vector2Float start, Vector2Float end, float smoothSpeed) {
        return end.minus(start).multiply(smoothSpeed).add(start);
    }

    public Vector2Float add(float xAdd, float yAdd) {
        return new Vector2Float(x + xAdd, y + yAdd);
    }

    public float distanceHyp(Vector2Float other) {
        return (float) Math.hypot(other.x - x, other.y - y);
    }

    public float distance(Vector2Float other) {
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

    public Vector2Float normalize() {
        return new Vector2Float((x/ mag()), ((y/ mag())));
    }
    
}
