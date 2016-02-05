package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/15/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserLoggedOutHandler extends EventHandler {
    public void onUserLoggedOut(UserLoggedOutEvent event);
}
