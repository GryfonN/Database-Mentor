package sk.gryfonnlair.dissertation.dbmentor.api.rule;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;

import java.util.List;

/**
 * Pravidlo pre debugger
 */
public interface DatabaseMentorRule {

    /**
     * Deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma casti kt sa daju spustit
     */
    List<String> applyRule(final String sqlSelectQuery);

    /**
     * Vrati mi info o aky pravidlo sa jedna, je to pre DebuggerService
     *
     * @return DebuggerRuleInfo TOKEN a description
     */
    DebuggerRuleInfo getDebuggerRuleInfo();
}
