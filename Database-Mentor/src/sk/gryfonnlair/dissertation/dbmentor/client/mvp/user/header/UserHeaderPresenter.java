package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserSwitchCardEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 04.02.14
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public interface UserHeaderPresenter extends Presenter {

    public interface UserHeaderView extends IsWidget {

        public void setupHeader(String dbTypeBundleName, String dbUrl, String userName, String databaseName);

        public void setPresenter(UserHeaderPresenter userHeaderPresenter);

    }

    public void logoutUser();

    public void switchUserCard(UserSwitchCardEvent.UserCardType userCardType);
}
