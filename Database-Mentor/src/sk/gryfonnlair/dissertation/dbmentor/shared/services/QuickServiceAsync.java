package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/11/14
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QuickServiceAsync {
    /**
     * Vykona SQL kt je v stringu
     *
     * @param sqlStatement
     * @return QuickCodeCallResult
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void callSQLStatement(String sqlStatement, AsyncCallback<QuickCodeCallResult> async);

    /**
     * Pole stringov pre qucik code do karty
     *
     * @return String[] alebo null
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getSQLSyntax(AsyncCallback<String[]> async);

    /**
     * Vrati posledny uploadnuty script prostrednictvom serveltu z session
     *
     * @return String sql kod
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getUploadedSQLScript(AsyncCallback<String> async);
}
