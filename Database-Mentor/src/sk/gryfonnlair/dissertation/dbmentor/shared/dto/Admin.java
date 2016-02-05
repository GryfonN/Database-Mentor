package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 13.12.2013
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class Admin implements IsSerializable {

    private String name;
    private String password;
    private long lastLogged;

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

    public long getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(long lastLogged) {
        this.lastLogged = lastLogged;
    }
}
