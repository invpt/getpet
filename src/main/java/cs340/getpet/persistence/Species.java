package cs340.getpet.persistence;

import com.google.gson.annotations.SerializedName;

import cs340.getpet.util.EnumSerializer;

/**
 * The species of an animal.
 */
public enum Species {
    @SerializedName("dog")
    DOG,
    @SerializedName("cat")
    CAT;

    @Override
    public String toString() {
        return EnumSerializer.toString(this, Species.class);
    }

    public static Species fromString(String s) {
        return EnumSerializer.fromString(s, Species.class);
    }
}