package sk.gryfonnlair.dissertation.dbmentor.bundle.rules;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/12/14
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RuleUtils {

    /**
     * <b>Pred pouzitim musim spravit check ci je slovo SELECT v TRASHI, a potom spravit
     * substring tak aby trash zacinal tym slovom SELECT.....</b>
     * <p/>
     * Spolieham sa v algoritme na to ze zaciatok je SELECT bez zatvorky pred nim, z
     * trashu vyberiem innner selekt, hladam zatvorku ), ak najdem spytam sa ci predosle
     * neobsahovalo zatvorku ( , ak ano idem hladam dalej, ak nie mam konec + kratim po )
     *
     * @param trash SELECT????.... bez medzery na zaciatku musi byt zaciatok innner selektu a boh vie co nakonci
     * @return iner selekt alebo NULL ak neni v trashi inner selekt
     */
    static String findInnerSelectFromTrash(final String trash) {
        int praveZatvorky = 0;
        int lastZatvorkaIndex = 0;

        while (true) {
            lastZatvorkaIndex = trash.indexOf(')', lastZatvorkaIndex + 1);//+1 lebo hladam dalej
            if (lastZatvorkaIndex == -1) {
                return null;
            } else {
                praveZatvorky++;
            }
            String stringSLavymi = trash.substring(0, lastZatvorkaIndex);
            //PRE lepsi split
            Object[] leftZ = ("PRE" + stringSLavymi).split("\\(");
            if (leftZ.length < praveZatvorky + 1) {//nasiel som posledny zatvorku selektu
                String innerSelect = trash.substring(0, lastZatvorkaIndex);
                return innerSelect;
            }
        }
    }

    /**
     * Vlozi do stringu namiesto sharpov iner selekt so zatvorkami
     *
     * @param sharpString  predpoklad kauzulArea s # znakmi po vybrati innerov
     * @param innerSelects list iner selektov
     * @return
     */
    static String replaceShaprWithInnerSelects(final String sharpString, final List<String> innerSelects) {
        //pre lepsi spil pre post
        int sharpNumber = ("PRE" + sharpString + "POST").split("#").length - 1;
        if (sharpNumber > innerSelects.size()) {
            System.err.println("RuleUtils.replaceShaprWithInnerSelects: sharpNumber > innerSelects.size()");
        }

        String result = sharpString;
        for (int i = 0; i < sharpNumber; i++) {
            result = result.replaceFirst("#", "(" + innerSelects.get(i) + ")");
        }
        return result;
    }

    /**
     * Nahradim kazdy inner znakom # = ( # )
     *
     * @param trashBezBodkoCiarky trash string
     * @param innerSelects        list kde odkladam najdee innner selekty
     * @return trash bez innerov ale s # znakmi
     */
    static String replaceInnerSelectsForSharp(final String trashBezBodkoCiarky, final List<String> innerSelects) {
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
                System.err.println("RuleGroupBy.replaceInnerSelectsForSharp > inner NUll");
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
     * vycistenie od riadkov, trim, a 1+medizer za jednu medzeru
     *
     * @param sqlInput
     * @return
     */
    static String clearSqlStatement(final String sqlInput) {
        return sqlInput.replaceAll("\n", " ").trim().replaceAll(" +", " ");
    }
}
