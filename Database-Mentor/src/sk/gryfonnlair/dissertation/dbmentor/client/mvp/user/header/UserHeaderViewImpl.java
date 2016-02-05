package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserSwitchCardEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 04.02.14
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class UserHeaderViewImpl extends Composite implements UserHeaderPresenter.UserHeaderView {
    interface UserHeaderViewImplUiBinder extends UiBinder<HTMLPanel, UserHeaderViewImpl> {
    }

    private static UserHeaderViewImplUiBinder ourUiBinder = GWT.create(UserHeaderViewImplUiBinder.class);

    private UserHeaderPresenter presenter;

    public UserHeaderViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        logo.setUrl(GWT.getModuleBaseURL() + "../" + "images/logo_DatabaseMentor.png");
    }

    @Override
    public void setupHeader(String dbTypeBundleName, String dbUrl, String userName, String databaseName) {
        //medzera koli vyzoru ako u debilov
        databaseConnectorTypeLabel.setText(" " + dbTypeBundleName);
        databaseUrlLabel.setText(" " + dbUrl);
        userNameLabel.setText(" " + userName);
        databaseNameLabel.setText(" " + databaseName);
    }

    @Override
    public void setPresenter(UserHeaderPresenter userHeaderPresenter) {
        this.presenter = userHeaderPresenter;
    }

    @UiField
    Label databaseConnectorTypeLabel;
    @UiField
    Label databaseUrlLabel;
    @UiField
    Label userNameLabel;
    @UiField
    Label databaseNameLabel;
    @UiField
    Image logo;

    @UiHandler("logoutButton")
    void clickLogoutButton(ClickEvent event) {
        presenter.logoutUser();
    }

    @UiHandler("welcomeButton")
    void clickWelcomeCardButton(ClickEvent event) {
        presenter.switchUserCard(UserSwitchCardEvent.UserCardType.WELCOME);
    }

    @UiHandler("quickCodeButton")
    void clickQuickCardButton(ClickEvent event) {
        presenter.switchUserCard(UserSwitchCardEvent.UserCardType.QUICK);
    }

    @UiHandler("proceduresButton")
    void clickProceduresCardButton(ClickEvent event) {
        presenter.switchUserCard(UserSwitchCardEvent.UserCardType.PROCEDURES);
    }

    @UiHandler("functionsButton")
    void clickFunctionsCardButton(ClickEvent event) {
        presenter.switchUserCard(UserSwitchCardEvent.UserCardType.FUNCTIONS);
    }

    @UiHandler("debugerButton")
    void clickDebuggerCardButton(ClickEvent event) {
        presenter.switchUserCard(UserSwitchCardEvent.UserCardType.DEBUGGER);
    }
}