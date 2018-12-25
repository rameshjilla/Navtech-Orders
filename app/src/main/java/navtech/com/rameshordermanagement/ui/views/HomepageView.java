package navtech.com.rameshordermanagement.ui.views;

import io.realm.RealmResults;
import navtech.com.rameshordermanagement.models.Order;

/**
 * Created by admin on 23/12/2018.
 */

public interface HomepageView extends BaseView {
    void getOrders(RealmResults<Order> orderRealmResults);

    void success(String success);
}
