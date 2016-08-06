package developer.htaihm.minfirebaseapp;

public final class StringUtil {
    public static String cleanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return phoneNumber;
        }
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    private StringUtil() {}
}
