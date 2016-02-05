package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;

public class UserLoggedInEvent extends GwtEvent<UserLoggedInHandler> {

    private static final Type<UserLoggedInHandler> TYPE = new Type<UserLoggedInHandler>();

    @Override
    public Type<UserLoggedInHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserLoggedInHandler handler) {
        handler.onUserLoggedIn(this);
    }

    public static Type<UserLoggedInHandler> getType() {
        return TYPE;
    }

    private User user;

    public UserLoggedInEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
