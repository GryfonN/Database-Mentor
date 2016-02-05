package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.header;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.client.event.AdminSwitchCardEvent;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/26/14
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminHeaderPresenter extends Presenter {

    public interface AdminHeaderView extends IsWidget {
        /**
         * Zostavi header vyzor vyplni udajmi
         *
         * @param adminName meno amdina do headru
         */
        void setupHeader(String adminName, String lastCheck);

        /**
         * Set presentera
         *
         * @param adminHeaderPresenter adminHeaderPresenter
         */
        void setPresenter(AdminHeaderPresenter adminHeaderPresenter);
    }

    /**
     * Logout
     */
    void logoutAdmin();

    /**
     * Prepnutie karty zavolanim eventu
     *
     * @param adminCardType typ karty kt chcem zobrazit
     */
    void switchAdminCard(AdminSwitchCardEvent.AdminCardType adminCardType);
}
