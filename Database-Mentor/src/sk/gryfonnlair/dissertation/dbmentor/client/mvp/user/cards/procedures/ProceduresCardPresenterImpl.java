package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.ProceduresServiceAsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class ProceduresCardPresenterImpl implements ProceduresCardPresenter {

    private final ProceduresCardView view;
    private final ProceduresServiceAsync proceduresServiceAsync;

    @Inject
    public ProceduresCardPresenterImpl(ProceduresCardView proceduresCardView, ProceduresServiceAsync proceduresServiceAsync) {
        this.view = proceduresCardView;
        this.proceduresServiceAsync = proceduresServiceAsync;
        bind();
    }

    @Override
    public void bind() {
        view.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        view.clearProcedureList();
        getProcedureList();
    }

    @Override
    public void getProcedureList() {
        DatabaseMentor.gryfProgressPopupPanel.center();
        proceduresServiceAsync.getAllProceduresNames(
                new AsyncCallback<String[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("ProceduresCardPresenterImpl.getProcedureList > onFailure" + caught.getMessage()));
                        view.generateProcedureList(new String[]{});
                        DatabaseMentor.gryfProgressPopupPanel.hide();
                        GWT.log("ProceduresCardPresenterImpl.getProcedureList > onFailure");
                    }

                    @Override
                    public void onSuccess(String[] result) {
                        view.generateProcedureList(result);
                        DatabaseMentor.gryfProgressPopupPanel.hide();
                        GWT.log("ProceduresCardPresenterImpl.getProcedureList > onSuccess\");");
                    }
                });
    }

    @Override
    public void getProcedureDetails(String procedureName) {
        proceduresServiceAsync.getProcedureDetails(procedureName,
                new AsyncCallback<Map<String, String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("ProceduresCardPresenterImpl.getProcedureDetails > onFailure EXP: " + caught.getMessage()));
                        view.fillDetailList(new HashMap<String, String>(0));
                        GWT.log("ProceduresCardPresenterImpl.getProcedureDetails > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Map<String, String> result) {
                        view.fillDetailList(result);
                        GWT.log("ProceduresCardPresenterImpl.getProcedureDetails > onSuccess");
                    }
                });
    }

    @Override
    public void getProcedureCode(String procedureName) {
        proceduresServiceAsync.getProcedureCode(procedureName,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("ProceduresCardPresenterImpl.getProcedureCode > onFailure EXP: " + caught.getMessage()));
                        view.fillCodeArea("");
                        GWT.log("ProceduresCardPresenterImpl.getProcedureCode > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.fillCodeArea(result);
                        GWT.log("ProceduresCardPresenterImpl.getProcedureCode > onSuccess");
                    }
                });
    }

    @Override
    public void getProcedureArguments(String procedureName) {
        proceduresServiceAsync.getProcedureArguments(procedureName,
                new AsyncCallback<List<ProcedureArgInfo>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("ProceduresCardPresenterImpl.getProcedureArguments > onFailure EXP: " + caught.getMessage()));
                        view.fillRunForm(null);
                        GWT.log("ProceduresCardPresenterImpl.getProcedureArguments > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<ProcedureArgInfo> result) {
                        view.fillRunForm(result);
                        GWT.log("ProceduresCardPresenterImpl.getProcedureArguments > onSuccess");
                    }
                });
    }

    @Override
    public void callProcedure(String procedureName, List<ProcedureArgInfo> arguments) {
        StringBuilder sb = new StringBuilder("ProceduresCardPresenterImpl.callProcedure > volam proceduru: " + procedureName + " s argumentami ");
        for (ProcedureArgInfo i : arguments) {
            sb.append(i.getName()).append('=').append(i.getValue());
        }
        GWT.log(sb.toString());

        proceduresServiceAsync.callProcedure(procedureName, arguments.toArray(new ProcedureArgInfo[arguments.size()]),
                new AsyncCallback<ProcedureCallResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("ProceduresCardPresenterImpl.callProcedure > onFailure EXP: " + caught.getMessage()));
                        view.showRunFormResult(null);
                        GWT.log("ProceduresCardPresenterImpl.callProcedure > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(ProcedureCallResult result) {
                        view.showRunFormResult(result);
                        GWT.log("ProceduresCardPresenterImpl.callProcedure > onSuccess");
                    }
                });
    }
}
