package navtech.com.rameshordermanagement.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import navtech.com.rameshordermanagement.R;
import navtech.com.rameshordermanagement.adapters.OrdersRecycleAdapter;
import navtech.com.rameshordermanagement.models.Order;

/**
 * Created by admin on 23/12/2018.
 */

public class OrdersActivity extends AppCompatActivity {
    RecyclerView rv_orders;
    Button btn_add_orders;
    final int min = 1;
    final int max = 20;
    OrdersRecycleAdapter ordersRecycleAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        setContentView(R.layout.activity_orders);
        rv_orders = findViewById(R.id.rv_orders);
        btn_add_orders = findViewById(R.id.btn_addorder);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Order> orders = realm.where(Order.class).equalTo("order_id", 19)
                        .findAll();
                orders.setValue("customer_name", "ASHWINI");
            }
        });


        rv_orders.setLayoutManager(new LinearLayoutManager(this));
        btn_add_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Order order = realm.createObject(Order.class, new Random().nextInt((max - min) + 1) + min);
                        order.setCustomer_name("Ramesh");
                        order.setCustomer_phonenumber("9949384362");
                        order.setUser_id(1);
                        realm.copyToRealm(order);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        RealmResults<Order> orders = null;
                        try {
                            orders = realm.where(Order.class).equalTo("user_id",
                                    1).findAll();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                     /*   ordersRecycleAdapter = new OrdersRecycleAdapter(orders, OrdersActivity.this);
                        rv_orders.setAdapter(ordersRecycleAdapter);
                        Log.d("orderssize", String.valueOf(orders.size()));*/


                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }
        });

    }
}
