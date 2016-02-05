package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * // * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12/15/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserLoggedOutEvent extends GwtEvent<UserLoggedOutHandler> {

    private static final Type<UserLoggedOutHandler> TYPE = new Type<UserLoggedOutHandler>();

    @Override
    public Type<UserLoggedOutHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserLoggedOutHandler handler) {
        handler.onUserLoggedOut(this);
    }

    public static Type<UserLoggedOutHandler> getType() {
        return TYPE;
    }
}