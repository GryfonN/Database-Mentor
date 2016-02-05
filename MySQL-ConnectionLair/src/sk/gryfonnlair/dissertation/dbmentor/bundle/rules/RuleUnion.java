package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * UNION
 * <p/>
 * SELECT ...
 * UNION [ALL | DISTINCT] SELECT ...
 * [UNION [ALL | DISTINCT] SELECT ...]
 * <p/>
 * http://dev.mysql.com/doc/refman/5.0/en/union.html
 * <br/>
 * http://www.w3schools.com/sql/sql_union.asp
 * <br/>
 * vrati jednotlive selekty spojene UNIONom
 */
public class RuleUnion implements DatabaseMentorRule {

    public static final String RULE_KEY = "UNION";

    /**
     * deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma
     * casti kt sa daju spustit
     */
    private static final String[] TAIL_KEYS = {"WHERE", "GROUP BY", "HAVING", "ORDER BY", "LIMIT", "PROCEDURE"};

    /**
     * deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma casti kt sa daju spustit
     */
    @Override
    public List<String> applyRule(String sqlSelectQuery) {
        List<String> list = new ArrayList<String>();
        String sql = RuleUtils.clearSqlStatement(sqlSelectQuery);
        //check ci obsahuje UNION
        if (sql.toUpperCase().contains(RULE_KEY)) {
            String[] parts = sql.split("(?i)UNION|(?i)UNION ALL|(?i)UNION DISTINCT");

            // odsekne ;
            String last = parts[parts.length - 1];
            if (last.contains(";")) {
                last = last.substring(0, last.length() - 1);
            }

            // vsetky casti okrem poslednej prida do listu a prida ;
            for (int i = 0; i < parts.length - 1; i++) {
                list.add(parts[i].trim() + ";");
            }

            // posledna cast sa osekava ak treba
            for (String key : TAIL_KEYS) {
                if (last.contains(") " + key) || last.contains(")" + key)) {
                    last = last.substring(0, last.indexOf(key));
                }
            }

            // prida modifikovanu poslednu cast alebo prida ak netreba modifokovat
            list.add(last.trim() + ";");
        }
        return list;
    }

    /**
     * Vrati mi info o aky pravidlo sa jedna, je to pre DebuggerService
     *
     * @return DebuggerRuleInfo TOKEN a description
     */
    @Override
    public DebuggerRuleInfo getDebuggerRuleInfo() {
        return new DebuggerRuleInfo("UNION", "Return separate SELECT statements between UNION");
    }

    public static String[] ARRAY = {
            " (SELECT a FROM t1 WHERE a=10 AND B=1) UNION (SELECT a FROM t2 WHERE a=11 AND B=2 order by col2) ORDER BY a LIMIT 10;",
            "SELECT a FROM t1 WHERE a=10 AND B=1 UNION (SELECT a FROM t2 WHERE a=11 AND B=2 order by col2) ORDER BY a LIMIT 10;",
            "SELECT a FROM t1 WHERE a=10 AND B=1 UNION SELECT a FROM t2 WHERE a=11 AND B=2 order by col2 ORDER BY a LIMIT 10;",
            " SELECT a FROM t1 WHERE a=10 AND B=1 UNION SELECT a FROM t2 WHERE a=11 AND B=2 order by col2;",
            "(SELECT 1 AS sort_col, col1a, col1b, ... FROM t1) UNION (SELECT 2, col2a, col2b, ... FROM t2) ORDER BY sort_col, col1a;",
            "(SELECT a FROM t1 WHERE a=10 AND B=1 ORDER BY a LIMIT 10) UNION (SELECT a FROM t2 WHERE a=11 AND B=2 ORDER BY a LIMIT 10);",
            "(SELECT a FROM t1 WHERE a=10 AND B=1 ORDER BY a LIMIT 10)\n"
                    + "UNION\n"
                    + "(SELECT a FROM t2 WHERE a=11 AND B=2 ORDER BY a LIMIT 10);",

            " (SELECT a FROM t1 WHERE a=10 AND B=1) Union (SELECT a FROM t2 WHERE a=11 AND B=2 order by col2) ORDER BY a LIMIT 10;",
            "(SELECT a FROM t1 WHERE a=10 AND B=1) Union (SELECT a FROM t2 WHERE a=11 AND B=2 order by col2) Union (SELECT a FROM t2 WHERE a=11 AND B=2 order by col2);"
    };
}
