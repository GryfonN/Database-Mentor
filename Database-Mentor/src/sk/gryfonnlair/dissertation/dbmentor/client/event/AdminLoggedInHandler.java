package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/25/14
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminLoggedInHandler extends EventHandler {
    public void onAdminLoggedIn(AdminLoggedInEvent event);
}
