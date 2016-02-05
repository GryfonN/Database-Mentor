package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 09:42
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeCardViewImpl extends Composite implements WelcomeCardPresenter.WelcomeCardView {
    interface WelcomeCardViewImplUiBinder extends UiBinder<HTMLPanel, WelcomeCardViewImpl> {

    }

    private static WelcomeCardViewImplUiBinder ourUiBinder = GWT.create(WelcomeCardViewImplUiBinder.class);

    private WelcomeCardPresenter presenter;

    public WelcomeCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(WelcomeCardPresenter userHeaderPresenter) {
        this.presenter = userHeaderPresenter;
    }
}