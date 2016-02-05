package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Vratim len vnutorne selecty
 */
public class RuleInnerSelect implements DatabaseMentorRule {

    public static final String RULE_KEY = "SELECT";

    /**
     * deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma casti kt sa daju spustit
     */
    @Override
    public List<String> applyRule(String sqlSelectQuery) {
        List<String> result = new ArrayList<String>();

        //vycistenie od riadkov, trim, a 1+medizer za jednu medzeru
        String sqlFull = RuleUtils.clearSqlStatement(sqlSelectQuery);
        //odstranim prvy SELECT_ aj s medzerov
        String trash = sqlFull.substring(7);
        //check ci obsahuje SELECT
        while (trash.toUpperCase().contains(RULE_KEY)) {
            //najde vnutorny SELECT
            final int indexInnerSelect = trash.toUpperCase().indexOf(RULE_KEY);
            //odrezem zo trash vsetko pred innerom
            trash = trash.substring(indexInnerSelect);
            //a idem hladat konec innerSelectu ale bez zatvorky
            final String innerSelect = RuleUtils.findInnerSelectFromTrash(trash);
            //ak je chyba, nemala by byt, vratim prazdny
            if (innerSelect == null) {
                System.err.println("RuleInnerSelect.applyRule > null iner selekt");
                return new ArrayList<String>();
            }
            result.add(innerSelect + ";");
            //a skratim trash o inner selekt +1 za zatvorku a dalej hladam dasie inner selekty = jedna uroven len
            trash = trash.substring(innerSelect.length() + 1);
        }
        return result;
    }

    /**
     * Vrati mi info o aky pravidlo sa jedna, je to pre DebuggerService
     *
     * @return DebuggerRuleInfo TOKEN a description
     */
    @Override
    public DebuggerRuleInfo getDebuggerRuleInfo() {
        return new DebuggerRuleInfo("INNER", "Divide SQL statement by each inner SELECT statement.");
    }

    public static String[] ARRAY = {
            "SELECT DATE(`date`) AS `date` , COUNT(`player_name`) AS `player_count` " + "FROM " +
                    "(SELECT MIN(`date`) AS `date`, `player_name` FROM `player_playtime` GROUP BY `player_name`) " +
                    "AS t GROUP BY DATE( `date`) DESC LIMIT 60 ;",

            "SELECT t.date , COUNT(*) AS player_count FROM " +
                    "(SELECT DATE(MIN(`date`)) AS date FROM player_playtime` GROUP BY player_name) " +
                    "AS t GROUP BY t.date DESC LIMIT 60;",

            "SELECT name FROM world\n" +
                    "  WHERE population >\n" +
                    "     (SELECT population FROM world\n" +
                    "      WHERE name='Russia');",

            "SELECT title, slug, summary, id as current_id, " +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1) AS prev_data, " +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id > current_id ORDER BY id ASC LIMIT 1) AS next_data " +
                    "FROM table WHERE id IN (2,3,4);"
    };
}
