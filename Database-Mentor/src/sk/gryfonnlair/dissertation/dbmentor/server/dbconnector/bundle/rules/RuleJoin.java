package sk.gryfonnlair.dissertation.dbmentor.server.dbconnector.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * JOIN
 * <p/>
 * table_reference [INNER | CROSS] JOIN table_factor [join_condition]
 * | table_reference STRAIGHT_JOIN table_factor
 * | table_reference STRAIGHT_JOIN table_factor ON conditional_expr
 * | table_reference {LEFT|RIGHT} [OUTER] JOIN table_reference join_condition
 * | table_reference NATURAL [{LEFT|RIGHT} [OUTER]] JOIN table_factor
 * <p/>
 * Detekujem aky join a vratim alternativy
 */
public class RuleJoin implements DatabaseMentorRule {

    public static final String RULE_KEY = "JOIN";
    private static final String JOIN_VARIATIONS[] = {"INNER JOIN", "LEFT OUTER JOIN", "RIGHT OUTER JOIN"};

    /**
     * List medzi sql tela medzi joinami na variacie
     */
    private List<String> sqlBlocks = new ArrayList<String>();
    /**
     * zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
     */
    private String lastPart;
    /**
     * list typpov joinov za sqlPartami
     * 0 join | 1 left | 2 Right
     */
    private List<Integer> joinTypeBlocks = new ArrayList<Integer>();


