package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public interface FunctionsServiceAsync {

    /**
     * Vrati mena funkcii
     *
     * @return String[]
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getAllFunctionsNames(AsyncCallback<String[]> async);

    /**
     * Vrati List dvojic informacii pre generovanie formulara o info
     *
     * @param functionName
     * @return Map<String, String>
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getFunctionDetails(String functionName, AsyncCallback<Map<String, String>> async);

    /**
     * vrati code funkcie
     *
     * @param functionName
     * @return String
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getFunctionCode(String functionName, AsyncCallback<String> async);

    /**
     * Vrati objekt z API kde je mapa argumentov
     *
     * @param functionName
     * @return List<FunctionArgInfo>
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getFunctionArguments(String functionName, AsyncCallback<List<FunctionArgInfo>> async);

    /**
     * Vrati vysledok z zavolania funckie
     *
     * @param functionName
     * @param args         pole {@link sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo}
     * @return String alebo null, SQL NULL v service staram sa o to
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void callFunction(String functionName, FunctionArgInfo[] args, AsyncCallback<String> async);
}
