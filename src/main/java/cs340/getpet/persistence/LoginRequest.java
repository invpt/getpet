package cs340.getpet.persistence;

import checkers.nullness.quals.NonNull;

public class LoginRequest {
    public final @NonNull String username;
    public final @NonNull String password;

    LoginRequest(@NonNull String username, @NonNull String password) {
        if (username == null)
            throw new NullPointerException("username");
        if (password == null)
            throw new NullPointerException("password");
        
        this.username = username;
        this.password = password;
    }
}
