package navtech.com.rameshordermanagement.presenters;

import io.realm.RealmResults;
import navtech.com.rameshordermanagement.helpers.OrdersRealmHelper;
import navtech.com.rameshordermanagement.models.Order;
import navtech.com.rameshordermanagement.ui.views.HomepageView;
import navtech.com.rameshordermanagement.utils.Constants;

/**
 * Created by admin on 23/12/2018.
 */

public class HomePagePresenter {
    private HomepageView homepageView;

    public HomePagePresenter(HomepageView homepageView) {
        this.homepageView = homepageView;
    }

    public void getOrders(int user_id) {
        RealmResults<Order> orderRealmResults = OrdersRealmHelper.getOrders(user_id);
        if (orderRealmResults == null) {
            homepageView.error("Somethig went wrong");
        } else homepageView.getOrders(orderRealmResults);
    }

    public void addOrder(Order order, int user_id) {
        OrdersRealmHelper.addOrder(order, user_id);

    }


    public void editOrder(Order order) {
        OrdersRealmHelper.editOrder(order, success -> {
            if (!success) {
                homepageView.error(Constants.ERROR);
            } else homepageView.success(Constants.ORDER_EDITED_SUCCESS);
        });
    }


    public void deleteOrder(int order_id) {
        OrdersRealmHelper.deleteOrder(order_id, success -> {
            if (!success) {
                homepageView.error(Constants.ERROR);
            } else homepageView.success(Constants.ORDER_DELETED_SUCCESS);
        });
    }


}
