package sk.gryfonnlair.dissertation.dbmentor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.GryfProgressPopupPanel;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 28.12.2013
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseMentor implements EntryPoint {

    Triglav triglav = new Triglav();
    public static GryfProgressPopupPanel gryfProgressPopupPanel = new GryfProgressPopupPanel();

    public void onModuleLoad() {

        triglav.ruleTheWorld(Cookies.getCookie(CookieKeys.ROLE_COOKIES_KEY));

        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(Window.ClosingEvent event) {
                triglav.logoutAdmin();
            }
        });

//        Window.addCloseHandler(new CloseHandler<Window>() {
//            @Override
//            public void onClose(CloseEvent<Window> event) {
//            }
//        });
    }
}