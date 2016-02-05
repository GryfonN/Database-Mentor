package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/11/14
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("QuickService")
public interface QuickService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use QuickService.App.getInstance() to access static instance of QuickServiceAsync
     */
    public static class App {
        private static final QuickServiceAsync ourInstance = (QuickServiceAsync) GWT.create(QuickService.class);

        public static QuickServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Vykona SQL kt je v stringu
     *
     * @param sqlStatement
     * @return QuickCodeCallResult
     * @throws RPCServiceException
     */
    QuickCodeCallResult callSQLStatement(String sqlStatement) throws RPCServiceException;

    /**
     * Pole stringov pre qucik code do karty
     *
     * @return String[] alebo null
     * @throws RPCServiceException
     */
    String[] getSQLSyntax() throws RPCServiceException;

    /**
     * Vrati posledny uploadnuty script prostrednictvom serveltu z session
     *
     * @return String sql kod
     * @throws RPCServiceException
     */
    String getUploadedSQLScript() throws RPCServiceException;
}
