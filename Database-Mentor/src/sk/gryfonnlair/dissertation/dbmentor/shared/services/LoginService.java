package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Admin;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 28.12.2013
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("LoginService")
public interface LoginService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use LoginService.App.getInstance() to access static instance of LoginServiceAsync
     */
    public static class App {
        private static final LoginServiceAsync ourInstance = (LoginServiceAsync) GWT.create(LoginService.class);

        public static LoginServiceAsync getInstance() {
            return ourInstance;
        }
    }

    //USER
    User loginUser(User user) throws RPCServiceException;

    User loginUserFromSession() throws RPCServiceException;

    /**
     * return void lebo ci uz success alebo fail tam rozhodnem o fire eventu do bussu, na closewindow jebem na response
     */
    void logoutUser();

    //ADMIN
    Admin loginAdmin(Admin admin) throws RPCServiceException;

    void logoutAdmin();
}
