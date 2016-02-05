package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * LIMIT
 * [LIMIT {[offset,] row_count | row_count OFFSET offset}]
 * https://dev.mysql.com/doc/refman/5.0/en/select.html
 */
public class RuleLimit implements DatabaseMentorRule {

    public static final String RULE_KEY = "LIMIT";
    private static final String[] TAIL_KEYS = {"PROCEDURE", "INTO OUTFILE", "INTO DUMPFILE", "INTO", "FOR UPDATE", "LOCK IN SHARE MODE"};

    /**
     * aj s medzerou za RULE_KEY_ slovom
     */
    private String firstPart;
    /**
     * zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
     */
    private String lastPart;

    /**
     * deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma casti kt sa daju spustit
     */
    @Override
    public List<String> applyRule(String sqlSelectQuery) {
        List<String> parts = new ArrayList<String>();
        //vycistenie od riadkov, trim, a 1+medizer za jednu medzeru
        String sql = RuleUtils.clearSqlStatement(sqlSelectQuery);

        if (sql.toUpperCase().contains(RULE_KEY)) {
            //prva cast aj s LIMIT_ a medzerou _, preto 6
            int ruleIndex = sql.toUpperCase().indexOf(RULE_KEY);
            firstPart = sql.substring(0, ruleIndex + 6);
            //zvysok za LIMIT_
            String trash = sql.substring(firstPart.length(), sql.length());
            String limitArea = cutLimitAreaFromTrash(trash);
            // zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
            lastPart = trash.substring(limitArea.length());
            //vybudujem sql bez tohto pravidla
            buildSQLWithoutThisRule(parts, firstPart, lastPart);

            //VARIACIE zatial ziadne , proste bude len bez limitu
            //zistim o aky limit sa jedna
            if (checkOffsetForm(limitArea)) { //LIMIT 5 OFFSET 6
            } else if (checkSimpleForm(limitArea)) { //LIMIT 5
            } else {
            }
        }
        return parts;
    }

    /**
     * Vrati mi info o aky pravidlo sa jedna, je to pre DebuggerService
     *
     * @return DebuggerRuleInfo TOKEN a description
     */
    @Override
    public DebuggerRuleInfo getDebuggerRuleInfo() {
        return new DebuggerRuleInfo("LIMIT", "Divide SQL statement by clauses plus return statement without LIMIT.");
    }

    /**
     * Vysklada cisty selekt bez pravidla LIMIT a da do listu
     *
     * @param listForSQLStatments list do ktoreho mam naplnit SQL
     * @param firstPart           prva cas po RUKE_KEY_ aj s medzerou
     * @param lastPart            _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildSQLWithoutThisRule(List<String> listForSQLStatments, String firstPart, String lastPart) {
        listForSQLStatments.add(firstPart.substring(0, firstPart.length() - 7) + lastPart);
    }

    /**
     * Z tailu ziska cast ktora parti LIMIT klauzule na zaklade TAIL_KEYS, bez medzier na zaciatku a konci
     *
     * @param trash prichadza mi BEZ medzery na zaciatku po slove LIMIT_ a az dokonca po ;
     * @return cast kt patri LIMITu, bez medzier na zaciatku a konci
     */
    private String cutLimitAreaFromTrash(final String trash) {
        String tailBezBodkoCiarky = trash.substring(0, trash.length() - 1);
        for (String tailKey : TAIL_KEYS) {
            if (tailBezBodkoCiarky.contains(tailKey)) {
                int cutIndex = tailBezBodkoCiarky.indexOf(tailKey);
                tailBezBodkoCiarky = tailBezBodkoCiarky.substring(0, cutIndex);
            }
        }
        return tailBezBodkoCiarky.trim();
    }

    /**
     * zistim ci sa jedna bez ofsetovy zapis jednoduchy
     *
     * @param limitArea prichadza mi BEZ medzery na zaciatku a nakonci
     * @return TRUE ak je v limite len jedno cislo
     */
    private boolean checkSimpleForm(final String limitArea) {
        int firstSpace = limitArea.toUpperCase().indexOf(" ");
        return firstSpace == -1;
    }

    /**
     * zistim ci sa jedna o dlhy offset zapis LIMIT 5 OFFSET 6
     *
     * @param limitArea prichadza mi BEZ medzery na zaciatku a nakonci
     * @return TRUE ak sa jedna o offsett zapis
     */
    private boolean checkOffsetForm(final String limitArea) {
        int firstSpace = limitArea.toUpperCase().indexOf(" ");
        if (firstSpace == -1) {
            return false;
        }
        //+1 preto lebo musi byt zaciatok slova a nie medzera
        String limitStartTrash = limitArea.substring(firstSpace + 1);
        return limitStartTrash.toUpperCase().startsWith("OFFSET");
    }

    public static String[] ARRAY = {
            "SELECT * FROM table;",
            "SELECT a, COUNT(*) FROM bar ORDER BY NULL LIMIT 100 , 100 FOR UPDATE;",
            "SELECT a, COUNT(*) FROM bar ORDER BY NULL LIMIT 5 FOR UPDATE;",
            "SELECT a, COUNT(*) FROM bar ORDER BY NULL LIMIT 5 OFFSET 6 FOR UPDATE;",

            "SELECT * FROM table LIMIT AVG(col) FOR UPDATE;",
            "SELECT * FROM table LIMIT (SELECT AVG(col) FROM table GROUP BY col) FOR UPDATE;",
            "SELECT * FROM table LIMIT AVG(col) OFFSET 6 FOR UPDATE;",
            "SELECT * FROM table LIMIT (SELECT AVG(col) FROM table GROUP BY col) OFFSET 6 FOR UPDATE;",
    };
}
