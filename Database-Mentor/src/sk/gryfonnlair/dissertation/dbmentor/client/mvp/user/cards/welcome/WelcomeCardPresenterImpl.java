package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 09:46
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeCardPresenterImpl implements WelcomeCardPresenter {

    private WelcomeCardView view;

    @Inject
    public WelcomeCardPresenterImpl(WelcomeCardView welcomeCardView) {
        this.view = welcomeCardView;

        bind();
    }


    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }

    @Override
    public void bind() {
        view.setPresenter(this);
    }
}
