package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("ConfigDefault")
public interface ConfigDefaultService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use ConfigDefaultService.App.getInstance() to access static instance of ConfigDefaultAsync
     */
    public static class App {
        private static final ConfigDefaultServiceAsync ourInstance = (ConfigDefaultServiceAsync) GWT.create(ConfigDefaultService.class);

        public static ConfigDefaultServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Vrati mapu K V properties file z servera
     *
     * @return
     */
    Map<String, String> getDefaultConfig() throws RPCServiceException;

    /**
     * Vytvori default properties podla argumentov pre login form
     *
     * @param userName
     * @param userPass
     * @param dbURL
     * @param dbName
     * @throws RPCServiceException
     */
    void saveDefaultConfig(String userName, String userPass, String dbURL, String dbName) throws RPCServiceException;
}
