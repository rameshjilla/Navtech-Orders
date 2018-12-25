package navtech.com.rameshordermanagement.ui.views;

/**
 * Created by admin on 22/12/2018.
 */

public interface LoginView extends BaseView {
    void onLoginSuccess(int user_id, String username);

    void usersSavedinDB(boolean condition);

    void areUsersExists(boolean condition);


}
