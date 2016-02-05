package sk.gryfonnlair.dissertation.dbmentor.client.mvp.login;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/3/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LoginPresenter extends Presenter {

    public interface LoginView extends IsWidget {

        public static final String MSG_NO_USER = "Fill user name";
        public static final String MSG_NO_PASS = "Fill password";
        public static final String MSG_NO_URL = "Fill connection url address to database";
        public static final String MSG_NO_DATABASE_NAME = "Fill database name for this user";

        public static final String PLACEHOLDER_USER_NAME = "User database login name";
        public static final String PLACEHOLDER_USER_PASS = "User password";
        public static final String PLACEHOLDER_USER_URL = "Url address to external database";
        public static final String PLACEHOLDER_USER_DATABASE = "Database name to log in";
        public static final String PLACEHOLDER_ADMIN_NAME = "Admin login name";
        public static final String PLACEHOLDER_ADMIN_PASS = "Admin password";

        public static final String LISTBOX_TOKEN_DEFAULT = "DEFAULT";
        public static final String DEFAULT_DATABASE_BUNDLE_NAME = "Default connection bundle";

        public void setDefaultConnectionData(String user, String pass, String url, String database);

        public void fillDatabaseTypeListBox(Map<Integer, String> bundlesMap);

        public void setPresenter(LoginPresenter loginPresenter);

        public void showDatabaseTypeListBoxProgress(boolean visible);

        public void showLoginButtonProgressUser(boolean visible);

        public void showLoginButtonProgressAdmin(boolean visible);

        /**
         * @param errorMsg ak NULL tak schovat error box
         */
        void showErrorMsg(String errorMsg);
    }

    public void loginUser(String dbTypeToken, String dbTypeBundleName, String userName, String userPass, String connectionURL, String dbName);

    public void loginAdmin(String adminName, String adminPass);

    public void getDefaultConfigurationData();

    public void getActiveBundlesFromServer();
}
