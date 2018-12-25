package navtech.com.rameshordermanagement.utils;

/**
 * Created by admin on 24/12/2018.
 */

public class Utils {
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isPhoneNumberValid(String phonenumber) {
        return phonenumber.matches(Constants.PHONE_NUMBER_REGEX);

    }

}
