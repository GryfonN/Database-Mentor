package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/13/14
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerParseResponse implements IsSerializable {

    private String originSQL;
    private DebuggerRuleInfo[] originRules;
    /**
     * <RULE, list sqls k nemu> poradie je urcene polom pravidiel k name tvoria kluce
     */
    private Map<String, List<String>> ruleSLQStm;

    public DebuggerParseResponse() {
    }

    public DebuggerParseResponse(String originSQL, DebuggerRuleInfo[] originRules, Map<String, List<String>> ruleSLQStm) {
        this.originSQL = originSQL;
        this.originRules = originRules;
        this.ruleSLQStm = ruleSLQStm;
    }

    public String getOriginSQL() {
        return originSQL;
    }

    public void setOriginSQL(String originSQL) {
        this.originSQL = originSQL;
    }

    public DebuggerRuleInfo[] getOriginRules() {
        return originRules;
    }

    public void setOriginRules(DebuggerRuleInfo[] originRules) {
        this.originRules = originRules;
    }

    public Map<String, List<String>> getRuleSLQStm() {
        return ruleSLQStm;
    }

    public void setRuleSLQStm(Map<String, List<String>> ruleSLQStm) {
        this.ruleSLQStm = ruleSLQStm;
    }
}

