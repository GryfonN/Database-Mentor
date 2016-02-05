package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.QuickServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class QuickCardPresenterImpl implements QuickCardPresenter {

    private QuickCardView view;
    private final QuickServiceAsync quickServiceAsync;

    @Inject
    public QuickCardPresenterImpl(QuickCardView quickCardView, QuickServiceAsync quickServiceAsync) {
        this.view = quickCardView;
        this.quickServiceAsync = quickServiceAsync;
        bind();

        setupUploadFormForUploadSQLScript();
    }

    @Override
    public void runSQLCode(String code) {
        quickServiceAsync.callSQLStatement(code,
                new AsyncCallback<QuickCodeCallResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("QuickCardPresenterImpl.runSQLCode > onFailure EXP v resulte");
                        view.showCodeSQLError(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(QuickCodeCallResult result) {
                        GWT.log("QuickCardPresenterImpl.runSQLCode > onSuccess");
                        view.showCodeResult(result);
                    }
                });
    }

    /**
     * Vola sa v konstruktore presentera. Nastavi formular na upload sql script fileu
     */
    @Override
    public void setupUploadFormForUploadSQLScript() {
        view.getSubmitButtonForUploadSQLScript().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getFormPanelForUploadSQLScript().submit();
            }
        });

        view.getFormPanelForUploadSQLScript().setAction(GWT.getModuleBaseURL() + SERVICE_PATH_UPLOAD_SQL_SCRIPT);
        view.getFormPanelForUploadSQLScript().setEncoding(FormPanel.ENCODING_MULTIPART);
        view.getFormPanelForUploadSQLScript().setMethod(FormPanel.METHOD_POST);
        view.getFormPanelForUploadSQLScript().addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                if (view.getFileUploadForUploadSQLScript().getFilename().length() < 1) {
                    Window.alert(MSG_NO_FILE);
                    event.cancel();
                } else if (!view.getFileUploadForUploadSQLScript().getFilename().endsWith(".sql")) {
                    Window.alert(MSG_NO_SQL_FILE);
                    event.cancel();
                } else {
                    view.showUploadProgress(true);
                }
            }
        });
        view.getFormPanelForUploadSQLScript().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                getUploadedSQLScriptFromSession();
            }
        });
    }

    @Override
    public void getUploadedSQLScriptFromSession() {
        quickServiceAsync.getUploadedSQLScript(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("error_div").add(new Label("Nepodarilo sa mi zistak upload File content" + caught.getMessage()));
                GWT.log("QuickCardPresenterImpl.getUploadedSQLScript > onFailure");
                view.setUploadedSQLScript("Nepodarilo sa mi zistak upload File content" + caught.getMessage());
                view.showUploadProgress(false);
            }

            @Override
            public void onSuccess(String result) {
                GWT.log("QuickCardPresenterImpl.getUploadedSQLScript > onSuccess");
                view.setUploadedSQLScript(result);
                view.showUploadProgress(false);
            }
        });
    }

    @Override
    public void bind() {
        view.setPresenter(this);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        view.resetView();
//        getSyntax();
    }
}
