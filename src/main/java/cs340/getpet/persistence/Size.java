package cs340.getpet.persistence;

import com.google.gson.annotations.SerializedName;

import cs340.getpet.util.EnumSerializer;

/**
 * The size of an animal.
 */
public enum Size {
    @SerializedName("small")
    SMALL,
    @SerializedName("medium")
    MEDIUM,
    @SerializedName("large")
    LARGE;

    @Override
    public String toString() {
        return EnumSerializer.toString(this, Size.class);
    }

    public static Size fromString(String s) {
        return EnumSerializer.fromString(s, Size.class);
    }
}