package uz.pdp.bankcard.util;

public class StringManager {
    public static boolean isContainsOnlyNumber(String text) {
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (!(character >= '0' && character <= '9')) {
                return false;
            }
        }
        return true;
    }
}
