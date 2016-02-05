package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/25/14
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminLoggedOutEvent extends GwtEvent<AdminLoggedOutHandler> {

    private static final Type<AdminLoggedOutHandler> TYPE = new Type<AdminLoggedOutHandler>();

    @Override
    public Type<AdminLoggedOutHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Should only be called by {@link com.google.gwt.event.shared.HandlerManager}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    @Override
    protected void dispatch(AdminLoggedOutHandler handler) {
        handler.onAdminLoggedOut(this);
    }

    public static Type<AdminLoggedOutHandler> getType() {
        return TYPE;
    }
}
