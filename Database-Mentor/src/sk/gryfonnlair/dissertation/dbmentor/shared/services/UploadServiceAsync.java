package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UploadServiceAsync {

    /**
     * Ulozi dva JAR subory do pozadovaneho priecinka a ostatne veci zapise do properties suboru,
     * napriklad ClassName na MCL potomka
     *
     * @param bundleName   meno pre priecinok na servery, klient overuje regex tvar
     * @param mclClassName meno triedy v module triedy MCL
     */
    void saveUploadBundleForm(String bundleName, String mclClassName, AsyncCallback<Void> async);
}
