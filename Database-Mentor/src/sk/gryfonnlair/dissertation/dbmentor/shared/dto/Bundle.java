package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/8/14
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bundle implements IsSerializable {

    private int id;
    private String bundleName;
    private String uploadDate;
    private String mclClassName;
    private boolean active;
    private String driverFileName;
    private String moduleFileName;

    public Bundle(){
    }

    public Bundle(int id, String bundleName, String uploadDate, String mclClassName, boolean active, String driverFileName, String moduleFileName) {
        this.id = id;
        this.bundleName = bundleName;
        this.uploadDate = uploadDate;
        this.mclClassName = mclClassName;
        this.active = active;
        this.driverFileName = driverFileName;
        this.moduleFileName = moduleFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMclClassName() {
        return mclClassName;
    }

    public void setMclClassName(String mclClassName) {
        this.mclClassName = mclClassName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDriverFileName() {
        return driverFileName;
    }

    public void setDriverFileName(String driverFileName) {
        this.driverFileName = driverFileName;
    }

    public String getModuleFileName() {
        return moduleFileName;
    }

    public void setModuleFileName(String moduleFileName) {
        this.moduleFileName = moduleFileName;
    }
}
