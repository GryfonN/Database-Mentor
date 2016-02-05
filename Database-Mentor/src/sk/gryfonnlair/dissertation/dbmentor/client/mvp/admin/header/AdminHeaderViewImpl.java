package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminSwitchCardEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/26/14
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminHeaderViewImpl extends Composite implements AdminHeaderPresenter.AdminHeaderView {
    interface AdminHeaderViewImplUiBinder extends UiBinder<HTMLPanel, AdminHeaderViewImpl> {
    }

    private static AdminHeaderViewImplUiBinder ourUiBinder = GWT.create(AdminHeaderViewImplUiBinder.class);

    private AdminHeaderPresenter presenter;

    public AdminHeaderViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        logo.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfonNLairBanner.png");
    }

    /**
     * Zostavi header vyzor vyplni udajmi
     *
     * @param adminName meno amdina do headru
     */
    @Override
    public void setupHeader(String adminName, String lastLogged) {
        this.adminName.setText(" " + adminName);
        this.adminLastLogged.setText(" " + lastLogged);
        this.adminLastLogged.setTitle("last logged date");
    }

    /**
     * Set presentera
     *
     * @param adminHeaderPresenter adminHeaderPresenter
     */
    @Override
    public void setPresenter(AdminHeaderPresenter adminHeaderPresenter) {
        this.presenter = adminHeaderPresenter;
    }

    @UiField
    Label adminName;
    @UiField
    Label adminLastLogged;
    @UiField
    Image logo;

    @UiHandler("logoutButton")
    void clickLogoutButton(ClickEvent event) {
        presenter.logoutAdmin();
    }

    @UiHandler("uploadButton")
    void clickUploadCardButton(ClickEvent event) {
        presenter.switchAdminCard(AdminSwitchCardEvent.AdminCardType.UPLOAD);
    }

    @UiHandler("bundlesButton")
    void clickBundlesCardButton(ClickEvent event) {
        presenter.switchAdminCard(AdminSwitchCardEvent.AdminCardType.BUNDLES);
    }

    @UiHandler("configurationButton")
    void clickConfigurationCardButton(ClickEvent event) {
        presenter.switchAdminCard(AdminSwitchCardEvent.AdminCardType.CONFIG);
    }
}