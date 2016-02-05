package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.fileupload.FileItem;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.server.SessionKeys;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.UploadService;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadServiceImpl extends RemoteServiceServlet implements UploadService {

    public static final String DIR_BUNDLES = "/bundles";
    public static final String DIR_INACTIVE = "/inactive";
    public static final String MSG_NO_JARS_IN_SESSION = "Required driver JAR file and module JAR file are not on server";
    public static final String MSG_CANNOT_SAVE_BUNDLE = "System coul not save bundle";
    public static final String MSG_BUNDLE_ALREADY_EXIST = " Bundle already exist !";
    public static final String MSG_COULD_NOT_GET_JARS_CONTENT_FILES = "Could not get JARs content files from session !";

    public static final String MSQ_COULD_NOT_CREATE_FILES = "Could not create files";
    public static final String SQL_QUERY_INSERT_BUNDLE = "INSERT INTO bundles " +
            "(bundle_name,upload_date,mcl_class_name,active,driver_file_name,module_file_name) " +
            "VALUES (?,?,?,?,?,?)";

    @Override
    public void saveUploadBundleForm(String bundleName, String mclClassName) throws RPCServiceException {
        Object driverObject =
                this.getThreadLocalRequest().getSession().getAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_DRIVER_FILEITEM);
        Object moduleObject =
                this.getThreadLocalRequest().getSession().getAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_ADMIN_MODULE_FILEITEM);
        //JARs check
        if (!(driverObject instanceof FileItem && moduleObject instanceof FileItem)) {
            throw new RPCServiceException(MSG_NO_JARS_IN_SESSION);
        }

        FileItem driverJarFileItem = (FileItem) driverObject;
        FileItem moduleJarFileItem = (FileItem) moduleObject;
        try {
            saveFilesToContainer(bundleName, driverJarFileItem, moduleJarFileItem);

            saveBundleToDatabase(bundleName, mclClassName, driverJarFileItem.getName(), moduleJarFileItem.getName());
        } catch (InstantiationException e) { //uz je taky bundle dir tak ho nemazem
            onSaveError(false, bundleName, MSG_CANNOT_SAVE_BUNDLE + "\n EXP : " + e.getMessage());
        } catch (Exception e) {
            onSaveError(true, bundleName, MSG_CANNOT_SAVE_BUNDLE + "\n EXP : " + e.getMessage());
        }
    }

    /**
     * Save potrebnych uborov a directory na disku vo warku
     *
     * @param bundleNameForDir  meno priecinku
     * @param driverJarFileItem ovladac file
     * @param moduleJarFileItem module file
     * @throws IOException
     * @throws InstantiationException ak je uz taky budnle a ja ho nechcem zmazat
     *                                tak si hovdim inu EXp aby som o tom hore vedel
     */
    private void saveFilesToContainer(String bundleNameForDir, FileItem driverJarFileItem, FileItem moduleJarFileItem)
            throws IOException, InstantiationException {
        //check root dir
        File dbConnectors = new File(getServletContext().getRealPath(DIR_BUNDLES));
        if (!dbConnectors.exists()) {
            dbConnectors.mkdir();
        }
        //check active dir
        File activeDir = new File(getServletContext().getRealPath(DIR_BUNDLES + DIR_INACTIVE));
        if (!activeDir.exists()) {
            activeDir.mkdir();
        }
        //create dir for bundle
        File bundleDir = new File(getServletContext().getRealPath(DIR_BUNDLES + DIR_INACTIVE + "/" + bundleNameForDir));
        File driverFile = new File(getServletContext().getRealPath(DIR_BUNDLES + DIR_INACTIVE + "/" + bundleNameForDir + "/" + driverJarFileItem.getName()));
        File moduleFile = new File(getServletContext().getRealPath(DIR_BUNDLES + DIR_INACTIVE + "/" + bundleNameForDir + "/" + moduleJarFileItem.getName()));
        //vytvorim dir , ak zlyha nemazem lebo nemma co
        if (!bundleDir.mkdir()) {
            throw new InstantiationException("\"" + bundleNameForDir + "\"" + MSG_BUNDLE_ALREADY_EXIST);
        }
        //vytvorim file na disku nak zlyha hadzem IO lebo musim mazat rekurzive
        if (!(driverFile.createNewFile() && moduleFile.createNewFile())) {
            throw new IOException(MSQ_COULD_NOT_CREATE_FILES);
        }
        //zapisem obsah file na disku ak zlyha hadzem IO lebo musim mazat rekurzive
        try {
            driverJarFileItem.write(driverFile);
            moduleJarFileItem.write(moduleFile);
        } catch (Exception e) {
            throw new IOException(MSG_COULD_NOT_GET_JARS_CONTENT_FILES + " > " + e.getMessage());
        }
    }

    private void saveBundleToDatabase(String bundleName, String mclClassName,
                                      String driverJarFileName, String moduleJarFileName)
            throws RPCServiceException {
        Connection connection = ServerUtil.createConnectionToServer();
        if (connection == null) {
            throw new RPCServiceException("Could not create connection to application database");
        }
        System.out.println("UploadServiceImpl.saveBundleToDatabase > mam connection na server databazu");

        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL_QUERY_INSERT_BUNDLE);

            long unixTimestamp = new Date().getTime() / 1000;

            // Set the values
            pstmt.setString(1, bundleName);
            pstmt.setLong(2, unixTimestamp);
            pstmt.setString(3, mclClassName);
            pstmt.setInt(4, 0);//inactive 0 / active 1
            pstmt.setString(5, driverJarFileName);
            pstmt.setString(6, moduleJarFileName);

            // Insert
            pstmt.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RPCServiceException("Could not store bundle to database SQL error: " + e.getMessage());
        }
    }

    /**
     * Ak nastane exp vola tuto metodu, vyhodi RPC EXP a plus zmaze files z disku ak sa daju
     *
     * @param deleteDir   ci ma zmazat dir alebo nie, nap ak exist dir exp tak nemazem co mam
     * @param dirToDelete
     * @param errorMsg
     * @throws RPCServiceException
     */
    private void onSaveError(boolean deleteDir, String dirToDelete, String errorMsg) throws RPCServiceException {
        //delete files
        if (deleteDir) {
            System.out.println("UploadServiceImpl.onSaveError > Mazem DIR:" + DIR_BUNDLES + DIR_INACTIVE + "/" + dirToDelete);
            File bundleDir = new File(getServletContext().getRealPath(DIR_BUNDLES + DIR_INACTIVE + "/" + dirToDelete));
            if (bundleDir.exists() && bundleDir.isDirectory()) {
                for (File c : bundleDir.listFiles()) {
                    c.delete();
                }
            }
            bundleDir.delete();
        }
        //EXP
        throw new RPCServiceException(errorMsg);
    }
}