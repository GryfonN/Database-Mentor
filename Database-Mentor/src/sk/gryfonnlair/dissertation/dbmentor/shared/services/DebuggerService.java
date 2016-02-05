package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseRequest;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("DebuggerService")
public interface DebuggerService extends RemoteService {
    /**
     * Utility/Convenience class.
     * Use DebuggerService.App.getInstance() to access static instance of DebuggerServiceAsync
     */
    public static class App {
        private static final DebuggerServiceAsync ourInstance = (DebuggerServiceAsync) GWT.create(DebuggerService.class);

        public static DebuggerServiceAsync getInstance() {
            return ourInstance;
        }
    }

    /**
     * Vrati pole pravidiel, nemo desc
     *
     * @return pravidla implementovane na servery v konkretnom MCL
     * @throws RPCServiceException
     */
    DebuggerRuleInfo[] getActiveRules() throws RPCServiceException;

    /**
     * na zaklade requestu rozsekam sql prikaz zabaleny v requeste a podla pravidiel v poradi
     *
     * @return vysledok za obaleny do objektu
     * @throws RPCServiceException ak sa nieco posere
     */
    DebuggerParseResponse parseSqlStatement(DebuggerParseRequest debuggerParseRequest) throws RPCServiceException;

    /**
     * Po rozparsovani tymto spustam jednotlive selekty v liste
     *
     * @param widgetId  id widgetu
     * @param sqlSelect select po rozprasovani
     * @return DebuggerRunResult s casom
     */
    DebuggerRunResult runSimpleSelectSqlStatement(int widgetId, String sqlSelect) throws RPCServiceException;
}
