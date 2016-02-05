package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.mastergaurav.codemirror.client.CodeEditor;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.DebuggerSelectedRuleButton;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.DebuggerSimpleStatement;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.ResultSetTable;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugCardViewImpl extends Composite implements DebugCardPresenter.DebugCardView {
    public static final String MSG_PLEASE_USE_SEMICOLON = "Please use semicolon at the end of SELECT statement";
    public static final String MSG_JUST_SELECT_STATEMENT = "Just SELECT statement is working for this debugger !";

    interface DebugCardViewImplUiBinder extends UiBinder<Widget, DebugCardViewImpl> {
    }

    private static DebugCardViewImplUiBinder ourUiBinder = GWT.create(DebugCardViewImplUiBinder.class);

    private DebugCardPresenter presenter;
    private List<DebuggerSimpleStatement> resultList = new ArrayList<DebuggerSimpleStatement>();
    private CodeEditor codeEditor;

    public DebugCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(DebugCardPresenter debugCardPresenter) {
        this.presenter = debugCardPresenter;
    }

    @Override
    public void resetView() {
        resultTextBox.setText("");
        resultTextBox.setVisible(false);
        runParsingButton.setEnabled(false);

        //presetovanie textu neslo a duplikovalo codemirror
        codeEditor = new CodeEditor(120);
        codeMirrorPanel.clear();
        codeMirrorPanel.add(codeEditor);

        rulesPanel.clear();
        selectedRulesPanel.clear();
        parseResultsPanel.clear();
    }

    @Override
    public void showCodeSQLError(String errorMsg) {
        if (errorMsg == null) {//SUCCESS
            resultTextBox.setVisible(false);
        } else {
            RootPanel.get("error_div").add(new Label(errorMsg));
            resultTextBox.setText(errorMsg);
            resultTextBox.setVisible(true);
        }
    }

    /**
     * Skotroluje TextArea ci je tam len jeden Sql statement ukonceny bodkociarkou,
     * ta asi nieje potrebna ale nech je lebo sa mi nechce pozerat pravidla kazde jedno zase
     */
    @Override
    public boolean checkSelectStatement() {
        String sqlStatement = codeEditor.getValue();
        if (!sqlStatement.trim().endsWith(";")) {
            this.showCodeSQLError(MSG_PLEASE_USE_SEMICOLON);
            return false;
        } else if (!sqlStatement.trim().toUpperCase().startsWith("SELECT")) {
            this.showCodeSQLError(MSG_JUST_SELECT_STATEMENT);
            return false;
        }
        return true;
    }

    /**
     * Ulozi do pola v Viewe a vygeneruje list pod sql areou na check co chcem aplikovat
     *
     * @param debuggerRuleInfos
     */
    @Override
    public void setupDebuggerRuleInfos(DebuggerRuleInfo[] debuggerRuleInfos) {
        rulesPanel.clear();
        for (final DebuggerRuleInfo dri : debuggerRuleInfos) {
            final Button ruleButton = new Button();
            ruleButton.setStyleName(style.ruleButtonDebugCard());
            ruleButton.addStyleName("btn");
            ruleButton.setText(dri.getName());
            ruleButton.setTitle(dri.getDescription());
            ruleButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    final DebuggerSelectedRuleButton selectedRuleButton = new DebuggerSelectedRuleButton();
                    selectedRuleButton.setText("  " + ruleButton.getText() + "  ❯");
                    selectedRuleButton.setRuleName(ruleButton.getText());
                    selectedRuleButton.setTitle("Remove rule from pipeline");
                    selectedRuleButton.setStyleName(style.selectedRuleButtonDebugCard());
                    selectedRuleButton.addStyleName("fui-cmd");
                    selectedRuleButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            selectedRuleButton.removeFromParent();
                        }
                    });
                    selectedRulesPanel.add(selectedRuleButton);
                }
            });
            rulesPanel.add(ruleButton);
        }
    }

    /**
     * Ak neni Rules neni co robit taze
     * Povoli/zakaze run buton
     *
     * @param enabled
     */
    @Override
    public void enableRunButton(boolean enabled) {
        runParsingButton.setEnabled(enabled);
    }

    /**
     * Pouzivam aj pre cakanie na RUles
     * ukazem gryfProgressBar
     *
     * @param isVisible tocim sa ?
     */
    @Override
    public void showParseProgress(boolean isVisible) {
        if (isVisible) {
            DatabaseMentor.gryfProgressPopupPanel.center();
        } else {
            DatabaseMentor.gryfProgressPopupPanel.hide();
        }
    }

    /**
     * Dostale som zajebistu mapu a vyskladam list vertical panel ?
     *
     * @param debuggerParseResponse zajebisty result
     */
    @Override
    public void setupPostParseList(DebuggerParseResponse debuggerParseResponse) {
        parseResultsPanel.clear();
        resultList.clear();
        //zacinam 1 lebo root ma 0 zabrate
        int resultListCounter = 1;

        FlowPanel wrapperRoot = new FlowPanel();
        wrapperRoot.addStyleName(style.resultWrapperDebugCard());
        wrapperRoot.add(new Label("ROOT sql statement:"));
        final DebuggerSimpleStatement rootStm = new DebuggerSimpleStatement(0, debuggerParseResponse.getOriginSQL());
        rootStm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.runSimpleSQLStatement(rootStm.getId(), rootStm.getCommand());
            }
        });
        wrapperRoot.add(rootStm);
        parseResultsPanel.add(wrapperRoot);
        resultList.add(rootStm);

        for (DebuggerRuleInfo s : debuggerParseResponse.getOriginRules()) {
            FlowPanel wrapper = new FlowPanel();
            wrapper.addStyleName(style.resultWrapperDebugCard());
            Label ruleName = new Label(s.getName());
            wrapper.add(ruleName);
            List<String> list = debuggerParseResponse.getRuleSLQStm().get(s.getName());
            for (int i = 0; i < list.size(); i++) {
                final DebuggerSimpleStatement stmbtn = new DebuggerSimpleStatement(resultListCounter, list.get(i));
                stmbtn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        presenter.runSimpleSQLStatement(stmbtn.getId(), stmbtn.getCommand());
                    }
                });
                resultList.add(resultListCounter, stmbtn);
                resultListCounter++;
                wrapper.add(stmbtn);
            }
            parseResultsPanel.add(wrapper);
        }
    }

    /**
     * Na zaklade id zistim o kt riadok sa jedna a setup custom widget
     * treba check errorMsg ci ok vsetko
     *
     * @param id                custom widgetu pre simple sql
     * @param debuggerRunResult vytiahnem cas a MCL tabulku
     * @param errorMsg          ak error mam priestor
     */
    @Override
    public void setupSimpleResultFor(int id, DebuggerRunResult debuggerRunResult, String errorMsg) {
        DebuggerSimpleStatement s = resultList.get(id);
        if (errorMsg == null) { //OK
            s.showResultTable(new ResultSetTable("Execute time in millis seconds: " + debuggerRunResult.getExecuteTime(), debuggerRunResult.getTable()));
        } else { //ERROR
            s.showErrorMsg(errorMsg);
        }
    }

    @UiField
    VerticalPanel rulesPanel;
    @UiField
    FlowPanel selectedRulesPanel;
    @UiField
    VerticalPanel parseResultsPanel;
    @UiField
    MyStyle style;
    @UiField
    TextArea resultTextBox;
    @UiField
    Button runParsingButton;
    @UiField
    VerticalPanel codeMirrorPanel;

    @UiHandler("runParsingButton")
    void parseButton(ClickEvent event) {
        if (!checkSelectStatement()) {
            return;
        }
        String sqlStatement = codeEditor.getValue();
        String[] ruleNamesInOrder = getSelectedRules();
        presenter.runParseSQL(sqlStatement.trim(), ruleNamesInOrder);
    }

    private String[] getSelectedRules() {
        int count = selectedRulesPanel.getWidgetCount();
        List<String> tokens = new ArrayList<String>(count + 1);
        for (int i = 0; i < count; i++) {
            DebuggerSelectedRuleButton b = (DebuggerSelectedRuleButton) selectedRulesPanel.getWidget(i);
            tokens.add(b.getRuleName());
        }
        return tokens.toArray(new String[tokens.size()]);
    }

    interface MyStyle extends CssResource {

        @ClassName("rulesSelectedPanel-DebugCard")
        String rulesSelectedPanelDebugCard();

        @ClassName("paragraph-DebugCard")
        String paragraphDebugCard();

        @ClassName("resultTextBox-DebugCard")
        String resultTextBoxDebugCard();

        @ClassName("cardItem-DebugCard")
        String cardItemDebugCard();

        @ClassName("cardBody-DebugCard")
        String cardBodyDebugCard();

        @ClassName("cardTitle-DebugCard")
        String cardTitleDebugCard();

        @ClassName("rulesScrollPanel-DebugCard")
        String rulesScrollPanelDebugCard();

        @ClassName("runParsingButton-DebugCard")
        String runParsingButtonDebugCard();

        @ClassName("selectedRuleButton-DebugCard")
        String selectedRuleButtonDebugCard();

        @ClassName("ruleButton-DebugCard")
        String ruleButtonDebugCard();

        @ClassName("resultWrapper-DebugCard")
        String resultWrapperDebugCard();

        @ClassName("codeMirrorPanel-DebugCard")
        String codeMirrorPanelDebugCard();
    }
}