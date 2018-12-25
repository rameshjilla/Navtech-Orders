package navtech.com.rameshordermanagement.utils;

import android.widget.EditText;

import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by admin on 25/12/2018.
 */

public class CustomObservables {

    public static Observable<String> getCustomerNameObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }

    public static Observable<String> getCustomerAddressObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }

    public static Observable<String> getCustomerphoneObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }


    public static Observable<String> getCustomercityObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }


    public static Observable<String> getCustomerstateObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }


    public static Observable<String> getDeliveryDateObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }

    public static Observable<String> getOrderTotalObservable(EditText editText) {
        return RxTextView.textChanges(editText
        ).skip(1).map
                (new
                         Function<CharSequence, String>() {
                             @Override
                             public String apply(CharSequence charSequence) throws Exception {
                                 return charSequence.toString();
                             }
                         });
    }
}
