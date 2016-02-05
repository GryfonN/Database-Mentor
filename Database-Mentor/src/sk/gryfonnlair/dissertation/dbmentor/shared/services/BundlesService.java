package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/28/14
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("BundlesService")
public interface BundlesService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use BundlesService.App.getInstance() to access static instance of BundlesServiceAsync
     */
    public static class App {
        private static final BundlesServiceAsync ourInstance = (BundlesServiceAsync) GWT.create(BundlesService.class);

        public static BundlesServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Na zaklade datumu z db vyselektuje pocetnovych bundlov nahratych do systemu
     *
     * @param lastCheckDate {@link long} datum z admin objektu
     * @return int pocet noviniek
     */
    int getNewBundlesCount(long lastCheckDate);

    /**
     * Vrati obsah active dir
     *
     * @return vzdy mapa hoc aj prazdna
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *          ak pruser, msg in
     */
    Map<Integer, String> getActiveBundles() throws RPCServiceException;

    /**
     * List bundlov z DB
     *
     * @return
     * @throws RPCServiceException
     */
    List<Bundle> getAllBundles() throws RPCServiceException;

    void moveBundle(int id, boolean active, String bundleName) throws RPCServiceException;

    void deleteBundle(int id, boolean active, String bundleName) throws RPCServiceException;
}
