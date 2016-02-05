package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.quick;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.mastergaurav.codemirror.client.CodeEditor;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.MCLResultSetTable;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.ResultSetTable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class QuickCardViewImpl extends Composite implements QuickCardPresenter.QuickCardView {

    public static final String MSG_CHOOSE_FILE = "Choose SQL script file please ...";
    public static final String MSG_RESULT_NULL = "Null Result !!";
    public static final String MSG_SUCCESS = "Success";

    interface QuickCardViewImplUiBinder extends UiBinder<HTMLPanel, QuickCardViewImpl> {
    }

    private static QuickCardViewImplUiBinder ourUiBinder = GWT.create(QuickCardViewImplUiBinder.class);

    private QuickCardPresenter presenter;
    private CodeEditor codeEditor;

    public QuickCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        uploadProgress.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
        loadingBar.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");
    }

    @Override
    public void setPresenter(QuickCardPresenter quickCardPresenter) {
        this.presenter = quickCardPresenter;
    }

    @Override
    public void resetView() {
        uploadFile.getElement().setPropertyString("value", "");
        uploadFilePath.setText(MSG_CHOOSE_FILE);
        uploadProgress.setVisible(false);

        resultStatusTextBox.setVisible(false);

        //presetovanie textu neslo a duplikovalo codemirror
        codeEditor = new CodeEditor(300);
        codeMirrorPanel.clear();
        codeMirrorPanel.add(codeEditor);
        loadingBar.setVisible(false);
        quickCodeResult.clear();
        quickCodeResult.setVisible(false);
    }

    @Override
    public void showCodeResult(QuickCodeCallResult result) {
        loadingBar.setVisible(false);
        quickCodeResult.clear();
        quickCodeResult.setVisible(true);

        resultStatusTextBox.setVisible(true);
        resultStatusTextBox.removeStyleName(style.errorTextBoxQuickCard());
        resultStatusTextBox.removeStyleName(style.successTextBoxQuickCard());
        resultStatusTextBox.addStyleName(style.successTextBoxQuickCard());

        if (result == null) {
            resultStatusTextBox.setText(MSG_RESULT_NULL);
        } else {
            resultStatusTextBox.setText(MSG_SUCCESS);
            List<MCLResultSetTable> resultSetTableList = result.getResultSetTableList();
            for (int i = 0; i < resultSetTableList.size(); i++) {
                MCLResultSetTable table = resultSetTableList.get(i);
                ResultSetTable tab = new ResultSetTable(String.valueOf(i + 1) + ". result", table);
                FlowPanel wrapper = new FlowPanel();
                wrapper.add(tab);
                wrapper.setStyleName(style.resultWrapperQuickCard());
                quickCodeResult.add(wrapper);
            }
        }
    }

    @Override
    public void showCodeSQLError(String errorMsg) {
        RootPanel.get("error_div").add(new Label(errorMsg));
        loadingBar.setVisible(false);
        quickCodeResult.clear();

        resultStatusTextBox.setVisible(true);
        resultStatusTextBox.setText(errorMsg);
        resultStatusTextBox.removeStyleName(style.errorTextBoxQuickCard());
        resultStatusTextBox.removeStyleName(style.successTextBoxQuickCard());
        resultStatusTextBox.addStyleName(style.errorTextBoxQuickCard());
    }

    @Override
    public FormPanel getFormPanelForUploadSQLScript() {
        return uploadFormPanel;
    }

    @Override
    public FileUpload getFileUploadForUploadSQLScript() {
        return uploadFile;
    }

    @Override
    public Button getSubmitButtonForUploadSQLScript() {
        return uploadButton;
    }

    @Override
    public void setUploadedSQLScript(String sqlCode) {
        codeEditor.setValue(sqlCode);
    }

    @Override
    public void showUploadProgress(boolean isVisible) {
        uploadFlowPanel.setVisible(!isVisible);
        uploadProgress.setVisible(isVisible);
    }

    @UiField
    Image loadingBar;
    @UiField
    VerticalPanel quickCodeResult;
    @UiField
    Button uploadButton;
    @UiField
    FileUpload uploadFile;
    @UiField
    FormPanel uploadFormPanel;
    @UiField
    Button uploadBrowseButton;
    @UiField
    Label uploadFilePath;
    @UiField
    Image uploadProgress;
    @UiField
    FlowPanel uploadFlowPanel;
    @UiField
    TextArea resultStatusTextBox;
    @UiField
    MyStyle style;
    @UiField
    VerticalPanel codeMirrorPanel;

    @UiHandler("runButton")
    void runSQLCode(ClickEvent event) {
        resultStatusTextBox.setVisible(false);
        presenter.runSQLCode(codeEditor.getValue());
        loadingBar.setVisible(true);
        quickCodeResult.setVisible(false);
    }

    @UiHandler("uploadBrowseButton")
    void clickUploadBrowseButton(ClickEvent event) {
        uploadFile.getElement().<InputElement>cast().click();
    }

    @UiHandler(value = {"uploadFile"})
    void showFileBrowser(ChangeEvent event) {
        uploadFilePath.setText(uploadFile.getFilename());
    }

    @UiHandler("uploadFormPanel")
    void reset(FormPanel.SubmitCompleteEvent e) {
        uploadFile.getElement().setPropertyString("value", "");
        uploadFilePath.setText(MSG_CHOOSE_FILE);
    }

    interface MyStyle extends CssResource {

        @ClassName("uploadFlowPanel-QuickCard")
        String uploadFlowPanelQuickCard();

        @ClassName("progressBar-QuickCard")
        String progressBarQuickCard();

        @ClassName("button-QuickCard")
        String buttonQuickCard();

        @ClassName("inlineButton-QuickCard")
        String inlineButtonQuickCard();

        @ClassName("cardTitle-QuickCard")
        String cardTitleQuickCard();

        @ClassName("errorTextBox-QuickCard")
        String errorTextBoxQuickCard();

        @ClassName("uploadFilePath-QuickCard")
        String uploadFilePathQuickCard();

        @ClassName("successTextBox-QuickCard")
        String successTextBoxQuickCard();

        @ClassName("cardBody-QuickCard")
        String cardBodyQuickCard();

        @ClassName("resultTextBox-QuickCard")
        String resultTextBoxQuickCard();

        @ClassName("runButton-QuickCard")
        String runButtonQuickCard();

        @ClassName("cardItem-QuickCard")
        String cardItemQuickCard();

        @ClassName("resultWrapper-QuickCard")
        String resultWrapperQuickCard();

        @ClassName("codeMirrorPanel-QuickCard")
        String codeMirrorPanelQuickCard();
    }
}