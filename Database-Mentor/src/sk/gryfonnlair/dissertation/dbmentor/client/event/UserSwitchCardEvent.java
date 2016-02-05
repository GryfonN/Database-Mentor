package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
public class UserSwitchCardEvent extends GwtEvent<UserSwitchCardHandler> {

    public enum UserCardType {
        WELCOME, QUICK, PROCEDURES, FUNCTIONS, DEBUGGER
    }

    private UserCardType userCardType;

    public UserSwitchCardEvent(UserCardType userCardType) {
        this.userCardType = userCardType;
    }

    public UserCardType getUserCardType() {
        return userCardType;
    }

    private static final Type<UserSwitchCardHandler> TYPE = new Type<UserSwitchCardHandler>();

    @Override
    public Type<UserSwitchCardHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserSwitchCardHandler handler) {
        handler.userSwitchCard(this);
    }

    public static Type<UserSwitchCardHandler> getType() {
        return TYPE;
    }
}
