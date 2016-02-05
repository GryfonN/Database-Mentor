package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.UploadServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadPresenterImpl implements UploadPresenter {

    private final UploadView view;
    private final UploadServiceAsync uploadServiceAsync;

    @Inject
    public UploadPresenterImpl(UploadView view, UploadServiceAsync uploadServiceAsync) {
        this.view = view;
        this.uploadServiceAsync = uploadServiceAsync;
        bind();

        setupUploadFormForDriver();
        setupUploadFormForModule();
    }

    @Override
    public void uploadBundle(String bundleName, String mclClassFullName) {
        uploadServiceAsync.saveUploadBundleForm(bundleName, mclClassFullName,
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("UploadPresenterImpl.uploadBundle > onFailure");
                        view.showUploadResult(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        GWT.log("UploadPresenterImpl.uploadBundle > onSuccess");
                        view.showUploadResult(null);
                    }
                });
    }

    @Override
    public void setupUploadFormForDriver() {
        view.getSubmitButtonForDriver().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getFormPanelForDriver().submit();
            }
        });

        view.getFormPanelForDriver().setAction(GWT.getModuleBaseURL() + SERVICE_PATH_UPLOAD_BUNDLE);
        view.getFormPanelForDriver().setEncoding(FormPanel.ENCODING_MULTIPART);
        view.getFormPanelForDriver().setMethod(FormPanel.METHOD_POST);
        view.getFormPanelForDriver().addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                if ((view.getFileUploadForDriver().getFilename().length() < 1) ||
                        !view.getFileUploadForDriver().getFilename().endsWith(".jar")) {
                    Window.alert(MSG_NO_FILE_DRIVER);
                    event.cancel();
                } else {
                    view.showUploadProgressForDriver(true);
                }
            }
        });
        view.getFormPanelForDriver().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                view.setUploadDriverDone(true);
                view.showUploadProgressForDriver(false);
            }
        });
    }

    @Override
    public void setupUploadFormForModule() {
        view.getSubmitButtonForModule().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getFormPanelForModule().submit();
            }
        });

        view.getFormPanelForModule().setAction(GWT.getModuleBaseURL() + SERVICE_PATH_UPLOAD_BUNDLE);
        view.getFormPanelForModule().setEncoding(FormPanel.ENCODING_MULTIPART);
        view.getFormPanelForModule().setMethod(FormPanel.METHOD_POST);
        view.getFormPanelForModule().addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                if ((view.getFileUploadForModule().getFilename().length() < 1) ||
                        (!view.getFileUploadForModule().getFilename().endsWith(".jar"))) {
                    Window.alert(MSG_NO_FILE_MODULE);
                    event.cancel();
                } else {
                    view.showUploadProgressForModule(true);
                }
            }
        });
        view.getFormPanelForModule().addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                view.setUploadModuleDone(true);
                view.showUploadProgressForModule(false);
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
    }
}
