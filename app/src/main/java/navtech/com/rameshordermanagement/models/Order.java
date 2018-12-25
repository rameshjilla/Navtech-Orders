package navtech.com.rameshordermanagement.models;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import navtech.com.rameshordermanagement.BR;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import navtech.com.rameshordermanagement.RealmDataBinding;

/**
 * Created by admin on 22/12/2018.
 */

public class Order extends RealmObject implements RealmDataBinding, Observable {
    @PrimaryKey
    private int order_id;
    @Ignore
    private transient PropertyChangeRegistry mCallbacks;
    private String customer_name;
    private String customer_city;
    private String customer_state;
    private String customer_address;
    private String customer_phonenumber;
    private double order_total;

    private String order_date;
    private int user_id;

    @Bindable
    public String getCustomer_city() {
        return customer_city;
    }

    public void setCustomer_city(String customer_city) {
        this.customer_city = customer_city;
        notifyPropertyChanged(BR.customer_city);

    }

    @Bindable
    public String getCustomer_state() {
        return customer_state;

    }

    public void setCustomer_state(String customer_state) {
        this.customer_state = customer_state;
        notifyPropertyChanged(BR.customer_state);
    }


    @Bindable
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
        notifyPropertyChanged(BR.order_id);
    }

    @Bindable
    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
        notifyPropertyChanged(BR.order_date);
    }

    @Bindable
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
        notifyPropertyChanged(BR.customer_name);
    }

    @Bindable
    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
        notifyPropertyChanged(BR.customer_address);
    }

    @Bindable
    public String getCustomer_phonenumber() {
        return customer_phonenumber;
    }

    public void setCustomer_phonenumber(String customer_phonenumber) {
        this.customer_phonenumber = customer_phonenumber;
        notifyPropertyChanged(BR.customer_phonenumber);
    }

    @Bindable
    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
        notifyPropertyChanged(BR.order_total);
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    @Override
    public void notifyChange() {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, 0, null);
        }
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks == null) {
            mCallbacks = new PropertyChangeRegistry();
        }
        mCallbacks.add(callback);
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }

    public synchronized void notifyPropertyChanged(int fieldId) {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }
}
