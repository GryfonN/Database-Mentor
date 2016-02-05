package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 13.12.2013
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class User implements IsSerializable {

    private String dbTypeToken;
    private String dbTypeBundleName;
    private String name;
    private String password;
    private String connectionURL;
    private String db;

    public String getDbTypeToken() {
        return dbTypeToken;
    }

    public void setDbTypeToken(String dbTypeToken) {
        this.dbTypeToken = dbTypeToken;
    }

    public String getDbTypeBundleName() {
        return dbTypeBundleName;
    }

    public void setDbTypeBundleName(String dbTypeBundleName) {
        this.dbTypeBundleName = dbTypeBundleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
