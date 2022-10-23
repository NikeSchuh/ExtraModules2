package de.nike.extramodules2.modules.entities.defensesystem;

import java.util.HashMap;
import java.util.Map;

public enum EyeMode {

    CHASING(1),
    NOT_CHASING(2),
    RAGE(3);

    private int value;
    private static Map map = new HashMap<>();

    EyeMode(int value) {
        this.value = value;
    }

    static {
        for (EyeMode eyeMode : EyeMode.values()) {
            map.put(eyeMode.value, eyeMode);
        }
    }

    public static EyeMode valueOf(int pageType) {
        return (EyeMode) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}
