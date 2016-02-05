package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public interface UserSwitchCardHandler extends EventHandler {
    public void userSwitchCard(UserSwitchCardEvent event);
}
