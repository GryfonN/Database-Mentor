package sk.gryfonnlair.dissertation.dbmentor.client.mvp.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminLoggedInEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserLoggedInEvent;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Admin;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.BundlesServiceAsync;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.ConfigDefaultServiceAsync;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.LoginServiceAsync;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/3/13
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private final LoginView view;
    private final EventBus eventBus;
    private final LoginServiceAsync loginServiceAsync;
    private final BundlesServiceAsync bundlesServiceAsync;
    private final ConfigDefaultServiceAsync configDefaultServiceAsync;

    @Inject
    public LoginPresenterImpl(LoginView loginView, EventBus eventBus, LoginServiceAsync loginServiceAsync,
                              BundlesServiceAsync bundlesServiceAsync, ConfigDefaultServiceAsync configDefaultServiceAsync) {
        this.view = loginView;
        this.eventBus = eventBus;
        this.loginServiceAsync = loginServiceAsync;
        this.bundlesServiceAsync = bundlesServiceAsync;
        this.configDefaultServiceAsync = configDefaultServiceAsync;
        bind();
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        getDefaultConfigurationData();
    }

    @Override
    public void bind() {
        view.setPresenter(this);
    }

    @Override
    public void loginUser(String dbTypeToken, String dbTypeBundleName, String userName, String userPass, String connectionURL, String dbName) {
        view.showLoginButtonProgressUser(true);
        User user = new User();
        user.setDbTypeToken(dbTypeToken);
        user.setDbTypeBundleName(dbTypeBundleName);
        user.setName(userName);
        user.setPassword(userPass);
        user.setConnectionURL(connectionURL);
        user.setDb(dbName);

        loginServiceAsync.loginUser(user, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                view.showErrorMsg(caught.getMessage());
                GWT.log("LoginPresenterImpl > loginUser > onFailure EXP:" + caught.getMessage());
                view.showLoginButtonProgressUser(false);
            }

            @Override
            public void onSuccess(User result) {
                view.showLoginButtonProgressUser(false);
                if (result != null) {
                    eventBus.fireEvent(new UserLoggedInEvent(result));
                } else {
                    GWT.log("LoginPresenterImpl > loginUser > onSuccess but USER=null");
                    view.showErrorMsg("LoginPresenterImpl > loginUser > onSuccess but USER=null");
                }
            }
        });
    }

    @Override
    public void loginAdmin(String adminName, String adminPass) {
        view.showLoginButtonProgressAdmin(true);
        Admin admin = new Admin();
        admin.setName(adminName);
        admin.setPassword(adminPass);

        loginServiceAsync.loginAdmin(admin, new AsyncCallback<Admin>() {
            @Override
            public void onFailure(Throwable caught) {
                view.showErrorMsg(caught.getMessage());
                GWT.log("LoginPresenterImpl > loginAdmin > onFailure EXP:" + caught.getMessage());
                view.showLoginButtonProgressAdmin(false);
            }

            @Override
            public void onSuccess(Admin result) {
                view.showLoginButtonProgressAdmin(false);
                if (result != null) {
                    eventBus.fireEvent(new AdminLoggedInEvent(result));
                } else {
                    GWT.log("LoginPresenterImpl > loginAdmin > onSuccess but ADMIN=null");
                    view.showErrorMsg("LoginPresenterImpl > loginAdmin > onSuccess but ADMIN=null");
                }
            }
        });
    }

    @Override
    public void getDefaultConfigurationData() {
        view.showDatabaseTypeListBoxProgress(true);
        configDefaultServiceAsync.getDefaultConfig(new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("error_div").add(new Label("LoginPresenterImpl > getDefaultConfigurationData > onFailure EXP:" + caught.getMessage()));
                GWT.log("LoginPresenterImpl > getDefaultConfigurationData > onFailure EXP:" + caught.getMessage());
                getActiveBundlesFromServer();
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                GWT.log("LoginPresenterImpl > getDefaultConfigurationData > onSuccess ");
                view.setDefaultConnectionData(result.get("user"), result.get("pass"), result.get("url"), result.get("database"));
                getActiveBundlesFromServer();
            }
        });
    }

    @Override
    public void getActiveBundlesFromServer() {
        bundlesServiceAsync.getActiveBundles(new AsyncCallback<Map<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("LoginPresenterImpl > getActiveBundles > onFailure EXP:" + caught.getMessage());
                view.showErrorMsg(caught.getMessage());
                view.fillDatabaseTypeListBox(new HashMap<Integer, String>(0));
                view.showDatabaseTypeListBoxProgress(false);
            }

            @Override
            public void onSuccess(Map<Integer, String> result) {
                GWT.log("LoginPresenterImpl > getActiveBundles > onSuccess ");
                view.fillDatabaseTypeListBox(result);
                view.showDatabaseTypeListBoxProgress(false);
            }
        });
    }
}
