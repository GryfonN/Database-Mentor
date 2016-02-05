package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 10.02.14
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("ProceduresService")
public interface ProceduresService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use ProceduresService.App.getInstance() to access static instance of ProceduresServiceAsync
     */
    public static class App {
        private static final ProceduresServiceAsync ourInstance = (ProceduresServiceAsync) GWT.create(ProceduresService.class);

        public static ProceduresServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Ziska mena procedur v db
     *
     * @return String[]
     * @throws RPCServiceException
     */
    String[] getAllProceduresNames() throws RPCServiceException;

    /**
     * Ziska dvojice do gen formulara o procedure
     *
     * @param procedureName
     * @return Map<String, String>
     * @throws RPCServiceException
     */
    Map<String, String> getProcedureDetails(String procedureName) throws RPCServiceException;

    /**
     * Vrati kod procedury
     *
     * @param procedureName
     * @return String
     * @throws RPCServiceException
     */
    String getProcedureCode(String procedureName) throws RPCServiceException;

    /**
     * Vrati objekt z API kde je mapa argumentov
     *
     * @param procedurename
     * @return List<ProcedureArgInfo>
     * @throws RPCServiceException
     */
    List<ProcedureArgInfo> getProcedureArguments(String procedurename) throws RPCServiceException;

    /**
     * Vrati vysledok z zavolania procedury
     *
     * @param procedureName
     * @param args          pole {@link ProcedureArgInfo}
     * @return ProcedureCallResult alebo null
     * @throws RPCServiceException
     */
    ProcedureCallResult callProcedure(String procedureName, ProcedureArgInfo[] args) throws RPCServiceException;
}
