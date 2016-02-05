package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("UploadService")
public interface UploadService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use UploadService.App.getInstance() to access static instance of UploadServiceAsync
     */
    public static class App {
        private static final UploadServiceAsync ourInstance = (UploadServiceAsync) GWT.create(UploadService.class);

        public static UploadServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Ulozi dva JAR subory do pozadovaneho priecinka a ostatne veci zapise do properties suboru,
     * napriklad ClassName na MCL potomka
     *
     * @param bundleName   meno pre priecinok na servery, klient overuje regex tvar
     * @param mclClassName meno triedy v module triedy MCL
     */
    void saveUploadBundleForm(String bundleName, String mclClassName) throws RPCServiceException;
}
