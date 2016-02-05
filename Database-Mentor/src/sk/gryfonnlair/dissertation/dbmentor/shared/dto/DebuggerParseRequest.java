package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;

/**
 * request pre rozobratie sql prikazu podla pravidiel
 */
public class DebuggerParseRequest implements IsSerializable {

    private String sqlStatement;
    private DebuggerRuleInfo[] chosenRules;

    public DebuggerParseRequest() {
    }

    public DebuggerParseRequest(String sqlStatement, DebuggerRuleInfo[] chosenRules) {
        this.sqlStatement = sqlStatement;
        this.chosenRules = chosenRules;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public DebuggerRuleInfo[] getChosenRules() {
        return chosenRules;
    }

    public void setChosenRules(DebuggerRuleInfo[] chosenRules) {
        this.chosenRules = chosenRules;
    }
}
