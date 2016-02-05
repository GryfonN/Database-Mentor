package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.header;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.client.CookieKeys;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserLoggedOutEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.event.UserSwitchCardEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 04.02.14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class UserHeaderPresenterImpl implements UserHeaderPresenter {

    private final UserHeaderView view;
    private final EventBus eventBus;

    @Inject
    public UserHeaderPresenterImpl(UserHeaderView userHeaderView, EventBus eventBus) {
        this.view = userHeaderView;
        this.eventBus = eventBus;

        bind();
    }

    @Override
    public void logoutUser() {
        eventBus.fireEvent(new UserLoggedOutEvent());
    }

    @Override
    public void switchUserCard(UserSwitchCardEvent.UserCardType userCardType) {
        eventBus.fireEvent(new UserSwitchCardEvent(userCardType));
    }

    private void getUserCookiesForHeader() {
        view.setupHeader(
                Cookies.getCookie(CookieKeys.USER_DB_TYPE_BUNDLE_NAME_COOKIES_KEY),
                Cookies.getCookie(CookieKeys.USER_DB_URL_COOKIES_KEY),
                Cookies.getCookie(CookieKeys.USER_NAME_COOKIES_KEY),
                Cookies.getCookie(CookieKeys.USER_DB_NAME_COOKIES_KEY)
        );
    }

    @Override
    public void bind() {
        view.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        getUserCookiesForHeader();
    }
}
