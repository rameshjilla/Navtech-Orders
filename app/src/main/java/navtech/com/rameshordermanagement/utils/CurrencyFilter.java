package navtech.com.rameshordermanagement.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

/**
 * Created by admin on 25/12/2018.
 */

public class CurrencyFilter extends DigitsKeyListener {
    public CurrencyFilter() {
        super(false, true);
    }

    private int digits = 2;

    public void setDigits(int d) {
        digits = d;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }

        int len = end - start;
        if (len == 0) {
            return source;
        }

        int dlen = dest.length();
        for (int i = 0; i < dstart; i++) {
            if (dest.charAt(i) == '.') {

                return (dlen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);
            }
        }

        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                if ((dlen - dend) + (end - (i + 1)) > digits)
                    return "";
                else
                    break;
            }
        }

        return new SpannableStringBuilder(source, start, end);
    }
}