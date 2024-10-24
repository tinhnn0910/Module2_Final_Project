package util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidEmail(String email) {
        return email == null || email.isEmpty() || EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidDate(String date) {
        try {
            if (date != null && !date.isEmpty()) {
                LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
