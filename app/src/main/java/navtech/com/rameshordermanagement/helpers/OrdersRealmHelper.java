package navtech.com.rameshordermanagement.helpers;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import navtech.com.rameshordermanagement.interfaces.RealmCallbacks;
import navtech.com.rameshordermanagement.models.Order;
import navtech.com.rameshordermanagement.utils.Constants;

/**
 * Created by admin on 23/12/2018.
 */

public class OrdersRealmHelper {

    public static RealmResults<Order> getOrders(int userid) {
        try {
            return Realm.getDefaultInstance().where(Order.class).equalTo(Constants.USER_ID, userid)
                    .findAllAsync().sort("order_id", Sort.DESCENDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addOrder(Order order, int user_id) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realm1 -> {
            Number currentIdNum = realm1.where(Order.class).max(Constants.ORDER_ID);
            int nextId;
            if (currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            order.setOrder_id(nextId);
            order.setUser_id(user_id);
            try {
                realm1.copyToRealm(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void editOrder(Order order, RealmCallbacks realmCallbacks) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            try {
                realm.insertOrUpdate(order);
                realmCallbacks.isTransactionSuccessFul(true);
            } catch (Exception e) {
                e.printStackTrace();
                realmCallbacks.isTransactionSuccessFul(false);
            }
            realm.close();
        });
    }

    public static void deleteOrder(int order_id, RealmCallbacks realmCallbacks) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            RealmResults<Order> result = realm.where(Order.class).equalTo(Constants.ORDER_ID,
                    order_id).findAll();
            try {
                result.deleteAllFromRealm();
                realmCallbacks.isTransactionSuccessFul(true);
            } catch (Exception e) {
                e.printStackTrace();
                realmCallbacks.isTransactionSuccessFul(false);
            }
        });
    }
}
