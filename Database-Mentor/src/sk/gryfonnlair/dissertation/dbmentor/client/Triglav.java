package sk.gryfonnlair.dissertation.dbmentor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import sk.gryfonnlair.dissertation.dbmentor.client.di.MyGinjector;
import sk.gryfonnlair.dissertation.dbmentor.client.event.*;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.AdminAppManager;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.login.LoginPresenter;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.UserAppManager;
import sk.gryfonnlair.dissertation.dbmentor.server.services.LoginServiceImpl;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;

/**
 * Trihlav alebo Triglav bolo lokálne trojhlavé božstvo uctievané polabskými Slovanmi
 * najmä vo Woline a Štetíne, neskôr aj v Brandenbursku. Tri hlavy symbolizujú sféry
 * jeho vlády – zem (login user nornmalne), nebo (login user zo session) a podsvetie (admin)
 */
public class Triglav {

    private final MyGinjector myGinjector;
    private final EventBus eventBus;
    private final LoginPresenter loginPresenter;

    public Triglav() {

        myGinjector = GWT.create(MyGinjector.class);
        eventBus = myGinjector.getEventBus();

        loginPresenter = myGinjector.getLoginPresenter();

        eventBus.addHandler(UserLoggedInEvent.getType(),
                new UserLoggedInHandler() {

                    @Override
                    public void onUserLoggedIn(UserLoggedInEvent event) {
                        Cookies.setCookie(CookieKeys.ROLE_COOKIES_KEY, CookieKeys.ROLE_USER);
                        String cookiesRole = Cookies.getCookie(CookieKeys.ROLE_COOKIES_KEY);
                        GWT.log("Triglav > EventBus > user logged as " + cookiesRole + "Cookies: "
                                + event.getUser().getName() + ", "
                                + event.getUser().getDbTypeToken() + ", "
                                + event.getUser().getConnectionURL() + ", "
                                + event.getUser().getDb() + ", ");

                        Cookies.setCookie(CookieKeys.USER_NAME_COOKIES_KEY, event.getUser().getName());
                        Cookies.setCookie(CookieKeys.USER_DB_TYPE_BUNDLE_NAME_COOKIES_KEY, event.getUser().getDbTypeBundleName());
                        Cookies.setCookie(CookieKeys.USER_DB_URL_COOKIES_KEY, event.getUser().getConnectionURL());
                        Cookies.setCookie(CookieKeys.USER_DB_NAME_COOKIES_KEY, event.getUser().getDb());
                        displayUserPage();
                    }
                });

        eventBus.addHandler(UserLoggedOutEvent.getType(),
                new UserLoggedOutHandler() {
                    @Override
                    public void onUserLoggedOut(UserLoggedOutEvent event) {
                        logoutUser();
                    }
                });

        eventBus.addHandler(AdminLoggedInEvent.getType(),
                new AdminLoggedInHandler() {
                    @Override
                    public void onAdminLoggedIn(AdminLoggedInEvent event) {
                        String tmpAdminName = event.getAdmin().getName();
                        String tmpAdminLastLoggedTimestamp = Long.toString(event.getAdmin().getLastLogged());

                        Cookies.setCookie(CookieKeys.ROLE_COOKIES_KEY, CookieKeys.ROLE_ADMIN);
                        Cookies.setCookie(CookieKeys.ADMIN_NAME_COOKIES_KEY, tmpAdminName);
                        Cookies.setCookie(CookieKeys.ADMIN_LAST_LOGGED_COOKIES_KEY, tmpAdminLastLoggedTimestamp);

                        GWT.log("Triglav > EventBus > " + CookieKeys.ROLE_ADMIN + " logged," +
                                " name:" + tmpAdminName + " lastLogged:" + tmpAdminLastLoggedTimestamp);
                        displayAdminPage();
                    }
                });

        eventBus.addHandler(AdminLoggedOutEvent.getType(),
                new AdminLoggedOutHandler() {
                    @Override
                    public void onAdminLoggedOut(AdminLoggedOutEvent event) {
                        logoutAdmin();
                        displayLoginPage();
                    }
                });
    }

    public void ruleTheWorld(String roleCookie) {
        if (roleCookie == null) {
            displayLoginPage();
        } else if (CookieKeys.ROLE_USER.equals(roleCookie)) {
            LoginServiceImpl.App.getInstance().loginUserFromSession(new AsyncCallback<User>() {
                @Override
                public void onFailure(Throwable caught) {
                    RootPanel.get("error_div").add(new Label("Triglav > Session login > onFailure = cookie found but service failed" + caught.getMessage()));
                    GWT.log("Triglav > Session login > onFailure = cookie found but service failed");
                }

                @Override
                public void onSuccess(User result) {
                    if (result != null) {
                        eventBus.fireEvent(new UserLoggedInEvent(result));
                    } else {
                        displayLoginPage();
                        GWT.log("Triglav > Session login > onSuccess = cookie found but in session no object");
                    }
                }
            });
        } else { //SESION LOGIN PRE ADMIN NERIESIM
            displayLoginPage();
        }
    }

    private void logoutUser() {
        GWT.log("Triglav.logoutUser > user logout, remove cookies");
        cleanAllUserCookies();
        LoginServiceImpl.App.getInstance().logoutUser(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
            }
        });
        displayLoginPage();
    }

    public void logoutAdmin() {
        GWT.log("Triglav.logoutAdmin > admin logout, remove cookies");
        cleanAllAdminCookies();
        LoginServiceImpl.App.getInstance().logoutAdmin(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
            }
        });
    }

    /**
     * Ak logout cez UI tak ano lebo to chce user, ale ak close window/refresh tak nie nech ma logne sprave
     */
    private void cleanAllUserCookies() {
        Cookies.removeCookie(CookieKeys.ROLE_COOKIES_KEY);
        Cookies.removeCookie(CookieKeys.USER_NAME_COOKIES_KEY);
        Cookies.removeCookie(CookieKeys.USER_DB_URL_COOKIES_KEY);
        Cookies.removeCookie(CookieKeys.USER_DB_NAME_COOKIES_KEY);
        Cookies.removeCookie(CookieKeys.USER_DB_TYPE_BUNDLE_NAME_COOKIES_KEY);
    }

    /**
     * Ak logout cez UI tak ano lebo to chce admin, ale ak close window/refresh tak nie nech ma logne sprave
     */
    private void cleanAllAdminCookies() {
        Cookies.removeCookie(CookieKeys.ADMIN_NAME_COOKIES_KEY);
        Cookies.removeCookie(CookieKeys.ADMIN_LAST_LOGGED_COOKIES_KEY);
    }

    private void displayLoginPage() {
        loginPresenter.go(RootPanel.get());
    }

    /**
     * zobrazi UI pre <b>USER</b>
     */
    private void displayUserPage() {
        UserAppManager userApp = new UserAppManager(myGinjector);
        userApp.go(RootPanel.get());
    }

    /**
     * Zobrazi UI pre <b>ADMIN</b>
     */
    private void displayAdminPage() {
        AdminAppManager adminAppManager = new AdminAppManager(myGinjector);
        adminAppManager.go(RootPanel.get());
    }
}
