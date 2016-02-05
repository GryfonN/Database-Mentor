package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.util.ArrayList;
import java.util.List;

/**
 * ORDER BY
 * [ORDER BY {col_name | expr | position} [ASC | DESC], ...]
 * http://dev.mysql.com/doc/refman/5.0/en/order-by-optimization.html
 * <p/>
 * Vratim BEZ, ak<2 tak alternativy kazdu osobitne
 */
public class RuleOrderBy implements DatabaseMentorRule {

    public static final String RULE_KEY = "ORDER BY";
    private static final String[] TAIL_KEYS = {")", "LIMIT", "PROCEDURE", "INTO OUTFILE", "INTO DUMPFILE", "INTO", "FOR UPDATE", "LOCK IN SHARE MODE"};

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
    private String[] orderByPartsWithSharps;

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
    public List<String> applyRule(final String sqlSelectQuery) {
        List<String> parts = new ArrayList<String>();
        //vycistenie od riadkov, trim, a 1+medizer za jednu medzeru
        String sql = RuleUtils.clearSqlStatement(sqlSelectQuery);

        //check ci obsahuje ORDER BY
        if (sql.toUpperCase().contains(RULE_KEY)) {
            //prva cast aj s ORDER BY a medzerou _, preto 9
            int ruleIndex = sql.toUpperCase().indexOf(RULE_KEY);
            firstPart = sql.substring(0, ruleIndex + 9);
            //zvysok za ORDER BY_
            String trash = sql.substring(firstPart.length(), sql.length());
            //ziskanie area pre orderby bez medzier na zac. a kon.
            String orderByAreaWithSharps = cutOrderByAreaFromTrash(trash);
            //rozsekanie variacii podla ciarky
            orderByPartsWithSharps = splitOrderByArea(orderByAreaWithSharps);

            // zvysok pre tvorbu variacii aj s medzerou na zciatku _??????
            String orderByArea = RuleUtils.replaceShaprWithInnerSelects(orderByAreaWithSharps, innerSelects);
            lastPart = trash.substring(orderByArea.length());
            //vybudujem sql bez tohto pravidla
            buildSQLWithoutThisRule(parts, firstPart, lastPart);

            //VARIACIE

            //pokial nemam viac ako dve party pre ORDER BY tak nemam co riesit
            if (orderByPartsWithSharps.length >= 2) {
                //do listu nasekam vsetky variacie
                buildAllVariations(parts, firstPart, orderByPartsWithSharps, lastPart);
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
        return new DebuggerRuleInfo("ORDER BY", "Divide SQL statement by clauses plus return statement without ORDER BY.");
    }

    /**
     * Z tailu ziska cast ktora parti order by klauzule na zaklade TAIL_KEYS, bez medzier na zaciatku a konci
     * a vysekam inner selekty
     *
     * @param trash prichadza mi BEZ medzery na zaciatku a az dokonca po ;
     * @return cast ktora parti order by klauzule, bez medzier na zaciatku a konci
     */
    private String cutOrderByAreaFromTrash(final String trash) {
        String trashBezBodkoCiarky = trash.substring(0, trash.length() - 1);

        String clearTrash = replaceInnerSelects(trashBezBodkoCiarky);

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
     * Nahradim kazdy inner znakom # = ( # )
     *
     * @param trashBezBodkoCiarky
     * @return trash bez innerov ale s # znakmi
     */
    private String replaceInnerSelects(final String trashBezBodkoCiarky) {
        innerSelects.clear();
        String resultString = "";
        String workingString = trashBezBodkoCiarky;
        while (workingString.toUpperCase().contains("SELECT")) {
            //najde vnutorny SELECT
            final int indexInnerSelect = workingString.toUpperCase().indexOf("SELECT");
            resultString = resultString + workingString.substring(0, indexInnerSelect - 1);
            //odrezem zo trash vsetko pred innerom
            workingString = workingString.substring(indexInnerSelect);
            //a idem hladat konec innerSelectu ale bez zatvorky
            final String innerSelect = RuleUtils.findInnerSelectFromTrash(workingString);
            if (innerSelect == null) {
                System.err.println("RuleOrderBy.replaceInnerSelectsForSharp > inner NUll");
                return "";
            }
            innerSelects.add(innerSelect);
            //a skratim workingString o inner selekt +1 za zatvorku a dalej hladam dasie inner selekty = jedna uroven len
            workingString = workingString.substring(innerSelect.length() + 1);
            //nahradim vo vysledku inner za #
            resultString = resultString + "#";
        }
        //ak ostal zvysok
        resultString = resultString + workingString;
        return resultString;
    }

    /**
     * rozsekanie variacii podla ciarky, a zavola nad nimi trim
     *
     * @param orderByArea
     * @return String[] ciste bez medzier
     */
    private String[] splitOrderByArea(final String orderByArea) {
        String[] notCleaned = orderByArea.split(",");
        //cistim trimujem
        for (int i = 0; i < notCleaned.length; i++) {
            notCleaned[i] = notCleaned[i].trim();
        }
        return notCleaned;
    }

    /**
     * Vysklada cisty selekt bez pravidla ORDER BY a da do listu
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
     * @param firstPart           prva cas po RUKE_KEY_ aj s medzerou
     * @param orderByParts        casti ciste bez medzier=to co medzi ciarkami
     * @param lastPart            _?????? zvysok pre tvorbu variacii aj s medzerou na zciatku
     */
    private void buildAllVariations(List<String> listForSQLStatments, String firstPart, String[] orderByParts, String lastPart) {
        int proceedSharp = 0;
        for (String orderByPart : orderByParts) {
            String whereStm = "";
            if (orderByPart.contains("#")) {
                whereStm = orderByPart.replaceFirst("#", "(" + innerSelects.get(proceedSharp) + ")");
                proceedSharp++;
            } else {
                whereStm = orderByPart;
            }
            String sql = firstPart + whereStm + lastPart;
            listForSQLStatments.add(sql);
        }
    }

    public static String[] ARRAY = {
            "SELECT * FROM table;",
            "SELECT * FROM t1 ORDER BY key1, key2;",
            "SELECT * FROM t1 WHERE key2=constant ORDER BY key_part2;",
            "SELECT * FROM t1 ORDER BY key_part1 DESC, key_part2 ASC;",
            "SELECT * FROM t1 ORDER BY ABS(key);",
            "SELECT ABS(a) AS a FROM t1 ORDER BY a;",
            "SELECT a, COUNT(*) FROM bar GROUP BY a ORDER BY NULL;",
            "SELECT a, COUNT(*) FROM bar GROUP BY a ORDER BY NULL LIMIT 100,100 FOR UPDATE;",
            "SELECT a, COUNT(*) FROM bar GROUP BY a ORDER BY NULL FOR UPDATE;",

            "SELECT title, slug, summary, id as current_id, " +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id) AS prev_data, " +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id > current_id ORDER BY id ASC LIMIT 1) AS next_data " +
                    "FROM table WHERE id IN (2,3,4);",

            "SELECT * FROM table WHERE id IN (2,3,4) order by auto LIMIT (SELECT AVG(auto) FROM table GROUP by name);",
//            zapeklyty inner select
            "SELECT * FROM table ORDER BY (SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1);",
//            2x inner selekt v order by
            "SELECT * FROM table ORDER BY (SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1),(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1);",
//            iner norm inner
            "SELECT * FROM table ORDER BY " +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1)," +
                    "column," +
                    "(SELECT CONCAT_WS(',' id, title, slug) FROM table WHERE id < current_id ORDER BY id DESC,title ASC LIMIT 1);"

    };
}
