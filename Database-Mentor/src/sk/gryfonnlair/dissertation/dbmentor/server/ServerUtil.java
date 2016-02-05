package sk.gryfonnlair.dissertation.dbmentor.server;

import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerUtil {

    public static MentorConnectionLair getMCLFromRequest(HttpServletRequest httpServletRequest) throws RPCServiceException {
        final HttpSession session = httpServletRequest.getSession();

        final Object maybeMCL = session.getAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_LAIR);
        if (maybeMCL instanceof MentorConnectionLair) {
            return (MentorConnectionLair) maybeMCL;
        } else {
            throw new RPCServiceException("NO MCL");
        }
    }

    /**
     * Precita inputStream a vrati String
     *
     * @param instream stream
     * @return String text nromalne
     */
    public static String readInputStream(final InputStream instream) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader r = new BufferedReader(new InputStreamReader(instream));
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        instream.close();
        return sb.toString();
    }

    /**
     * Connection na db podla udajov v metode zadane
     *
     * @return NULL ak EXP
     */
    public static synchronized Connection createConnectionToServer() {
        Connection connection = null;
        String serverURL = "localhost:3306";
        String databaseName = "database_mentor";
        String userName = "database_mentor";
        String userPass = "database_mentor00#";

        String driver = "com.mysql.jdbc.Driver";
        StringBuffer sb = new StringBuffer("jdbc:mysql://").append(serverURL).append('/').append(databaseName);

        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(sb.toString(), userName, userPass);
        } catch (InstantiationException e) {
            System.err.println("ServerUtil.getConnectionToMysqlServer > InstantiationException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("ServerUtil.getConnectionToMysqlServer > IllegalAccessException:" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ServerUtil.getConnectionToMysqlServer > ClassNotFoundException:" + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ServerUtil.getConnectionToMysqlServer > SQLException:" + e.getMessage());
        }
        System.out.println("ServerUtil.getConnectionToMysqlServer > MySQL Class.forName DONE !");

        return connection;
    }
}
