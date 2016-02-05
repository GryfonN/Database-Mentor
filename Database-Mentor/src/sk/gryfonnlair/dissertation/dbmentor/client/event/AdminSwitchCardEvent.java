package sk.gryfonnlair.dissertation.dbmentor.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/25/14
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSwitchCardEvent extends GwtEvent<AdminSwitchCardHandler> {

    /**
     * Tokeny pre definiciu akcie kliknutia kontretnej karty v event
     */
    public enum AdminCardType {
        UPLOAD, BUNDLES, CONFIG, VALIDATOR
    }

    private AdminCardType adminCardType;

    public AdminSwitchCardEvent(AdminCardType adminCardType) {
        this.adminCardType = adminCardType;
    }

    public AdminCardType getAdminCardType() {
        return adminCardType;
    }

    private static final Type<AdminSwitchCardHandler> TYPE = new Type<AdminSwitchCardHandler>();

    @Override
    public Type<AdminSwitchCardHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Should only be called by {@link com.google.gwt.event.shared.HandlerManager}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    @Override
    protected void dispatch(AdminSwitchCardHandler handler) {
        handler.adminSwitchCard(this);
    }

    public static Type<AdminSwitchCardHandler> getType() {
        return TYPE;
    }
}
