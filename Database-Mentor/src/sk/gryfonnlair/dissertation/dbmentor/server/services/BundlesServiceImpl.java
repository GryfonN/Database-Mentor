package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.io.FileUtils;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.BundlesService;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/28/14
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BundlesServiceImpl extends RemoteServiceServlet implements BundlesService {

    public static final String SQL_QUERY_SELECT_ACTIVE_BUNDLES = "SELECT id,bundle_name FROM database_mentor.bundles WHERE active=1;";
    public static final String SQL_QUERY_DELETE_BY_ID = "DELETE FROM database_mentor.bundles WHERE id=?;";
    public static final String SQL_QUERY_UPDATE_BY_ID = "UPDATE database_mentor.bundles SET active=? WHERE id=?;";
    public static final String SQL_QUERY_SELECT_ALL_BUNDLES = "SELECT id,bundle_name,upload_date,mcl_class_name,active,driver_file_name,module_file_name FROM database_mentor.bundles;";

    public static final String DIR_BUNDLES = "/bundles";
    public static final String DIR_INACTIVE = "/inactive";
    public static final String DIR_ACTIVE = "/active";

    /**
     * Na zaklade datumu z db vyselektuje pocetnovych bundlov nahratych do systemu
     *
     * @param lastCheckDate {@link long} datum z admin objektu
     * @return int pocet noviniek
     */
    @Override
    public int getNewBundlesCount(long lastCheckDate) {
        Date tmpDate = new Date(lastCheckDate);
        System.out.println("BundlesServiceImpl.getNewBundlesCount > date:" + tmpDate.toString());
        return 89;
    }

    /**
     * Vrati obsah obsah databazy
     *
     * @return vzdy mapa hoc aj prazdna
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *          ak pruser, msg in
     */
    @Override
    public Map<Integer, String> getActiveBundles() throws RPCServiceException {
        Map<Integer, String> resultMap = new HashMap<Integer, String>();
        //TODO SLEEP nechat pre efekt
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            System.out.println("EXCEPTION PRI SLEEP");
//        }


        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Nepodarilo sa spojit s databazou servera");
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_ACTIVE_BUNDLES);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String bundleName = resultSet.getString(2);
                resultMap.put(id, bundleName);
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("LoginServiceImpl.getActiveBundles > run query SQLException:" + e.getMessage());
            throw new RPCServiceException("SQL Exception: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * List bundlov z DB
     *
     * @return
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    @Override
    public List<Bundle> getAllBundles() throws RPCServiceException {
        List<Bundle> bundles = new ArrayList<Bundle>();
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Nepodarilo sa spojit s databazou servera");
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_ALL_BUNDLES);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bundleName = resultSet.getString("bundle_name");
                String uploadDate = resultSet.getString("upload_date");
                String mclClassName = resultSet.getString("mcl_class_name");
                boolean active = resultSet.getBoolean("active");
                String driverFileName = resultSet.getString("driver_file_name");
                String moduleFileName = resultSet.getString("module_file_name");
                bundles.add(new Bundle(id, bundleName, uploadDate, mclClassName, active, driverFileName, moduleFileName));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("BundlesServiceImpl.getAllBundles > run query SQLException:" + e.getMessage());
            throw new RPCServiceException("SQL Exception: " + e.getMessage());
        }
        return bundles;
    }

    @Override
    public void moveBundle(int id, boolean active, String bundleName) throws RPCServiceException {
        File oldDir = new File(getServletContext().getRealPath(
                DIR_BUNDLES + (active ? DIR_INACTIVE : DIR_ACTIVE) + "/" + bundleName)
        );
        File newDir = new File(getServletContext().getRealPath(
                DIR_BUNDLES + (active ? DIR_ACTIVE : DIR_INACTIVE) + "/" + bundleName)
        );
        try {
            FileUtils.moveDirectory(oldDir, newDir);
        } catch (IOException e) {
            System.err.println("BundlesServiceImpl.moveBundle > move IOException:" + e.getMessage());
            throw new RPCServiceException("IOException: " + e.getMessage());
        }

        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Could not create connection to application database");
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY_UPDATE_BY_ID);

            pstmt.setInt(1, active ? 1 : 0);
            pstmt.setInt(2, id);
            pstmt.execute();
            connection.close();
        } catch (SQLException e) {
            try {
                FileUtils.moveDirectory(newDir, oldDir);
            } catch (IOException ex) {
                System.err.println("BundlesServiceImpl.moveBundle > move IOException:" + ex.getMessage());
                throw new RPCServiceException("Could not update bundle from database + IOException: " + e.getMessage());
            }
            throw new RPCServiceException("Could not update bundle from database EXP:" + e.getMessage());
        }
    }

    @Override
    public void deleteBundle(int id, boolean active, String bundleName) throws RPCServiceException {
        //check root dir
        File bundleDir = new File(getServletContext().getRealPath(
                DIR_BUNDLES + (active ? DIR_ACTIVE : DIR_INACTIVE) + "/" + bundleName));
        if (!bundleDir.exists()) {
            throw new RPCServiceException("NO directory : "
                    + DIR_BUNDLES + (active ? DIR_ACTIVE : DIR_INACTIVE) + "/" + bundleName);
        }
        //mazem kontent
        String[] entries = bundleDir.list();
        for (String s : entries) {
            File currentFile = new File(bundleDir.getPath(), s);
            currentFile.delete();
        }
        bundleDir.delete();

        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Could not create connection to application database");
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY_DELETE_BY_ID);

            pstmt.setInt(1, id);
            pstmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RPCServiceException("Could not delete bundle from database EXP:" + e.getMessage());
        }
    }
}