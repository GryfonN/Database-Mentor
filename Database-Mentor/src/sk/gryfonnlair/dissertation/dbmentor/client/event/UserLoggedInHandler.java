package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserLoggedInHandler extends EventHandler {
    public void onUserLoggedIn(UserLoggedInEvent event);
}
