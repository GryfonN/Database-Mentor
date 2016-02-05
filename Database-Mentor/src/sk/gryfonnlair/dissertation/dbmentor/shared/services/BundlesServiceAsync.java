package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.Bundle;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/28/14
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BundlesServiceAsync {
    /**
     * Na zaklade datumu z db vyselektuje pocetnovych bundlov nahratych do systemu
     *
     * @param lastCheckDate {@link long} datum z admin objektu
     * @return int pocet noviniek
     */
    void getNewBundlesCount(long lastCheckDate, AsyncCallback<Integer> async);

    /**
     * Vrati obsah active dir
     *
     * @return vzdy mapa hoc aj prazdna
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *          ak pruser, msg in
     */
    void getActiveBundles(AsyncCallback<Map<Integer, String>> async);

    /**
     * List bundlov z DB
     *
     * @return
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getAllBundles(AsyncCallback<List<Bundle>> async);

    void moveBundle(int id, boolean active, String bundleName, AsyncCallback<Void> async);

    void deleteBundle(int id, boolean active, String bundleName, AsyncCallback<Void> async);
}
