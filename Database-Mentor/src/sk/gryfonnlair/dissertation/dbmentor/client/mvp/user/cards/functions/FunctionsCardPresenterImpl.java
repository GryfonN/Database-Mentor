package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.FunctionsServiceAsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class FunctionsCardPresenterImpl implements FunctionsCardPresenter {

    private final FunctionsCardView view;
    private final FunctionsServiceAsync functionsServiceAsync;

    @Inject
    public FunctionsCardPresenterImpl(FunctionsCardView functionsCardView, FunctionsServiceAsync functionsServiceAsync) {
        view = functionsCardView;
        this.functionsServiceAsync = functionsServiceAsync;
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
        view.clearFunctionList();
        getFunctionList();
    }

    @Override
    public void getFunctionList() {
        DatabaseMentor.gryfProgressPopupPanel.center();
        functionsServiceAsync.getAllFunctionsNames(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("error_div").add(new Label("FunctionsCardPresenterImpl.getFunctionArguments > onFailure EXP: " + caught.getMessage()));
                view.generateFunctionList(new String[]{});
                DatabaseMentor.gryfProgressPopupPanel.hide();
                GWT.log("FunctionsCardPresenterImpl.getFunctionList > onFailure EXP:" + caught.getMessage());
            }

            @Override
            public void onSuccess(String[] result) {
                view.generateFunctionList(result);
                DatabaseMentor.gryfProgressPopupPanel.hide();
                GWT.log("FunctionsCardPresenterImpl.getFunctionList > onSuccess");
            }
        });
    }

    @Override
    public void getFunctionDetails(String functionName) {
        functionsServiceAsync.getFunctionDetails(functionName, new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("error_div").add(new Label("FunctionsCardPresenterImpl.getFunctionArguments > onFailure EXP: " + caught.getMessage()));
                view.fillDetailList(new HashMap<String, String>(0));
                GWT.log("FunctionsCardPresenterImpl.getFunctionDetails > onFailure EXP:" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                view.fillDetailList(result);
                GWT.log("FunctionsCardPresenterImpl.getFunctionDetails > onSuccess");
            }
        });
    }

    @Override
    public void getFunctionCode(String functionName) {
        functionsServiceAsync.getFunctionCode(functionName,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("FunctionsCardPresenterImpl.getFunctionArguments > onFailure EXP: " + caught.getMessage()));
                        view.fillCodeArea("");
                        GWT.log("FunctionsCardPresenterImpl.getFunctionCode > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.fillCodeArea(result);
                        GWT.log("FunctionsCardPresenterImpl.getFunctionCode > onSuccess");
                    }
                });
    }

    @Override
    public void getFunctionArguments(String functionName) {
        functionsServiceAsync.getFunctionArguments(functionName,
                new AsyncCallback<List<FunctionArgInfo>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("FunctionsCardPresenterImpl.getFunctionArguments > onFailure EXP: " + caught.getMessage()));
                        view.fillRunForm(null);
                        GWT.log("FunctionsCardPresenterImpl.getFunctionArguments > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<FunctionArgInfo> result) {
                        view.fillRunForm(result);
                        GWT.log("FunctionsCardPresenterImpl.getFunctionArguments > onSuccess");
                    }
                });
    }

    @Override
    public void callFunction(String functionName, List<FunctionArgInfo> arguments) {
        StringBuilder sb = new StringBuilder("FunctionsCardPresenterImpl.callFunction > volam funkciu: " + functionName + " s argumentami ");
        for (FunctionArgInfo i : arguments) {
            sb.append(i.getName()).append('=').append(i.getValue());
        }
        GWT.log(sb.toString());

        functionsServiceAsync.callFunction(functionName, arguments.toArray(new FunctionArgInfo[arguments.size()]),
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("error_div").add(new Label("FunctionsCardPresenterImpl.callFunction > onFailure EXP: " + caught.getMessage()));
                        view.showRunFormResult("ERROR");
                        GWT.log("FunctionsCardPresenterImpl.callFunction > onFailure EXP: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.showRunFormResult(result);
                        GWT.log("FunctionsCardPresenterImpl.callFunction > onSuccess");
                    }
                });
    }
}
