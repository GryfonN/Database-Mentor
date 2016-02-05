package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 10.02.14
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public interface ProceduresServiceAsync {
    /**
     * Ziska mena procedur v db
     *
     * @return String[]
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getAllProceduresNames(AsyncCallback<String[]> async);

    /**
     * Ziska dvojice do gen formulara o procedure
     *
     * @param procedureName
     * @return Map<String, String>
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getProcedureDetails(String procedureName, AsyncCallback<Map<String, String>> async);

    /**
     * Vrati kod procedury
     *
     * @param procedureName
     * @return String
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getProcedureCode(String procedureName, AsyncCallback<String> async);

    /**
     * Vrati objekt z API kde je mapa argumentov
     *
     * @param procedurename
     * @return List<ProcedureArgInfo>
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getProcedureArguments(String procedurename, AsyncCallback<List<ProcedureArgInfo>> async);

    /**
     * Vrati vysledok z zavolania procedury
     *
     * @param procedureName
     * @param args          pole {@link sk.gryfonnlair.dissertation.dbmentor.server.dbconnector.api.gwtdto.ProcedureArgInfo}
     * @return ProcedureCallResult alebo null
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void callProcedure(String procedureName, ProcedureArgInfo[] args, AsyncCallback<ProcedureCallResult> async);
}
