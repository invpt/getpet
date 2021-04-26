package cs340.getpet.http.rest;

public final class Validator {
    Validator() {}

    public static void assertTrue(boolean condition, String message) throws ValidationException {
        if (!condition)
            throw new ValidationException(message);
    }

    public static void assertFalse(boolean condition, String message) throws ValidationException {
        assertTrue(!condition, message);
    }

    public static void assertNull(Object o, String message) throws ValidationException {
        assertTrue(o == null, message);
    }

    public static void assertNonNull(Object o, String message) throws ValidationException {
        assertTrue(o != null, message);
    }

    public static void assertMatches(String s, String regex, String message) throws ValidationException {
        if (s != null)
            assertTrue(s.matches(regex), message);
    }
}
