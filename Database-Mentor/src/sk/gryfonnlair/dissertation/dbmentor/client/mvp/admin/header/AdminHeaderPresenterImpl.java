package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.client.CookieKeys;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminLoggedOutEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminSwitchCardEvent;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.BundlesServiceAsync;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.LoginServiceAsync;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/26/14
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminHeaderPresenterImpl implements AdminHeaderPresenter {

    private final AdminHeaderView view;
    private final EventBus eventBus;
    private final BundlesServiceAsync bundlesServiceAsync;
    private final LoginServiceAsync loginServiceAsync;

    @Inject
    public AdminHeaderPresenterImpl(AdminHeaderView view, EventBus eventBus,
                                    BundlesServiceAsync bundlesServiceAsync, LoginServiceAsync loginServiceAsync) {
        this.view = view;
        this.eventBus = eventBus;
        this.bundlesServiceAsync = bundlesServiceAsync;
        this.loginServiceAsync = loginServiceAsync;
        bind();
    }

    /**
     * Logout
     */
    @Override
    public void logoutAdmin() {
        eventBus.fireEvent(new AdminLoggedOutEvent());
    }

    /**
     * Prepnutie karty zavolanim eventu
     *
     * @param adminCardType typ karty kt chcem zobrazit
     */
    @Override
    public void switchAdminCard(AdminSwitchCardEvent.AdminCardType adminCardType) {
        eventBus.fireEvent(new AdminSwitchCardEvent(adminCardType));
    }

    /**
     * Podla CookieKeys.ADMIN_NAME_COOKIES_KEY setne meno do lablu
     */
    private void getAdminCookiesForHeader() {
        String timeString = Cookies.getCookie(CookieKeys.ADMIN_LAST_LOGGED_COOKIES_KEY);
        String dateString = "Unknown";
        try {
            Date dateObj = new Date(1000l * Long.parseLong(timeString));
            DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("HH:mm:ss dd.MM.yyyy");
            dateString = dateTimeFormat.format(dateObj);
        } catch (NumberFormatException e) {
        }
        view.setupHeader(Cookies.getCookie(CookieKeys.ADMIN_NAME_COOKIES_KEY), dateString);
    }

    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    @Override
    public void bind() {
        view.setPresenter(this);
    }

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        getAdminCookiesForHeader();
    }
}
