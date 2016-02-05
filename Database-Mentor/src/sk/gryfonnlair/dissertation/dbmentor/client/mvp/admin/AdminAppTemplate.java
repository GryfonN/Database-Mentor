package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/25/14
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAppTemplate extends Composite {
    interface AdminAppTemplateUiBinder extends UiBinder<Widget, AdminAppTemplate> {
    }

    private static AdminAppTemplateUiBinder ourUiBinder = GWT.create(AdminAppTemplateUiBinder.class);

    private AdminAppManager adminAppManager;

    public AdminAppTemplate() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setAdminAppPresenter(AdminAppManager adminAppManager) {
        this.adminAppManager = adminAppManager;
    }

    @Override
    protected void onDetach() {
        adminAppManager.removeHistoryHandler();
        super.onDetach();
    }

    @UiField
    SimplePanel header;
    @UiField
    SimplePanel content;
}