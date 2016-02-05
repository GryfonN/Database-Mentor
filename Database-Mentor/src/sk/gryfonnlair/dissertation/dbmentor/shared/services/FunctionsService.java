package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("FunctionsService")
public interface FunctionsService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use FunctionsService.App.getInstance() to access static instance of FunctionsServiceAsync
     */
    public static class App {
        private static final FunctionsServiceAsync ourInstance = (FunctionsServiceAsync) GWT.create(FunctionsService.class);

        public static FunctionsServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Vrati mena funkcii
     *
     * @return String[]
     * @throws RPCServiceException
     */
    String[] getAllFunctionsNames() throws RPCServiceException;

    /**
     * Vrati List dvojic informacii pre generovanie formulara o info
     *
     * @param functionName
     * @return Map<String, String>
     * @throws RPCServiceException
     */
    Map<String, String> getFunctionDetails(String functionName) throws RPCServiceException;

    /**
     * vrati code funkcie
     *
     * @param functionName
     * @return String
     * @throws RPCServiceException
     */
    String getFunctionCode(String functionName) throws RPCServiceException;

    /**
     * Vrati objekt z API kde je mapa argumentov
     *
     * @param functionName
     * @return List<FunctionArgInfo>
     * @throws RPCServiceException
     */
    List<FunctionArgInfo> getFunctionArguments(String functionName) throws RPCServiceException;

    /**
     * Vrati vysledok z zavolania funckie
     *
     * @param functionName
     * @param args         pole {@link FunctionArgInfo}
     * @return String alebo null, SQL NULL v service staram sa o to
     * @throws RPCServiceException
     */
    String callFunction(String functionName, FunctionArgInfo[] args) throws RPCServiceException;
}
