package cs340.getpet.persistence;

import checkers.nullness.quals.NonNull;

public class LoginResponse {
    public final @NonNull String role;

    LoginResponse(@NonNull String role) {
        if (role == null)
            throw new NullPointerException("role");

        this.role = role;
    }
}
