package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Admin;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/25/14
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminLoggedInEvent extends GwtEvent<AdminLoggedInHandler> {

    private static final Type<AdminLoggedInHandler> TYPE = new Type<AdminLoggedInHandler>();

    @Override
    public Type<AdminLoggedInHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AdminLoggedInHandler handler) {
        handler.onAdminLoggedIn(this);
    }

    public static Type<AdminLoggedInHandler> getType() {
        return TYPE;
    }

    private Admin admin;

    public AdminLoggedInEvent(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }
}
