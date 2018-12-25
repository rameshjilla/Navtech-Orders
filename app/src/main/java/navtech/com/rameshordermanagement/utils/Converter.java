package navtech.com.rameshordermanagement.utils;

import android.content.res.Resources;
import android.databinding.InverseMethod;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import navtech.com.rameshordermanagement.R;

/**
 * Created by admin on 24/12/2018.
 */

public class Converter {
    @InverseMethod("toDouble")
    public static String toString(TextView view, double oldValue,
                                  double value) {
        NumberFormat numberFormat = getNumberFormat(view);
        try {
            // Don't return a different value if the parsed value
            // doesn't change
            String inView = view.getText().toString();
            double parsed =
                    numberFormat.parse(inView).doubleValue();
            if (parsed == value) {
                return view.getText().toString();
            }
        } catch (ParseException e) {
            // Old number was broken
        }
        return numberFormat.format(value);
    }

    public static double toDouble(TextView view, double oldValue,
                                  String value) {
        NumberFormat numberFormat = getNumberFormat(view);
        try {
            return numberFormat.parse(value).doubleValue();
        } catch (ParseException e) {
            Resources resources = view.getResources();
            String errStr = resources.getString(R.string.bad_number);
            view.setError(errStr);
            return oldValue;
        }
    }


    private static NumberFormat getNumberFormat(View view) {
        Resources resources = view.getResources();
        Locale locale = resources.getConfiguration().locale;
        NumberFormat format =
                NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}