package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Admin;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 28.12.2013
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public interface LoginServiceAsync {
    void loginUser(User user, AsyncCallback<User> async);

    void loginUserFromSession(AsyncCallback<User> async);

    /**
     * return void lebo ci uz success alebo fail tam rozhodnem o fire eventu do bussu, na closewindow jebem na response
     */
    void logoutUser(AsyncCallback<Void> async);

    //ADMIN
    void loginAdmin(Admin admin, AsyncCallback<Admin> async);

    void logoutAdmin(AsyncCallback<Void> async);
}
