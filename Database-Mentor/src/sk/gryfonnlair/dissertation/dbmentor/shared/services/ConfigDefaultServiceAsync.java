package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigDefaultServiceAsync {

    /**
     * Vrati mapu K V properties file z servera
     *
     * @return
     */
    void getDefaultConfig(AsyncCallback<Map<String, String>> async);

    /**
     * Vytvori default properties podla argumentov pre login form
     *
     * @param userName
     * @param userPass
     * @param dbURL
     * @param dbName
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void saveDefaultConfig(String userName, String userPass, String dbURL, String dbName, AsyncCallback<Void> async);
}
