package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.server.SessionKeys;
import sk.gryfonnlair.dissertation.dbmentor.server.dbconnector.bundle.DefaultMysqlConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Admin;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.User;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 28.12.2013
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    public static final String SQL_QUERY_SELECT_ADMIN = "SELECT name,password,last_logged FROM database_mentor.admins WHERE id=1;";
    private static final String SQL_QUERY_UPDATE_ADMIN_DATE = "UPDATE `database_mentor`.`admins` SET `last_logged`=? WHERE `id`='1';";
    public static final String SQL_QUERY_SELECT_SPECIFIC_BUNDLE = "SELECT id,bundle_name,mcl_class_name,driver_file_name,module_file_name FROM database_mentor.bundles WHERE active=1 AND id=";
    public static final String TOKEN_DEFAULT = "DEFAULT";
    private static final String DIR_BUNDLES = "/bundles";
    public static final String DIR_ACTIVE = "/active";

    @Override
    public User loginUser(User user) throws RPCServiceException {
        //prve test conncetion
        MentorConnectionLair mcl = establishMclConnectionLair(user);
        if (mcl == null) {
            throw new RPCServiceException("Server problem: MCL could not be created");
        }

        isConnectionByLairReady(mcl);

        //store Usera v Session Cookies sa setne v Success na clientovi
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER, user);
        session.setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_LAIR, mcl);
