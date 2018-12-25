package navtech.com.rameshordermanagement.ui.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.functions.Function7;
import io.reactivex.observers.DisposableObserver;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import navtech.com.rameshordermanagement.R;
import navtech.com.rameshordermanagement.adapters.OrdersRecycleAdapter;
import navtech.com.rameshordermanagement.databinding.AddOrderBinding;
import navtech.com.rameshordermanagement.databinding.EditOrderBinding;
import navtech.com.rameshordermanagement.models.Order;
import navtech.com.rameshordermanagement.presenters.HomePagePresenter;
import navtech.com.rameshordermanagement.ui.views.HomepageView;
import navtech.com.rameshordermanagement.utils.Constants;
import navtech.com.rameshordermanagement.utils.CurrencyFilter;
import navtech.com.rameshordermanagement.utils.CustomObservables;
import navtech.com.rameshordermanagement.utils.TinyDB;
import navtech.com.rameshordermanagement.utils.Utils;

/**
 * Created by admin on 23/12/2018.
 */

public class HomePage extends Fragment implements HomepageView,
        OrdersRecycleAdapter.GetOrderActions {
    HomePagePresenter homePagePresenter;
    TinyDB tinyDB;
    RealmResults<Order> intialrealmOrders;
    RealmChangeListener<Order> changeListener;
    int user_id;
    AddOrderBinding addOrderBinding;
    EditOrderBinding editOrderBinding;
    RecyclerView rv_orders;
    TextView textview_no_orders;
    Observable<Boolean> observable;
    Observable<String> Observable_customerName;
    Observable<String> Observable_customerAddress;
    Observable<String> Observable_customerPhone;
    Observable<String> Observable_customercity;
    Observable<String> Observable_customerState;
    Observable<String> Observable_customerDelivery;
    Observable<String> Observable_customerOrderTotal;
    boolean enableButton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        rv_orders = view.findViewById(R.id.rv_orders);
        textview_no_orders = view.findViewById(R.id.tv_noorders);
        rv_orders.setLayoutManager(new LinearLayoutManager(getActivity()));

        init();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private void init() {
        tinyDB = new TinyDB(getActivity());
        homePagePresenter = new HomePagePresenter(this);
        user_id = tinyDB.getInt("user_id");
        homePagePresenter.getOrders(user_id);

        intialrealmOrders.addChangeListener(new RealmChangeListener<RealmResults<Order>>() {
            @Override
            public void onChange(RealmResults<Order> orderRealmResults) {
                orderRealmResults.sort("order_id", Sort.DESCENDING);

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_order_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_order) {
            addOrder();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void error(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getOrders(RealmResults<Order> orderRealmResults) {
        intialrealmOrders = orderRealmResults;

        OrdersRecycleAdapter ordersRecycleAdapter = new OrdersRecycleAdapter(intialrealmOrders,
                getActivity(), this);
        rv_orders.setAdapter(ordersRecycleAdapter);
    }

    @Override
    public void success(String success) {
        Toast.makeText(getActivity(), success, Toast.LENGTH_SHORT).show();
    }

    private void addOrder() {
        AlertDialog dialog = getAlertDialog("Add Order", "Create");
        addOrderBinding.setOrders(new Order());
        addOrderBinding.editOrderTotal.setKeyListener(new CurrencyFilter());
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double d = 0;
                try {
                    if (Double.compare(Double.valueOf(addOrderBinding.editOrderTotal
                                    .getText().toString()),
                            Double.valueOf
                                    (0.0)) > 0) {
                        d = Double.valueOf(addOrderBinding.editOrderTotal.getText().toString());
                        addOrderBinding.getOrders().setOrder_total(d);
                        homePagePresenter.addOrder(addOrderBinding.getOrders(), user_id);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Please Enter the valid Amount", Toast
                                .LENGTH_SHORT).show();
                    }


                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Please Enter the valid Amount", Toast
                            .LENGTH_SHORT).show();
                }


            }
        });
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        validateOrderFields(dialog.getButton(DialogInterface.BUTTON_POSITIVE));
    }


    @NonNull
    private AlertDialog getAlertDialog(String title, String action) {
        addOrderBinding = DataBindingUtil.inflate(
                getLayoutInflater(), R.layout.add_order, null, false);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title);
        alert.setView(addOrderBinding.getRoot());
        alert.setCancelable(false);
        addOrderBinding.editDeliveryDate.setOnClickListener(new ClickListener());
        alert.setNegativeButton("Cancel", (dialog, which) -> {

        });
        alert.setPositiveButton(action, (dialog, which) -> {

        });
        AlertDialog dialog = alert.create();
        dialog.show();
        return dialog;
    }


    @Override
    public void editOrder(Order order) {
        AlertDialog dialog = getAlertDialog("Edit Order", "Edit");
        addOrderBinding.setOrders(order);
        addOrderBinding.editOrderTotal.setText(String.valueOf(order.getOrder_total()));
        addOrderBinding.editOrderTotal.setKeyListener(new CurrencyFilter());
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(addOrderBinding.getOrders().getCustomer_name()) && !TextUtils.isEmpty
                        (addOrderBinding.getOrders().getCustomer_address()) &&
                        !TextUtils.isEmpty(addOrderBinding.getOrders().getCustomer_phonenumber())
                        && !TextUtils.isEmpty(addOrderBinding.getOrders().getCustomer_city()) &&
                        !TextUtils.isEmpty(addOrderBinding.getOrders().getCustomer_state()) &&
                        !TextUtils.isEmpty(addOrderBinding.getOrders().getOrder_date()) &&
                        !TextUtils.isEmpty(String.valueOf(addOrderBinding.editOrderTotal.getText
                                ().toString()))) {

                    if (addOrderBinding.getOrders().getCustomer_phonenumber().matches
                            (Constants.PHONE_NUMBER_REGEX)) {
                        double d = 0;
                        try {
                            if (Double.compare(Double.valueOf(addOrderBinding.editOrderTotal
                                            .getText().toString()),
                                    Double.valueOf
                                            (0.0)) > 0) {
                                d = Double.valueOf(addOrderBinding.editOrderTotal.getText().toString());
                                addOrderBinding.getOrders().setOrder_total(d);
                                homePagePresenter.editOrder(addOrderBinding.getOrders());
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "Please Enter the valid Amount", Toast
                                        .LENGTH_SHORT).show();
                            }


                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), "Please Enter the valid Amount", Toast
                                    .LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Please Enter the Valid Phone Number", Toast
                                .LENGTH_SHORT)
                                .show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please Enter the fields", Toast.LENGTH_SHORT)
                            .show();
                }


            }
        });


    }


    @Override
    public void deleteOrder(int order_id) {
        homePagePresenter.deleteOrder(order_id);
    }

    private void validateOrderFields(Button button) {
        getCustomerObservables();
        observable = Observable.combineLatest(Observable_customerName, Observable_customerAddress,
                Observable_customerPhone, Observable_customercity, Observable_customerState,
                Observable_customerDelivery, Observable_customerOrderTotal,
                new Function7<String, String, String, String, String, String, String, Boolean>() {
                    @Override
                    public Boolean apply(String s, String s2, String s3, String s4, String s5, String s6, String s7) throws Exception {
                        return isValidForm(s, s2, s3, s4, s5, s6, s7);
                    }
                });


        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateButton(aBoolean, button);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public boolean isValidForm(String customername, String customeraddress, String customerphone,
                               String customer_city, String customer_state, String
                                       customer_deliveryDate, String customer_order_total) {
        boolean validname = !customername.isEmpty();

        if (!validname) {
            addOrderBinding.tipCustomerName.setError("Please enter name");
        } else {
            addOrderBinding.tipCustomerName.setError("");
        }
        boolean validaddress = !customeraddress.isEmpty();
        if (!validaddress) {
            addOrderBinding.tipCustomerAddress.setError("Please enter address");
        } else {
            addOrderBinding.tipCustomerAddress.setError("");
        }


        boolean validphonenumber = !customerphone.isEmpty() && Utils.isPhoneNumberValid(customerphone);
        if (!validphonenumber) {
            addOrderBinding.tipCustomerPhone.setError("Please enter valid Phone Number");

        } else {
            addOrderBinding.tipCustomerPhone.setError("");
        }

        boolean validcity = !customer_city.isEmpty();

        if (!validcity) {
            addOrderBinding.tipCustomerCity.setError("Please enter City");
        } else {
            addOrderBinding.tipCustomerCity.setError("");
        }


        boolean validstate = !customer_state.isEmpty();

        if (!validstate) {
            addOrderBinding.tipCustomerState.setError("Please enter State");
        } else {
            addOrderBinding.tipCustomerState.setError("");
        }

        boolean validdeliveryDate = !customer_deliveryDate.isEmpty();

        if (!validdeliveryDate) {
            addOrderBinding.tipCustomerDeliveryDate.setError("Please enter Delivery Date");
        } else {
            addOrderBinding.tipCustomerDeliveryDate.setError("");
        }

        boolean validorderTotal = !customer_order_total.isEmpty();

        if (!validorderTotal) {
            addOrderBinding.tipCustomerOrderTotal.setError("Please enter Valid Total");
        } else {
            addOrderBinding.tipCustomerOrderTotal.setError("");
        }

        return validname && validaddress
                && validphonenumber && validcity && validstate && validdeliveryDate && validorderTotal;
    }


    public void updateButton(boolean valid, Button button) {
        if (valid)
            button.setEnabled(true);
        else button.setEnabled(false);
    }

    private void getCustomerObservables() {

        Observable_customerName = CustomObservables.getCustomerNameObservable
                (addOrderBinding.editCustomerName);
        Observable_customerAddress = CustomObservables
                .getCustomerAddressObservable(addOrderBinding.editCustomerAddress);
        Observable_customerPhone = CustomObservables.getCustomerphoneObservable
                (addOrderBinding.editCustomerPhonenumber);
        Observable_customercity = CustomObservables.getCustomercityObservable
                (addOrderBinding.editCustomerCity);
        Observable_customerState = CustomObservables.getCustomerstateObservable(addOrderBinding
                .editCustomerState);
        Observable_customerDelivery = CustomObservables.getDeliveryDateObservable(addOrderBinding.editDeliveryDate);
        Observable_customerOrderTotal = CustomObservables.getOrderTotalObservable(addOrderBinding.editOrderTotal);

    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.edit_delivery_date:
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            int modifiedmonth = monthOfYear + 1;
                            addOrderBinding.editDeliveryDate.setText(dayOfMonth + "/" +
                                    modifiedmonth + "/" + year);
                        }
                    }, now.get
                            (Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show(getActivity().getFragmentManager(), "DatePicker");
            }
        }
    }
}
