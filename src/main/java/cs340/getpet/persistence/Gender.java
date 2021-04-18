package cs340.getpet.persistence;

import com.google.gson.annotations.SerializedName;

import cs340.getpet.util.EnumSerializer;

/**
 * The gender of an animal.
 */
public enum Gender {
    @SerializedName("m")
    MALE,
    @SerializedName("f")
    FEMALE;

    @Override
    public String toString() {
        return EnumSerializer.toString(this, Gender.class);
    }

    public static Gender fromString(String s) {
        return EnumSerializer.fromString(s, Gender.class);
    }
}