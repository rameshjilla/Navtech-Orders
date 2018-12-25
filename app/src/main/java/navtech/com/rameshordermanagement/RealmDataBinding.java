package navtech.com.rameshordermanagement;

import io.realm.RealmChangeListener;

/**
 * Created by admin on 22/12/2018.
 */

public interface RealmDataBinding {
    interface Factory {
        RealmChangeListener create();
    }

    RealmDataBinding.Factory FACTORY = () -> element -> {
        if(element instanceof RealmDataBinding) {
            ((RealmDataBinding)element).notifyChange();
        }
    };

    void notifyChange();
}