//            //setup SID Cookie
//            HttpServletResponse httpServletResponse = this.getThreadLocalResponse();
//            Cookie sessionId = new Cookie("sid", session.getId());
//            sessionId.setPath("/");
//            sessionId.setMaxAge(12 * 30 * 24 * 60 * 60);
//            httpServletResponse.addCookie(sessionId);
        return user;
    }

    @Override
    public User loginUserFromSession() throws RPCServiceException {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        Object userObject = session.getAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER);
        User user = userObject instanceof User ? (User) userObject : null;
        if (user != null) {
            MentorConnectionLair mcl = establishMclConnectionLair(user);
            if (mcl == null) {
                System.err.println("LoginServiceImpl.loginUserFromSession > MCL null");
                throw new RPCServiceException("Server problem: MCL could not be created");
            }
            try {
                isConnectionByLairReady(mcl);
            } catch (RPCServiceException e) {
                System.err.println("LoginServiceImpl.loginUserFromSession > Cannot connect with MCL from session");
                return null;
            }
            session.setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_LAIR, mcl);
        }
        return user;
    }

    @Override
    public void logoutUser() {
        cleanUserSession(this.getThreadLocalRequest().getSession());
    }

    @Override
    public Admin loginAdmin(final Admin admin) throws RPCServiceException {
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Nepodarilo sa spojit s databazou servera");
        }
        System.out.println("LoginServiceImpl.loginAdmin > mam connection na server databazu");

        String name = "";
        String pass = "";
        long lastLogged = 0L;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_ADMIN);

            if (resultSet.next()) {
                name = resultSet.getString("name");
                pass = resultSet.getString("password");
                lastLogged = resultSet.getLong("last_logged");
            }

            connection.close();
        } catch (SQLException e) {
            System.err.println("LoginServiceImpl.loginAdmin > run query SQLException:" + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }

        if (name.equals(admin.getName()) && pass.equals(admin.getPassword())) {
            try {
                updateAdminLastLoggedDate();

            } catch (RPCServiceException e) {
                System.err.println(e.getMessage());
            }
            HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
            HttpSession session = httpServletRequest.getSession();
            admin.setLastLogged(lastLogged);

            session.setAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN, admin);

            Admin resultAdmin = new Admin();
            resultAdmin.setName(admin.getName());
            resultAdmin.setLastLogged(admin.getLastLogged());

            return resultAdmin;
        } else {
            throw new RPCServiceException("Nespravne prihlasovacie udaje");
        }
    }

    @Override
    public void logoutAdmin() {
        cleanAdminSession(this.getThreadLocalRequest().getSession());
    }

    /**
     * Update last date prihlasenia admina
     *
     * @throws RPCServiceException ak sa nieco posere
     */
    private void updateAdminLastLoggedDate() throws RPCServiceException {
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("LoginServiceImpl.updateAdminLastLoggedDate > Nepodarilo sa spojit s databazou servera");
        }
        try {
            PreparedStatement pstm = connection.prepareStatement(SQL_QUERY_UPDATE_ADMIN_DATE);
            pstm.setLong(1, (new Date().getTime() / 1000));
            pstm.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new RPCServiceException("LoginServiceImpl.updateAdminLastLoggedDate > Nepodaril sa update " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    private void cleanUserSession(HttpSession session) {
        session.removeAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER);
        session.removeAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_LAIR);
    }

    private void cleanAdminSession(HttpSession session) {
        session.removeAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN);
        session.removeAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_DRIVER_FILEITEM);
        session.removeAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_MODULE_FILEITEM);
    }

    /**
     * Testne nasetovany a vytvoreny MCL objekt, ka chyba vrati EXP
     *
     * @param mentorConnectionLair mcl na otestovanie
     * @throws RPCServiceException ak sa nepodarilo zhovtovit connection,
     *                             viem to poslat na clienta az pri normla logine
     */
    private void isConnectionByLairReady(MentorConnectionLair mentorConnectionLair) throws RPCServiceException {
        try {
            Connection con = mentorConnectionLair.createConnection();
        } catch (Exception e) {
            System.err.println("LoginServiceImpl > isConnectionByLairReady > Chyba pri ziskavani objektu Connection" + e.getMessage());
            throw new RPCServiceException("Connection Error : " + e.getMessage());
        }
    }

    /**
     * Vytvara mcl s kt neskor pri logine pracujem
     *
     * @param user objekt login usera
     * @return NULL ak sa to posere
     */
    private MentorConnectionLair establishMclConnectionLair(User user) {
        if (TOKEN_DEFAULT.equalsIgnoreCase(user.getDbTypeToken())) {
            System.out.println("LoginServiceImpl.establishMclConnectionLair > DEFAULT choosen");
            return new DefaultMysqlConnectionLair(user.getDb(), user.getName(), user.getPassword(), user.getConnectionURL());
        } else { //reflexia
            System.out.println("LoginServiceImpl.establishMclConnectionLair > Bundle with id="
                    + user.getDbTypeToken() + " choosen");
            return createMCLWithReflexion(user);
        }
    }

    /**
     * POmocou reflexie vytvorim MCL
     *
     * @param user userdata pre tvorbu mcl
     * @return konkretnu impl objektu MentorConnectionLair,
     *         NULL ak {@linkplain BundleHolder} sa nevytiahol z db, ak driver/module file not exist
     */
    private MentorConnectionLair createMCLWithReflexion(User user) {
        //vytiahnem si bundleInfo z DB
        BundleHolder bundleHolder = getBundleDataFromDatabase(user);
        if (bundleHolder == null) {
            System.err.println("LoginServiceImpl.createMCLWithReflexion > prisiel mi z metody getBundleDataFromDatabase NULL");
            return null;
        }
        //Vytvorim si files a check si existuju vazne
        File driverFile = new File(getServletContext().getRealPath(
                DIR_BUNDLES + DIR_ACTIVE + "/" + bundleHolder.bundleName + "/" + bundleHolder.driverFileName));
        File moduleFile = new File(getServletContext().getRealPath(
                DIR_BUNDLES + DIR_ACTIVE + "/" + bundleHolder.bundleName + "/" + bundleHolder.moduleFileName));
        if (!driverFile.exists() || !moduleFile.exists()) {
            System.err.println("LoginServiceImpl.createMCLWithReflexion > driver+module file NEEXISTUJU VRAJ");
            return null;
        }
        //REFLEXIA PODLA TEST PROJEKTU
        MentorConnectionLair mcl = null;
        try {
            URL[] urls = new URL[]{driverFile.toURI().toURL(), moduleFile.toURI().toURL()};

            URLClassLoader cl = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());
            //stary zly sposob
            //URLClassLoader cl = (URLClassLoader) LoginServiceImpl.class.getClassLoader();

            Class classFromRFX = cl.loadClass(bundleHolder.mclClassName);

            Constructor constructor =
                    classFromRFX.getConstructor(new Class[]{String.class, String.class, String.class, String.class});

//            Object o = constructor.newInstance(user.getDb(), user.getName(), user.getPassword(), user.getConnectionURL());
            //Fur Test
//            Class superClass = o.getClass().getSuperclass();
//            System.out.println(" EQUALS : " + superClass.equals(MentorConnectionLair.class));
//            System.out.println("D:" + superClass.toString());
//            System.out.println(MentorConnectionLair.class.toString());
//            Field f = superClass.getDeclaredField("serialVersionUID");
//            System.out.println(f.getLong(null));
//            System.out.println("API orgininal UID : " + MentorConnectionLair.serialVersionUID);

            mcl = (MentorConnectionLair) constructor.newInstance(user.getDb(), user.getName(), user.getPassword(), user.getConnectionURL());
        } catch (Exception e) {
            System.err.println("LoginServiceImpl.createMCLWithReflexion > REFLEXIA > " + e.getClass().toString() + " : "
                    + e.getMessage());
            return null;
        }
        return mcl;
    }

    /**
     * Get z databazy presne budnle kt si zvolil user
     *
     * @param user data loginu ako user objekt
     * @return {@linkplain BundleHolder}, NULL ak error s connection, ak neni resultset,ak SQLEXP
     */
    private BundleHolder getBundleDataFromDatabase(User user) {
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            System.err.println("LoginServiceImpl.getBundleDataFromDatabase > connection null");
            return null;
        }

        BundleHolder resultBundleHolder = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_SPECIFIC_BUNDLE + user.getDbTypeToken() + ";");

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String mclClassName = resultSet.getString("mcl_class_name");
                String bundleName = resultSet.getString("bundle_name");
                String driverFileName = resultSet.getString("driver_file_name");
                String moduleFileName = resultSet.getString("module_file_name");
                resultBundleHolder = new BundleHolder(id, mclClassName, bundleName, driverFileName, moduleFileName);
            } else {
                System.err.println("LoginServiceImpl.getBundleDataFromDatabase > ziaden balik v db s takym id \n\t" +
                        "SQL>" + SQL_QUERY_SELECT_SPECIFIC_BUNDLE + user.getDbTypeToken() + ";");
            }

            connection.close();
        } catch (SQLException e) {
            System.err.println("LoginServiceImpl.getBundleDataFromDatabase > run query SQLException:" + e.getMessage());
            return null;
        }
        return resultBundleHolder;
    }

    /**
     * Pomocny objekt pre metodu createMCLWithReflexion
     */
    class BundleHolder {

        int id;
        String mclClassName;
        String bundleName;
        String driverFileName;
        String moduleFileName;

        BundleHolder(int id, String mclClassName, String bundleName, String driverFileName, String moduleFileName) {
            this.id = id;
            this.mclClassName = mclClassName;
            this.bundleName = bundleName;
            this.driverFileName = driverFileName;
            this.moduleFileName = moduleFileName;
        }
    }
}