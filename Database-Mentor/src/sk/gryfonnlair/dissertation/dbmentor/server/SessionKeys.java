package sk.gryfonnlair.dissertation.dbmentor.server;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SessionKeys {

    //USER
    String SESSION_ATTRIBUTE_KEY_USER = "user";
    String SESSION_ATTRIBUTE_KEY_USER_LAIR = "user_connection_lair";
    String SESSION_ATTRIBUTE_KEY_USER_QUICKCODE_UPLOAD_FILE = "user_quickCode_uploadFile";
    //ADMIN
    String SESSION_ATTRIBUTE_KEY_ADMIN = "admin";
    String SESSION_ATTRIBUTE_KEY_ADMIN_DRIVER_FILEITEM = "admin_driver_fileitem";
    String SESSION_ATTRIBUTE_KEY_ADMIN_MODULE_FILEITEM = "admin_module_fileitem";
}
