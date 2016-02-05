package sk.gryfonnlair.dissertation.dbmentor.client.mvp.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 28.12.2013
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class LoginViewImpl extends Composite implements LoginPresenter.LoginView {

    private String defaultUser = "";
    private String defaultPass = "";
    private String defaultUrl = "";
    private String defaultDatabase = "";

    interface LoginViewImplUiBinder extends UiBinder<HTMLPanel, LoginViewImpl> {
    }

    private static LoginViewImplUiBinder ourUiBinder = GWT.create(LoginViewImplUiBinder.class);

    private LoginPresenter loginPresenter;

    public LoginViewImpl() {
        textBoxUserPass = new PasswordTextBox();
        textBoxAdminPass = new PasswordTextBox();
        initWidget(ourUiBinder.createAndBindUi(this));

        logo.setUrl(GWT.getModuleBaseURL() + "../" + "images/logo_DatabaseMentor.png");

        textBoxUserName.getElement().setPropertyString("placeholder", PLACEHOLDER_USER_NAME);
        textBoxUserPass.getElement().setPropertyString("placeholder", PLACEHOLDER_USER_PASS);
        textBoxURL.getElement().setPropertyString("placeholder", PLACEHOLDER_USER_URL);
        textBoxDatabaseName.getElement().setPropertyString("placeholder", PLACEHOLDER_USER_DATABASE);

        textBoxAdminName.getElement().setPropertyString("placeholder", PLACEHOLDER_ADMIN_NAME);
        textBoxAdminPass.getElement().setPropertyString("placeholder", PLACEHOLDER_ADMIN_PASS);

        toAdmin.setUrl(GWT.getModuleBaseURL() + "../" + "images/LoginToAdminIcon.png");
        toUser.setUrl(GWT.getModuleBaseURL() + "../" + "images/LoginToUserIcon.png");

        listBoxDatabaseTypesProgress.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
        buttonLoginProgressUser.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
        buttonLoginProgressAdmin.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
    }

    public static native void flipToAdmin() /*-{
        $wnd.flipToAdmin();
    }-*/;

    public static native void flipToUser() /*-{
        $wnd.flipToUser();
    }-*/;

    @Override
    public void setDefaultConnectionData(String user, String pass, String url, String database) {
        defaultUser = user;
        defaultPass = pass;
        defaultUrl = url;
        defaultDatabase = database;
    }

    @Override
    public void fillDatabaseTypeListBox(Map<Integer, String> bundlesMap) {
        listBoxDatabaseTypes.clear();
        listBoxDatabaseTypes.addItem(DEFAULT_DATABASE_BUNDLE_NAME, LISTBOX_TOKEN_DEFAULT);
        for (Map.Entry<Integer, String> entry : bundlesMap.entrySet()) {
            int id = entry.getKey();
            String bundleName = entry.getValue();
            GWT.log("LoginViewImpl.fillDatabaseTypeListBox > " + bundleName + " = id:" + id);
            listBoxDatabaseTypes.addItem(bundleName, Integer.toString(id));
        }
        listBoxDatabaseTypes.setSelectedIndex(0);
        fillLoginUserForm(true);
    }

    @Override
    public void setPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void showDatabaseTypeListBoxProgress(boolean visible) {
        listBoxPanel.setVisible(!visible);
        listBoxDatabaseTypesProgress.setVisible(visible);
    }

    @Override
    public void showLoginButtonProgressUser(boolean visible) {
        buttonLoginUser.setVisible(!visible);
        buttonLoginProgressUser.setVisible(visible);
    }

    @Override
    public void showLoginButtonProgressAdmin(boolean visible) {
        buttonLoginAdmin.setVisible(!visible);
        buttonLoginProgressAdmin.setVisible(visible);
    }

    /**
     * @param errorMsg ak NULL tak schovat error box
     */
    @Override
    public void showErrorMsg(String errorMsg) {
        if (errorMsg == null) { //schovat
            errorTextBox.setVisible(false);
        } else {
            RootPanel.get("error_div").add(new Label(errorMsg));
            errorTextBox.setVisible(true);
            errorTextBox.setText(errorMsg);
        }
    }

    /**
     * Naplni alebo clearne formular
     *
     * @param defaultValues ak TRUE tak nasetuje formular ak FALSE tak clear all textboxes
     */
    private void fillLoginUserForm(boolean defaultValues) {
        if (defaultValues) {
            textBoxUserName.setText(defaultUser);
            textBoxUserPass.setText(defaultPass);
            textBoxURL.setText(defaultUrl);
            textBoxDatabaseName.setText(defaultDatabase);
        } else {
            textBoxUserName.setText("");
            textBoxUserPass.setText("");
            textBoxURL.setText("");
            textBoxDatabaseName.setText("");
        }
    }

    private boolean checkLoginUserForm() {
        if (textBoxUserName.getText().isEmpty()) {
            showErrorMsg(MSG_NO_USER);
        } else if (textBoxUserPass.getText().isEmpty()) {
            showErrorMsg(MSG_NO_PASS);
        } else if (textBoxURL.getText().isEmpty()) {
            showErrorMsg(MSG_NO_URL);
        } else if (textBoxDatabaseName.getText().isEmpty()) {
            showErrorMsg(MSG_NO_DATABASE_NAME);
        } else {
            return true;
        }
        return false;
    }

    @UiField
    MyStyle style;
    @UiField
    Button buttonLoginUser;
    @UiField
    Image buttonLoginProgressUser;
    @UiField
    TextBox textBoxUserName;
    @UiField(provided = true)
    TextBox textBoxUserPass;
    @UiField
    TextBox textBoxURL;
    @UiField
    TextBox textBoxDatabaseName;
    @UiField
    FlowPanel listBoxPanel;
    @UiField
    ListBox listBoxDatabaseTypes;
    @UiField
    Image listBoxDatabaseTypesProgress;
    @UiField
    TextBox textBoxAdminName;
    @UiField(provided = true)
    TextBox textBoxAdminPass;
    @UiField
    Button buttonLoginAdmin;
    @UiField
    Image buttonLoginProgressAdmin;
    @UiField
    Image toAdmin;
    @UiField
    Image toUser;
    @UiField
    TextBox errorTextBox;
    @UiField
    Image logo;

    @UiHandler("buttonLoginUser")
    void clickLoginButtonUser(ClickEvent event) {
        showErrorMsg(null);
        if (checkLoginUserForm()) {
            loginPresenter.loginUser(
                    listBoxDatabaseTypes.getValue(listBoxDatabaseTypes.getSelectedIndex()),
                    listBoxDatabaseTypes.getItemText(listBoxDatabaseTypes.getSelectedIndex()),
                    textBoxUserName.getText(),
                    textBoxUserPass.getText(),
                    textBoxURL.getText(),
                    textBoxDatabaseName.getText());
        }
    }

    @UiHandler("buttonLoginAdmin")
    void clickLoginButtonAdmin(ClickEvent event) {
        showErrorMsg(null);
        loginPresenter.loginAdmin(
                textBoxAdminName.getText(),
                textBoxAdminPass.getText()
        );
    }

    @UiHandler("toUser")
    void toUser(ClickEvent event) {
        showErrorMsg(null);
        fillLoginUserForm(false);
        showDatabaseTypeListBoxProgress(true);
        loginPresenter.getActiveBundlesFromServer();
        flipToUser();
    }

    @UiHandler("toAdmin")
    void toAdmin(ClickEvent event) {
        showErrorMsg(null);
        flipToAdmin();
    }

    @UiHandler("listBoxDatabaseTypes")
    void changeListBoxDatabaseTypes(ChangeEvent e) {
        fillLoginUserForm(LISTBOX_TOKEN_DEFAULT.equals(listBoxDatabaseTypes.getValue(listBoxDatabaseTypes.getSelectedIndex())));
    }

    interface MyStyle extends CssResource {

        @ClassName("listBoxDIV-Login")
        String listBoxDIVLogin();

        @ClassName("loginProgressImage-Login")
        String loginProgressImageLogin();

        @ClassName("loginButton-Login")
        String loginButtonLogin();

        @ClassName("login-icon-Login")
        String loginIconLogin();

        @ClassName("listBox-Login")
        String listBoxLogin();

        @ClassName("loginWindowGreen-Login")
        String loginWindowGreenLogin();

        @ClassName("listBoxProgressImage-Login")
        String listBoxProgressImageLogin();

        @ClassName("errorTextBox-Login")
        String errorTextBoxLogin();

        @ClassName("logo-Login")
        String logoLogin();
    }
}