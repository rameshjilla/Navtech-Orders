package navtech.com.rameshordermanagement.presenters;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;
import navtech.com.rameshordermanagement.R;
import navtech.com.rameshordermanagement.models.User;
import navtech.com.rameshordermanagement.ui.views.LoginView;
import navtech.com.rameshordermanagement.utils.Constants;
import navtech.com.rameshordermanagement.utils.MyApplication;

/**
 * Created by admin on 22/12/2018.
 */

public class LoginPresenter {
    private final LoginView loginView;


    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(bgRealm -> {
        }, () -> {
            User userLogin = realm.where(User.class).equalTo(Constants.USER_NAME, user
                    .getUsername()).equalTo(Constants.PASSWORD, user.getPassword()).findFirst();
            if (userLogin != null) {
                loginView.onLoginSuccess(userLogin.getUser_id(), userLogin.getUsername());
            } else {
                loginView.error(Constants.INVALID_LOGIN);
            }
        }, error -> loginView.error(error.toString()));
    }

    public void loadJsonFromAssets() throws IOException {
        Realm realm = Realm.getDefaultInstance();
        InputStream stream = MyApplication.getInstance().getApplicationContext()
                .getResources().openRawResource(R.raw.usersdata);
        realm.executeTransactionAsync(realm1 -> {
            try {
                realm1.createAllFromJson(User.class, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                RealmResults<User> users = realm.where(User.class)
                        .findAll();
                if (users.size() > 0)
                    loginView.usersSavedinDB(true);
                else loginView.usersSavedinDB(false);
            }
        }, error -> loginView.error(error.toString()));
    }

    public void checkUsersExists() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> users = realm.where(User.class)
                .findAll();
        if (users.size() > 0)
            loginView.areUsersExists(
                    true);
        else loginView.areUsersExists(false);
    }
}
