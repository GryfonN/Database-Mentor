package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * GROUB BY
 * [GROUP BY {col_name | expr | position} [ASC | DESC], ... [WITH ROLLUP]]
 * https://dev.mysql.com/doc/refman/5.0/en/select.html
 * Vratim BEZ, ak<2 tak alternativy kazdu osobitne
 */
public class RuleGroupBy implements DatabaseMentorRule {

    public static final String RULE_KEY = "GROUP BY";
    private static final String[] TAIL_KEYS = {")", "HAVING", "ORDER BY", "LIMIT", "PROCEDURE", "INTO OUTFILE", "INTO DUMPFILE", "INTO", "FOR UPDATE", "LOCK IN SHARE MODE"};

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
    private String[] groupByPartsWithSharps;

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

        //check ci obsahuje GROUP BY
        if (sql.toUpperCase().contains(RULE_KEY)) {
            //prva cast aj s GROUP BY a medzerou _, preto 9
            int ruleIndex = sql.toUpperCase().indexOf(RULE_KEY);
            firstPart = sql.substring(0, ruleIndex + 9);
            //zvysok za GROUP BY_
            String trash = sql.substring(firstPart.length(), sql.length());
            //ziskanie area pre group by bez medzier na zac. a kon.
            String groupByAreaWithSharps = cutGroupByAreaFromTrash(trash);
            //rozsekanie variacii podla ciarky
            groupByPartsWithSharps = splitGroupByArea(groupByAreaWithSharps);

            // zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
            String groupByArea = RuleUtils.replaceShaprWithInnerSelects(groupByAreaWithSharps, innerSelects);
            lastPart = trash.substring(groupByArea.length());
            //vybudujem sql bez tohto pravidla
            buildSQLWithoutThisRule(parts, firstPart, lastPart);

            //VARIACIE

            //pokial nemam viac ako dve party pre GROUP BY tak nemam co riesit
            if (groupByPartsWithSharps.length >= 2) {
                //do listu nasekam vsetky variacie
                buildAllVariations(parts, firstPart, groupByPartsWithSharps, lastPart);
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
        return new DebuggerRuleInfo("GROUP BY", "Divide SQL statement by clauses plus return statement without GROUP BY.");
    }

    /**
     * Z tailu ziska cast ktora parti group by klauzule na zaklade TAIL_KEYS, bez medzier na zaciatku a konci
     * a vysekam inner selekty
     *
     * @param trash prichadza mi BEZ medzery na zaciatku a az dokonca po ;
     * @return cast ktora parti group by klauzule, bez medzier na zaciatku a konci
     */
    private String cutGroupByAreaFromTrash(final String trash) {
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
                        if (preArea.split("\\(").length < (fullArea).split("\\)").length) { //ak lava zatvorka konecne
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

    /**
     * rozsekanie variacii podla ciarky, a zavola nad nimi trim
     *
     * @param groupByArea
     * @return String[] ciste bez medzier
     */
    private String[] splitGroupByArea(final String groupByArea) {
        String[] notCleaned = groupByArea.split(",");
        //cistim trimujem
        for (int i = 0; i < notCleaned.length; i++) {
            notCleaned[i] = notCleaned[i].trim();
        }
        return notCleaned;
    }

    /**
     * Vysklada cisty selekt bez pravidla GROUP BY a da do listu
     *
     * @param listForSQLStatments list do ktoreho mam naplnit SQL
     * @param firstPart           prva cas po RUKE_KEY_ aj s medzerou
     * @param lastPart            _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildSQLWithoutThisRule(List<String> listForSQLStatments, String firstPart, String lastPart) {
        listForSQLStatments.add(firstPart.substring(0, firstPart.length() - 10) + lastPart);
    }

    /**
     * Naseka do listu variace
     *
     * @param listForSQLStatments list do ktoreho mam naplnit jednotlive variacie SQL
     * @param firstPart           prva cas po RULE_KEY_ aj s medzerou
     * @param groupByParts        casti ciste bez medzier=to co medzi ciarkami
     * @param lastPart            _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildAllVariations(List<String> listForSQLStatments, String firstPart, String[] groupByParts, String lastPart) {
        int proceedSharp = 0;
        for (String groupByPart : groupByParts) {
            String whereStm = "";
            if (groupByPart.contains("#")) {
                whereStm = groupByPart.replaceFirst("#", "(" + innerSelects.get(proceedSharp) + ")");
                proceedSharp++;
            } else {
                whereStm = groupByPart;
            }
            String sql = firstPart + whereStm + lastPart;
            listForSQLStatments.add(sql);
        }
    }

    public static String[] ARRAY = {
//            http://www.w3schools.com/sql/sql_groupby.asp
            "SELECT Shippers.ShipperName,COUNT(Orders.OrderID) AS NumberOfOrders FROM Orders\n" +
                    " LEFT JOIN Shippers\n" +
                    " ON Orders.ShipperID=Shippers.ShipperID\n" +
                    " GROUP BY ShipperName;",
            "SELECT Shippers.ShipperName, Employees.LastName,\n" +
                    " COUNT(Orders.OrderID) AS NumberOfOrders\n" +
                    " FROM ((Orders\n" +
                    " INNER JOIN Shippers\n" +
                    " ON Orders.ShipperID=Shippers.ShipperID)\n" +
                    " INNER JOIN Employees\n" +
                    " ON Orders.EmployeeID=Employees.EmployeeID)\n" +
                    " GROUP BY ShipperName,LastName;"
    };
}
