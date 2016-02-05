package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/1/14
 * Time: 8:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadViewImpl extends Composite implements UploadPresenter.UploadView {

    interface UploadViewImplUiBinder extends UiBinder<HTMLPanel, UploadViewImpl> {
    }

    private static UploadViewImplUiBinder ourUiBinder = GWT.create(UploadViewImplUiBinder.class);

    private UploadPresenter presenter;

    private boolean uploadDriverDone = false;
    private boolean uploadModuleDone = false;

    public UploadViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        uploadDriverProgressImage.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
        uploadModuleProgressImage.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");

        uploadBundleNameTextBox.getElement().setPropertyString("placeholder", "MySQL_5.1.28");
        uploadBundleClassNameTextBox.getElement().setPropertyString("placeholder", "sk.gryfonnlair.dissertation.dbmentor.bundle.DefaultMysqlConnectionLair");
    }

    @Override
    public void setPresenter(UploadPresenter uploadPresenter) {
        this.presenter = uploadPresenter;
    }

    @Override
    public void resetView() {
        uploadBundleNameTextBox.setText("");
        uploadDriverFileUpload.getElement().setPropertyString("value", "");
        uploadDriverPathLabel.setText("Choose driver JAR file please ...");
        uploadDriverProgressImage.setVisible(false);

        uploadModuleFileUpload.getElement().setPropertyString("value", "");
        uploadModulePathLabel.setText("Choose driver JAR file please ...");
        uploadModuleProgressImage.setVisible(false);

        uploadBundleClassNameTextBox.setText("");

        uploadResultTextBox.setVisible(false);

        uploadDriverDone = false;
        uploadDriverBrowseButton.setVisible(true);
        uploadDriverUploadButton.setVisible(true);
        uploadModuleDone = false;
        uploadModuleBrowseButton.setVisible(true);
        uploadModuleUploadButton.setVisible(true);

        uploadBundleUploadButton.setEnabled(true);

        //TODO FOR TEST
        uploadBundleNameTextBox.setText("MySQL_5.1.28");
        uploadBundleClassNameTextBox.setText("sk.gryfonnlair.dissertation.dbmentor.bundle.DefaultMysqlConnectionLair");
    }

    @Override
    public void setUploadDriverDone(boolean done) {
        uploadDriverDone = done;
        uploadDriverBrowseButton.setVisible(false);
        uploadDriverUploadButton.setVisible(false);
    }

    @Override
    public void setUploadModuleDone(boolean done) {
        uploadModuleDone = done;
        uploadModuleBrowseButton.setVisible(false);
        uploadModuleUploadButton.setVisible(false);
    }

    /**
     * nastavy style na TextBoxe a ak msg!=null tak ju napise inak success
     *
     * @param errorMsg ak NULL tak je Success, inak error msg show
     */
    @Override
    public void showUploadResult(String errorMsg) {
        uploadBundleUploadButton.setEnabled(true);
        showUploadProgressForBundle(false);

        uploadResultTextBox.removeStyleName(style.errorTextBoxUploadCard());
        uploadResultTextBox.removeStyleName(style.successTextBoxUploadCard());
        if (errorMsg == null) {//SUCCESS
            uploadResultTextBox.setText(UploadPresenter.MSG_RESULT_UPLOAD_SUCCESS);
            uploadResultTextBox.addStyleName(style.successTextBoxUploadCard());
            uploadBundleUploadButton.setEnabled(false);
        } else {
            RootPanel.get("error_div").add(new Label(errorMsg));
            uploadResultTextBox.setText(errorMsg);
            uploadResultTextBox.addStyleName(style.errorTextBoxUploadCard());
        }
        uploadResultTextBox.setVisible(true);
    }

    /**
     * hide button show progreess
     *
     * @param isProgressVisible
     */
    @Override
    public void showUploadProgressForBundle(boolean isProgressVisible) {
        uploadBundleUploadButton.setVisible(!isProgressVisible);
        if (isProgressVisible) {
            DatabaseMentor.gryfProgressPopupPanel.center();
        } else {
            DatabaseMentor.gryfProgressPopupPanel.hide();
        }
    }

    @Override
    public FormPanel getFormPanelForDriver() {
        return uploadDriverFormPanel;
    }

    @Override
    public FileUpload getFileUploadForDriver() {
        return uploadDriverFileUpload;
    }

    @Override
    public Button getSubmitButtonForDriver() {
        return uploadDriverUploadButton;
    }

    @Override
    public void showUploadProgressForDriver(boolean isProgressVisible) {
        uploadDriverBrowseButton.setEnabled(!isProgressVisible);
        uploadDriverUploadButton.setEnabled(!isProgressVisible);

        uploadDriverProgressImage.removeStyleName(style.progressBarVisibleUploadCard());
        uploadDriverProgressImage.removeStyleName(style.progressBarHiddenUploadCard());

        uploadDriverProgressImage.addStyleName(isProgressVisible ?
                style.progressBarVisibleUploadCard() : style.progressBarHiddenUploadCard());
    }

    @Override
    public FormPanel getFormPanelForModule() {
        return uploadModuleFormPanel;
    }

    @Override
    public FileUpload getFileUploadForModule() {
        return uploadModuleFileUpload;
    }

    @Override
    public Button getSubmitButtonForModule() {
        return uploadModuleUploadButton;
    }

    @Override
    public void showUploadProgressForModule(boolean isProgressVisible) {
        uploadModuleBrowseButton.setEnabled(!isProgressVisible);
        uploadModuleUploadButton.setEnabled(!isProgressVisible);

        uploadModuleProgressImage.removeStyleName(style.progressBarVisibleUploadCard());
        uploadModuleProgressImage.removeStyleName(style.progressBarHiddenUploadCard());

        uploadModuleProgressImage.addStyleName(isProgressVisible ?
                style.progressBarVisibleUploadCard() : style.progressBarHiddenUploadCard());
    }

    @UiField
    TextBox uploadBundleNameTextBox;

    @UiField
    FormPanel uploadDriverFormPanel;
    @UiField
    Button uploadDriverBrowseButton;
    @UiField
    FileUpload uploadDriverFileUpload;
    @UiField
    Label uploadDriverPathLabel;
    @UiField
    Button uploadDriverUploadButton;

    @UiField
    Image uploadDriverProgressImage;
    @UiField
    FormPanel uploadModuleFormPanel;
    @UiField
    FileUpload uploadModuleFileUpload;
    @UiField
    Button uploadModuleBrowseButton;
    @UiField
    Label uploadModulePathLabel;
    @UiField
    Button uploadModuleUploadButton;
    @UiField
    Image uploadModuleProgressImage;

    @UiField
    TextBox uploadBundleClassNameTextBox;

    @UiField
    Button uploadBundleUploadButton;
    @UiField
    TextBox uploadResultTextBox;
    @UiField
    MyStyle style;

    @UiHandler("uploadDriverBrowseButton")
    void clickUploadDriverBrowseButton(ClickEvent event) {
        uploadDriverFileUpload.getElement().<InputElement>cast().click();
    }

    @UiHandler(value = {"uploadDriverFileUpload"})
    void changeUploadDriverPathLabel(ChangeEvent event) {
        uploadDriverPathLabel.setText(uploadDriverFileUpload.getFilename());
    }

    @UiHandler("uploadModuleBrowseButton")
    void clickUploadModuleBrowseButton(ClickEvent event) {
        uploadModuleFileUpload.getElement().<InputElement>cast().click();
    }

    @UiHandler(value = {"uploadModuleFileUpload"})
    void changeUploadModulePathLabel(ChangeEvent event) {
        uploadModulePathLabel.setText(uploadModuleFileUpload.getFilename());
    }

    @UiHandler("uploadBundleUploadButton")
    void clickUploadBundleUploadButton(ClickEvent event) {
        uploadBundleUploadButton.setEnabled(false);
        if (checkBundleFormValid()) {
            uploadResultTextBox.setVisible(false);
            showUploadProgressForBundle(true);
            presenter.uploadBundle(uploadBundleNameTextBox.getText(), uploadBundleClassNameTextBox.getText());
        }
    }

    private boolean checkBundleFormValid() {
        if (uploadBundleNameTextBox.getText().isEmpty()) {
            showUploadResult(UploadPresenter.MSG_NO_BUNDLE_NAME);
        } else if (!uploadDriverDone) {
            showUploadResult(UploadPresenter.MSG_NO_FILE_DRIVER);
        } else if (!uploadModuleDone) {
            showUploadResult(UploadPresenter.MSG_NO_FILE_MODULE);
        } else if (uploadBundleClassNameTextBox.getText().isEmpty()) {
            showUploadResult(UploadPresenter.MSG_NO_MCL_CLASS_FULL_NAME);
        } else {
            return true;
        }
        return false;
    }

    interface MyStyle extends CssResource {
        @ClassName("progressBar-UploadCard")
        String progressBarUploadCard();

        @ClassName("inlineButton-UploadCard")
        String inlineButtonUploadCard();

        @ClassName("uploadDiv-UploadCard")
        String uploadDivUploadCard();

        @ClassName("button-UploadCard")
        String buttonUploadCard();

        @ClassName("uploadFilePath-UploadCard")
        String uploadFilePathUploadCard();

        @ClassName("cardBody-UploadCard")
        String cardBodyUploadCard();

        @ClassName("cardTitle-UploadCard")
        String cardTitleUploadCard();

        @ClassName("cardLabel-UploadCard")
        String cardLabelUploadCard();

        @ClassName("errorTextBox-UploadCard")
        String errorTextBoxUploadCard();

        @ClassName("resultTextBox-UploadCard")
        String resultTextBoxUploadCard();

        @ClassName("successTextBox-UploadCard")
        String successTextBoxUploadCard();

        @ClassName("progressBarVisible-UploadCard")
        String progressBarVisibleUploadCard();

        @ClassName("progressBarHidden-UploadCard")
        String progressBarHiddenUploadCard();

        @ClassName("uploadButton-UploadCard")
        String uploadButtonUploadCard();

        @ClassName("downloadButton-UploadCard")
        String downloadButtonUploadCard();

        @ClassName("downloadButtonLink-UploadCard")
        String downloadButtonLinkUploadCard();
    }
}