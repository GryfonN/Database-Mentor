package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserAppTemplate extends Composite {
    interface UserAppTemplateUiBinder extends UiBinder<Widget, UserAppTemplate> {
    }

    private static UserAppTemplateUiBinder ourUiBinder = GWT.create(UserAppTemplateUiBinder.class);

    private UserAppManager userAppPresenter;

    public UserAppTemplate() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setUserAppPresenter(UserAppManager userAppPresenter) {
        this.userAppPresenter = userAppPresenter;
    }

    @Override
    protected void onDetach() {
        userAppPresenter.removeHistoryHandler();
        super.onDetach();
    }

    @UiField
    SimplePanel header;
    @UiField
    SimplePanel content;
}