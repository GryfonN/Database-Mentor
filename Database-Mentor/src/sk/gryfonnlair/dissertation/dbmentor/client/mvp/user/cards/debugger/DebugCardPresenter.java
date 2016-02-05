package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger;

import com.google.gwt.user.client.ui.IsWidget;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.Presenter;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DebugCardPresenter extends Presenter {

    public interface DebugCardView extends IsWidget {

        void setPresenter(DebugCardPresenter debugCardPresenter);

        void resetView();

        /**
         * nasetuje css na result box hore plus da msg
         *
         * @param errorMsg ak NULL tak success
         */
        void showCodeSQLError(String errorMsg);

        /**
         * Skotroluje TextArea ci je tam len jeden Sql statement ukonceny bodkociarkou,
         * ta asi nieje potrebna ale nech je lebo sa mi nechce pozerat pravidla kazde jedno zase
         *
         * @return boolean ci mozem spustit
         */
        boolean checkSelectStatement();

        /**
         * Ulozi do pola v Viewe a vygeneruje list pod sql areou na check co chcem aplikovat
         *
         * @param debuggerRuleInfos
         */
        void setupDebuggerRuleInfos(DebuggerRuleInfo[] debuggerRuleInfos);

        /**
         * Ak neni Rules neni co robit taze
         * Povoli/zakaze run buton
         *
         * @param enabled
         */
        void enableRunButton(boolean enabled);

        /**
         * ukazem gryfProgressBar
         *
         * @param isVisible tocim sa ?
         */
        void showParseProgress(boolean isVisible);

        /**
         * Dostale som zajebistu mapu a vyskladam list vertical panel ?
         *
         * @param debuggerParseResponse zajebisty result
         */
        void setupPostParseList(DebuggerParseResponse debuggerParseResponse);

        /**
         * Na zaklade id zistim o kt riadok sa jedna a setup custom widget
         * treba check errorMsg ci ok vsetko
         *
         * @param id                custom widgetu pre simple sql
         * @param debuggerRunResult vytiahnem cas a MCL tabulku
         * @param errorMsg          ak error mam priestor
         */
        void setupSimpleResultFor(int id, DebuggerRunResult debuggerRunResult, String errorMsg);


    }

    /**
     * Z servera get pole pravidiel
     */
    void getDebuggerRuleInfos();

    /**
     * volam server parse
     *
     * @param sqlFromTextArea      uz osetreny text !
     * @param ruleNameInRightOrder pole tokenov z buttonov
     */
    void runParseSQL(String sqlFromTextArea, String[] ruleNameInRightOrder);

    /**
     * Call jednoducheho sql selektu aj id prikladam pre response si ulozim
     * v anonym ibjekte GWT callback objekte
     *
     * @param idWidget     id widgetu kde vykreslit response
     * @param sqlStatement sql co vykonat, z widgetu vytiahnem
     */
    void runSimpleSQLStatement(int idWidget, String sqlStatement);
}