    /**
     * deli podla pravidla sql prikaz a vracia list jeho vykonatelnych alt casti
     *
     * @param sqlSelectQuery
     * @return List stringov prazdny ak sa neda aplikovat pravidlo, inak ma casti kt sa daju spustit
     */
    @Override
    public List<String> applyRule(final String sqlSelectQuery) {
        //pre istotu cistim, v testoch mam viac stringov po sebe
        sqlBlocks.clear();
        joinTypeBlocks.clear();
        List<String> parts = new ArrayList<String>();
        //vycistenie od riadkov, trim, a 1+medizer za jednu medzeru
        String workingSQL = RuleUtils.clearSqlStatement(sqlSelectQuery);

        //check ci obsahuje JOIN
        while (workingSQL.toUpperCase().contains(RULE_KEY)) {
            int indexOfJoin = workingSQL.indexOf(RULE_KEY);
            //najdem index JOIN a poslem do metody na najdenie jeho celeho tvaru
            String foundJOIN = whatFormOfJoin(workingSQL, indexOfJoin);
            //najdeny cely tvar odseknem a pridam part
            String part = workingSQL.substring(0, workingSQL.indexOf(foundJOIN));
            //skratim working string pre cyklus while
            workingSQL = workingSQL.substring(indexOfJoin + 4);
            //pridam part
            sqlBlocks.add(part);
        }

        lastPart = workingSQL;
        //mam stavebne bloky a zapnam magicku funkciu
        List<String> variations = makeVariations(sqlBlocks, joinTypeBlocks, lastPart);
        //nabucham variacie ak su do vysledku
        for (String s : variations) {
            parts.add(s);
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
        return new DebuggerRuleInfo("JOIN", "Generate variations for every type of JOIN (INNER JOIN | LEFT JOIN | IGHT JOIN).");
    }

    /**
     * Vratim co som nasiel aby som to neskor osekal z stringu, spoliham sa ze metoda plny pole typov
     *
     * @param sqlTrash
     * @param indexOfJoin
     * @return ak nic extra tak JOIN
     */
    private String whatFormOfJoin(final String sqlTrash, final int indexOfJoin) {
//        21 pred potrebujem max
        String preSQLTrash = sqlTrash.substring(
                indexOfJoin - 21 < 0 ? 0 : indexOfJoin - 21,
                indexOfJoin).toUpperCase();
        if (preSQLTrash.endsWith("_")) {
            joinTypeBlocks.add(0);
            return "STRAIGHT_JOIN";
        } else if (preSQLTrash.contains("INNER")) {
            joinTypeBlocks.add(0);
            return "INNER JOIN";
        } else if (preSQLTrash.contains("CROSS")) {
            joinTypeBlocks.add(0);
            return "CROSS JOIN";
        } else if (preSQLTrash.contains("LEFT")) {
            joinTypeBlocks.add(1);
            String returnString = "LEFT";
            if (preSQLTrash.contains("OUTER")) {
                returnString = returnString + " OUTER JOIN";
            } else {
                returnString = returnString + " JOIN";
            }
            if (preSQLTrash.contains("NATURAL")) {
                returnString = "NATURAL " + returnString;
            }
            return returnString;
        } else if (preSQLTrash.contains("RIGHT")) {
            joinTypeBlocks.add(2);
            String returnString = "RIGHT";
            if (preSQLTrash.contains("OUTER")) {
                returnString = returnString + " OUTER JOIN";
            } else {
                returnString = returnString + " JOIN";
            }
            if (preSQLTrash.contains("NATURAL")) {
                returnString = "NATURAL " + returnString;
            }
            return returnString;
        }
        //ak nenajdem nic exte
        joinTypeBlocks.add(0);
        return "JOIN";
    }

    private List<String> makeVariations(List<String> sqlBlocks, List<Integer> joinTypeBlocks, String lastPart) {
        List<String> completeSQLs = new ArrayList<String>();
        //ak mam nejake bloky
        if (!sqlBlocks.isEmpty() && !joinTypeBlocks.isEmpty()) {
            //list variacii
            List<String> listOfVariations = new ArrayList<String>();
            //iterujem bloky
            for (int i = 0; i < joinTypeBlocks.size(); i++) {
                //ak sa jedna o prvy tak pevne setuje zaciatok variacii
                if (i == 0) {
                    listOfVariations = recursionByType(sqlBlocks.get(i), joinTypeBlocks.get(i));
                }
                //ak sa jenda o dalsie tak k variacia bucham variacie
                else {
                    List<String> temp = new ArrayList<String>();
                    //prechadzam aktualnevariacie a vytvaram na nich strom dalsie variacie
                    for (String s : listOfVariations) {
                        for (String v : recursionByType(sqlBlocks.get(i), joinTypeBlocks.get(i))) {
                            temp.add(s + v);
                        }
                    }
                    //spravil som variacie pre vsetko v list kt su v pomocnom liste a preto pre setujem a pokracuejm vALG
                    listOfVariations = temp;
                }
            }
            //pridam last block ku vsetkemu
            for (String variation : listOfVariations) {
                completeSQLs.add(variation + lastPart);
            }
        }
        return completeSQLs;
    }

    /**
     * Na sqlBlock vrati variacie s pripojeny jointype
     *
     * @param sqlBlock jeden sql block
     * @param joinType
     * @return vsetky variacie joinov za tym napojene
     */
    private List<String> recursionByType(final String sqlBlock, final int joinType) {
        List<String> result = new ArrayList<String>(4);
        for (int i = 0; i < JOIN_VARIATIONS.length; i++) {
//ak by som chcel len onstatne variacie JOINOV
//            if (joinType == i) continue;
            result.add(sqlBlock + JOIN_VARIATIONS[i]);
        }
        return result;
    }

    private List<String> rekursionByList(final List<String> firstList, final List<String> secondList) {
        List<String> result = new ArrayList<String>();
        for (String s1 : firstList) {
            for (String s2 : secondList) {
                result.add(s1 + s2);
            }
        }
        return result;
    }

    public static String[] ARRAY = {
//            ekvivalenty
            "SELECT * FROM t1 LEFT JOIN (t2, t3, t4) ON (t2.a=t1.a AND t3.b=t1.b AND t4.c=t1.c)",
            "SELECT * FROM t1 LEFT JOIN (t2 CROSS JOIN t3 CROSS JOIN t4) ON (t2.a=t1.a AND t3.b=t1.b AND t4.c=t1.c);",
            "SELECT t1.name, t2.salary FROM employee AS t1 INNER JOIN info AS t2 ON t1.name = t2.name;",
            "SELECT t1.name, t2.salary FROM employee t1 INNER JOIN info t2 ON t1.name = t2.name;",
            "SELECT left_tbl.* FROM left_tbl LEFT JOIN right_tbl ON left_tbl.id = right_tbl.id\n" +
                    "  WHERE right_tbl.id IS NULL;",

            "SELECT left_tbl.* FROM { OJ left_tbl LEFT OUTER JOIN right_tbl ON left_tbl.id = right_tbl.id }\n" +
                    "  WHERE right_tbl.id IS NULL;",

            "SELECT * FROM table1 LEFT JOIN table2 ON table1.id=table2.id\n" +
                    "  LEFT JOIN table3 ON table2.id=table3.id;",

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
