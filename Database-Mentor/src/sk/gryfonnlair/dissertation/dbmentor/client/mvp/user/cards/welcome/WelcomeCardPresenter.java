package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.welcome;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 09:41
 * To change this template use File | Settings | File Templates.
 */
public interface WelcomeCardPresenter extends Presenter {

    public interface WelcomeCardView extends IsWidget {

        public void setPresenter(WelcomeCardPresenter userHeaderPresenter);

    }
}
