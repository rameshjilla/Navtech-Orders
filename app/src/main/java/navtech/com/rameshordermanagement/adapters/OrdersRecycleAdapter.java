package navtech.com.rameshordermanagement.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import navtech.com.rameshordermanagement.R;
import navtech.com.rameshordermanagement.databinding.OrdersListItemBinding;
import navtech.com.rameshordermanagement.models.Order;

/**
 * Created by admin on 23/12/2018.
 */

public class OrdersRecycleAdapter extends RealmRecyclerViewAdapter<Order, OrdersRecycleAdapter.MyViewHolder> {
    Context context;
    private LayoutInflater layoutInflater;
    GetOrderActions getOrderActions;


    @Override
    public OrdersRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        OrdersListItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.orders_list_item, parent, false);
        return new MyViewHolder(binding);
    }

    public interface GetOrderActions {
        void editOrder(Order order);

        void deleteOrder(int order_id);
    }

    public OrdersRecycleAdapter(OrderedRealmCollection<Order> orderedRealmCollection, Context
            context, GetOrderActions getOrderActions) {
        super(context, orderedRealmCollection, true);
        this.context = context;
        this.getOrderActions = getOrderActions;
    }

    @Override
    public void onBindViewHolder(OrdersRecycleAdapter.MyViewHolder holder, int position) {
        Order orders = getData().get(position);
        holder.binding.setOrders(orders);
        holder.binding.tvOrderNumber.setText("Order No # " + orders.getOrder_id());
        holder.binding.tvOrderTotal.setText(context.getResources().getString(R.string
                .symbol_rupee) + " " + String.valueOf(orders
                .getOrder_total()));
        holder.binding.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Order EditOrder = gson.fromJson(gson.toJson(Realm.getDefaultInstance()
                        .copyFromRealm(orders)), Order.class);
                getOrderActions.editOrder(EditOrder);
            }
        });

        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Do You want to Delete the Order?");
                alert.setNegativeButton("Cancel", (dialog, which) -> {

                });
                alert.setPositiveButton("Delete", (dialog, which) -> {
                    getOrderActions.deleteOrder(orders.getOrder_id());
                });
                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final OrdersListItemBinding binding;

        public MyViewHolder(final OrdersListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
