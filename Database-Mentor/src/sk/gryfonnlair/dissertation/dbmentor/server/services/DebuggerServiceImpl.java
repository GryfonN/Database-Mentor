package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseRequest;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.DebuggerService;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerServiceImpl extends RemoteServiceServlet implements DebuggerService {

    /**
     * Vrati pole pravidiel, nemo desc
     *
     * @return pravidla implementovane na servery v konkretnom MCL
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    @Override
    public DebuggerRuleInfo[] getActiveRules() throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());
        return mentorConnectionLair.getDebuggerActiveRules();
    }

    /**
     * na zaklade requestu rozsekam sql prikaz zabaleny v requeste a podla pravidiel v poradi
     *
     * @return vysledok za obaleny do objektu
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *          ak sa nieco posere
     */
    @Override
    public DebuggerParseResponse parseSqlStatement(DebuggerParseRequest debuggerParseRequest) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());

        DebuggerParseResponse response = new DebuggerParseResponse();
        response.setOriginSQL(debuggerParseRequest.getSqlStatement());
        response.setOriginRules(debuggerParseRequest.getChosenRules());

        try {
            Map<String, List<String>> result = mentorConnectionLair.parseDebuggerSQLStatement(debuggerParseRequest.getSqlStatement(), debuggerParseRequest.getChosenRules());
            response.setRuleSLQStm(result);
        } catch (Exception e) {
            System.err.println("DebuggerServiceImpl.parseSqlStatement > EXP pri parsovani: " + e.getMessage());
            throw new RPCServiceException(e.getMessage());
        }
        return response;
    }

    /**
     * Po rozparsovani tymto spustam jednotlive selekty v liste
     *
     * @param sqlSelect select po rozprasovani
     * @return DebuggerRunResult s casom
     */
    @Override
    public DebuggerRunResult runSimpleSelectSqlStatement(int widgetId, String sqlSelect) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());
        DebuggerRunResult result = null;

        try {
            result = mentorConnectionLair.runSimpleStatement(sqlSelect);
        } catch (Exception e) {
            throw new RPCServiceException(e.getMessage());
        }

        result.setWidgetId(widgetId);
        return result;
    }
}