package sk.gryfonnlair.dissertation.dbmentor.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseRequest;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DebuggerServiceAsync {

    /**
     * Vrati pole pravidiel, nemo desc
     *
     * @return pravidla implementovane na servery v konkretnom MCL
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    void getActiveRules(AsyncCallback<DebuggerRuleInfo[]> async);

    /**
     * na zaklade requestu rozsekam sql prikaz zabaleny v requeste a podla pravidiel v poradi
     *
     * @return vysledok za obaleny do objektu
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *          ak sa nieco posere
     */
    void parseSqlStatement(DebuggerParseRequest debuggerParseRequest, AsyncCallback<DebuggerParseResponse> async);

    /**
     * Po rozparsovani tymto spustam jednotlive selekty v liste
     *
     * @param widgetId  id widgetu
     * @param sqlSelect select po rozprasovani
     * @return DebuggerRunResult s casom
     */
    void runSimpleSelectSqlStatement(int widgetId, String sqlSelect, AsyncCallback<DebuggerRunResult> async);
}
