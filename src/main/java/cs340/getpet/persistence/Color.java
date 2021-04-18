package cs340.getpet.persistence;

import com.google.gson.annotations.SerializedName;

import cs340.getpet.util.EnumSerializer;

/**
 * The color of an animal.
 */
public enum Color {
    @SerializedName("black")
    BLACK,
    @SerializedName("white")
    WHITE,
    @SerializedName("brown")
    BROWN,
    @SerializedName("gold")
    GOLD,
    @SerializedName("dGray")
    DARK_GRAY,
    @SerializedName("lGray")
    LIGHT_GRAY;

    @Override
    public String toString() {
        return EnumSerializer.toString(this, Color.class);
    }

    public static Color fromString(String s) {
        return EnumSerializer.fromString(s, Color.class);
    }
}