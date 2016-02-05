package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.ConfigDefaultService;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public class ConfigDefaultServiceImpl extends RemoteServiceServlet implements ConfigDefaultService {
    private static final String SQL_QUERY_SELECT_CONFIG = "SELECT `user`,`pass`,`url`,`database` FROM database_mentor.configuration_default WHERE `id`=1;";
    private static final String SQL_QUERY_UPDATE_CONFIG = "UPDATE `database_mentor`.`configuration_default` SET `user`=? , `pass`=? , `url`=? , `database`=? WHERE `id`=1;";
    public static final String MSG_COULD_NOT_CONNECT_TO_DB = "Could not connect to server database";

    /**
     * Vrati mapu K V properties file z servera
     *
     * @return
     */
    @Override
    public Map<String, String> getDefaultConfig() throws RPCServiceException {
        Map<String, String> map = new HashMap<String, String>(4);
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException(MSG_COULD_NOT_CONNECT_TO_DB);
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_CONFIG);
            if (resultSet.next()) {
                String user = resultSet.getString("user");
                String pass = resultSet.getString("pass");
                String url = resultSet.getString("url");
                String database = resultSet.getString("database");
                System.out.println("ConfigDefaultServiceImpl.getDefaultConfig > Vytiahol som "
                        + user + "," + pass + "," + url + "," + database);

                map.put("user", user);
                map.put("pass", pass);
                map.put("url", url);
                map.put("database", database);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("ConfigDefaultServiceImpl.getDefaultConfig > run query SQLException:" + e.getMessage());
            throw new RPCServiceException("SQL Exception: " + e.getMessage());
        }
        return map;
    }

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
    @Override
    public void saveDefaultConfig(String userName, String userPass, String dbURL, String dbName) throws RPCServiceException {
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException(MSG_COULD_NOT_CONNECT_TO_DB);
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY_UPDATE_CONFIG);
            pstmt.setString(1, userName);
            pstmt.setString(2, userPass);
            pstmt.setString(3, dbURL);
            pstmt.setString(4, dbName);
            pstmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RPCServiceException("Could not update config to database, SQL error: " + e.getMessage());
        }
    }
}