package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.debugger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRuleInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.DebuggerRunResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseRequest;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.DebuggerParseResponse;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.DebuggerServiceAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/24/14
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugCardPresenterImpl implements DebugCardPresenter {

    public static final String MSG_SERVER_COULD_NOT_GET_DEBUGGER_RULES_EXP = "Server could not get debugger rules EXP:";
    public static final String MSG_NO_RULES_ON_SERVER_FOR_DEBUGGING = "No rules on server for debugging";
    public static final String MSG_SERVER_COULD_NOT_PARSE = "Server could not parse sql statement";
    private DebugCardView view;
    private DebuggerServiceAsync debuggerServiceAsync;

    @Inject
    public DebugCardPresenterImpl(DebugCardView view, DebuggerServiceAsync debuggerServiceAsync) {
        this.view = view;
        this.debuggerServiceAsync = debuggerServiceAsync;
        bind();
    }

    /**
     * Z servera get pole pravidiel
     */
    @Override
    public void getDebuggerRuleInfos() {
        view.showParseProgress(true);
        debuggerServiceAsync.getActiveRules(new AsyncCallback<DebuggerRuleInfo[]>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("DebugCardPresenterImpl.getDebuggerRuleInfos > onFailure");
                view.showParseProgress(false);
                view.showCodeSQLError(MSG_SERVER_COULD_NOT_GET_DEBUGGER_RULES_EXP + caught.getMessage());
                view.enableRunButton(false);
            }

            @Override
            public void onSuccess(DebuggerRuleInfo[] result) {
                GWT.log("DebugCardPresenterImpl.getDebuggerRuleInfos > onSuccess");
                view.showParseProgress(false);
                if (result.length < 1) {
                    view.showCodeSQLError(MSG_NO_RULES_ON_SERVER_FOR_DEBUGGING);
                    view.enableRunButton(false);
                } else {
                    view.showCodeSQLError(null);
                    view.setupDebuggerRuleInfos(result);
                    view.enableRunButton(true);
                }
            }
        });
    }

    /**
     * volam server parse, a disable button, takze potom ho musim zapnut
     *
     * @param sqlFromTextArea uz osetreny text !
     */
    @Override
    public void runParseSQL(String sqlFromTextArea, String[] ruleNameInRightOrder) {
        view.showParseProgress(true);
        view.enableRunButton(false);
        List<DebuggerRuleInfo> listForRequest = new ArrayList<DebuggerRuleInfo>();
        for (String s : ruleNameInRightOrder) {
            listForRequest.add(new DebuggerRuleInfo(s, ""));
        }
        DebuggerParseRequest request = new DebuggerParseRequest(sqlFromTextArea, listForRequest.toArray(new DebuggerRuleInfo[listForRequest.size()]));
        debuggerServiceAsync.parseSqlStatement(request, new AsyncCallback<DebuggerParseResponse>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("DebugCardPresenterImpl.runParseSQL > onFailure");
                RootPanel.get("error_div").add(new Label("DebugCardPresenterImpl.runParseSQL > onFailure EXP:" + caught.getMessage()));
                view.showParseProgress(false);
                view.enableRunButton(true);
                view.showCodeSQLError(MSG_SERVER_COULD_NOT_PARSE + caught.getMessage());
            }

            @Override
            public void onSuccess(DebuggerParseResponse result) {
                GWT.log("DebugCardPresenterImpl.runParseSQL > onSuccess");
                view.showParseProgress(false);
                view.enableRunButton(true);
                view.showCodeSQLError(null);
                view.setupPostParseList(result);
            }
        });
    }

    /**
     * Call jednoducheho sql selektu aj id prikladam pre response si ulozim
     * v anonym ibjekte GWT callback objekte
     *
     * @param idWidget     id widgetu kde vykreslit response
     * @param sqlStatement sql co vykonat, z widgetu vytiahnem
     */
    @Override
    public void runSimpleSQLStatement(final int idWidget, final String sqlStatement) {
        debuggerServiceAsync.runSimpleSelectSqlStatement(idWidget, sqlStatement, new AsyncCallback<DebuggerRunResult>() {
            @Override
            public void onFailure(Throwable caught) {
                view.setupSimpleResultFor(idWidget, null, caught.getMessage());
            }

            @Override
            public void onSuccess(DebuggerRunResult result) {
                view.setupSimpleResultFor(result.getWidgetId(), result, null);
            }
        });
    }

    /**
     * V presentery v tejto metode setujem viewvu presenter, vzajomna vezba, volat v konstruktore presentera
     */
    @Override
    public void bind() {
        view.setPresenter(this);
    }

    /**
     * @param container HasWidgets kde vlozim svoj presenter/view, povinne riadky prve clear a potom add as widget
     */
    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        view.resetView();
        getDebuggerRuleInfos();
    }
}
