package sk.gryfonnlair.dissertation.dbmentor.server.dbconnector.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;


/**
 * WHERE
 * [WHERE where_condition]
 * https://dev.mysql.com/doc/refman/5.0/en/select.html
 */
public class RuleWhere implements DatabaseMentorRule {

    public static final String RULE_KEY = "WHERE";
    private static final String[] TAIL_KEYS = {")", "GROUP BY", "HAVING", "ORDER BY", "LIMIT", "PROCEDURE", "INTO OUTFILE", "INTO DUMPFILE", "INTO", "FOR UPDATE", "LOCK IN SHARE MODE"};

    /**
     * aj s medzerou za RULE_KEY_ slovom
     */
    private String firstPart;
    /**
     * zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
     */
    private String lastPart;
    /**
     * jednotlive stavebne bloky pre variacie, bez medzie na zac. a kon.
     */
    private String[] wherePartsWithSharps;

    /**
     * pre Inerselecty array
     * prvy voje prvy vyde
     */
    private List<String> innerSelects = new ArrayList<String>();


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

        //check ci obsahuje WHERE
        if (sql.toUpperCase().contains(RULE_KEY)) {
            //prva cast aj s WHERE a medzerou _, preto 6
            int ruleIndex = sql.toUpperCase().indexOf(RULE_KEY);
            firstPart = sql.substring(0, ruleIndex + 6);
            //zvysok za WHERE_
            String trash = sql.substring(firstPart.length(), sql.length());
            //ziskanie area pre group by bez medzier na zac. a kon.
            String whereAreaWithSharps = cutWhereAreaFromTrash(trash);
            //rozsekanie variacii podla ciarky
            wherePartsWithSharps = splitWhereArea(whereAreaWithSharps);

            // zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
            String whereArea = RuleUtils.replaceShaprWithInnerSelects(whereAreaWithSharps, innerSelects);
            lastPart = trash.substring(whereArea.length());
            //vybudujem sql bez tohto pravidla
            buildSQLWithoutThisRule(parts, firstPart, lastPart);

            //VARIACIE

            //pokial nemam viac ako dve party pre WHERE tak nemam co riesit
            if (wherePartsWithSharps.length >= 2) {
                //do listu nasekam vsetky variacie
                buildAllVariations(parts, firstPart, wherePartsWithSharps, lastPart);
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
        return new DebuggerRuleInfo("WHERE", "Divide SQL statement by clauses plus return statement without WHERE.");
    }

    /**
     * Z tailu ziska cast ktora parti where klauzule na zaklade TAIL_KEYS, bez medzier na zaciatku a konci
     * a vysekam inner selekty za sharpy
     *
     * @param trash prichadza mi BEZ medzery na zaciatku a az dokonca po ;
     * @return cast ktora parti where klauzule, bez medzier na zaciatku a konci
     */
    private String cutWhereAreaFromTrash(String trash) {
        String trashBezBodkoCiarky = trash.substring(0, trash.length() - 1);

        String clearTrash = RuleUtils.replaceInnerSelectsForSharp(trashBezBodkoCiarky, innerSelects);

        for (String tailKey : TAIL_KEYS) {
            if (clearTrash.contains(tailKey)) {
                // ) pravidlo
                if (tailKey.equalsIgnoreCase(TAIL_KEYS[0])) {
                    int cutIndex = 0;

                    while (true) {
                        cutIndex = clearTrash.indexOf(tailKey, cutIndex + 1);
                        if (cutIndex == -1) {
                            break;
                        }
                        //pre + post aby lepsie delilo
                        String preArea = "PRE" + (clearTrash.substring(0, cutIndex)) + "POST";
                        String fullArea = "PRE" + (clearTrash.substring(0, cutIndex + 1)) + "POST";
                        //ak lava zatvorka konecne
                        if (preArea.split("\\(").length < (fullArea).split("\\)").length) {
                            clearTrash = clearTrash.substring(0, cutIndex);
                            break;
                        }
                    }
                } else {    //normal pravidla
                    int cutIndex = clearTrash.indexOf(tailKey);
                    clearTrash = clearTrash.substring(0, cutIndex);
                }
            }
        }
        return clearTrash.trim();
    }

    private String[] splitWhereArea(String whereAreaWithSharps) {
        String[] notCleaned = whereAreaWithSharps.split(" (?i)AND | (?i)OR ");
        //cistim trimujem
        for (int i = 0; i < notCleaned.length; i++) {
            notCleaned[i] = notCleaned[i].trim();
        }
        return notCleaned;
    }

    /**
     * Vysklada cisty selekt bez pravidla WHERE a da do listu
     *
     * @param listForSQLStatements list do ktoreho mam naplnit SQL
     * @param firstPart            prva cas po RUKE_KEY_ aj s medzerou
     * @param lastPart             _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildSQLWithoutThisRule(List<String> listForSQLStatements, String firstPart, String lastPart) {
        listForSQLStatements.add(firstPart.substring(0, firstPart.length() - 7) + lastPart);
    }

    /**
     * Naseka do listu variace
     *
     * @param listForSQLStatements list do ktoreho mam naplnit jednotlive variacie SQL
     * @param firstPart            prva cas po RULE_KEY_ aj s medzerou
     * @param wherePartsWithSharps casti ciste bez medzier=to co medzi ciarkami
     * @param lastPart             _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildAllVariations(List<String> listForSQLStatements, String firstPart, String[] wherePartsWithSharps, String lastPart) {
        int proceedSharp = 0;
        for (String groupByPart : wherePartsWithSharps) {
            String whereStm = "";
            if (groupByPart.contains("#")) {
                whereStm = groupByPart.replaceFirst("#", "(" + innerSelects.get(proceedSharp) + ")");
                proceedSharp++;
            } else {
                whereStm = groupByPart;
            }
            String sql = firstPart + whereStm + lastPart;
            listForSQLStatements.add(sql);
        }
    }

    public static String[] ARRAY = {
//            http://www.w3schools.com/sql/sql_groupby.asp
            "SELECT * FROM Customers WHERE CustomerID=1;",
            "SELECT t1.name, t2.salary FROM employee AS t1, info AS t2 WHERE t1.name = t2.name;",
            "SELECT col_name FROM tbl_name WHERE col_name > 0;",
            "SELECT * FROM ccr_news WHERE insert_date > 0 AND insert_date > 0;",
            "SELECT * FROM table WHERE myid IN (2, 16, 93,102);",

            "SELECT * FROM my_table \n" +
                    "WHERE pk_column >= \n" +
                    "(SELECT FLOOR( MAX(pk_column) * RAND()) FROM my_table) \n" +
                    "ORDER BY pk_column\n" +
                    "LIMIT 1;",

            "SELECT * FROM my_table \n" +
                    "WHERE pk_column >= \n" +
                    "(SELECT FLOOR( MAX(pk_column) * RAND()) FROM my_table GROUP BY peto) AND auto=5\n" +
                    "ORDER BY pk_column;",

            "SELECT * FROM my_table \n" +
                    "WHERE pk_column >= \n" +
                    "(SELECT FLOOR( MAX(pk_column) * RAND()) FROM my_table GROUP BY peto) AND auto=5 oR insert_date > 0 Or myid IN (2, 16, 93,102)\n" +
                    "ORDER BY pk_column;",

            ""
    };
}
