package sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import sk.gryfonnlair.dissertation.dbmentor.client.DatabaseMentor;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.admin.cards.upload.UploadPresenter;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 08.04.14
 * Time: 09:48
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationViewImpl extends Composite implements ConfigurationPresenter.ConfigurationView {
    interface ConfigurationViewImplUiBinder extends UiBinder<HTMLPanel, ConfigurationViewImpl> {
    }

    private static ConfigurationViewImplUiBinder ourUiBinder = GWT.create(ConfigurationViewImplUiBinder.class);

    private ConfigurationPresenter presenter;

    public ConfigurationViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(ConfigurationPresenter configurationPresenter) {
        this.presenter = configurationPresenter;
    }

    @Override
    public void resetView() {
        userNameTextBox.setText("");
        userPassTextBox.setText("");
        urlTextBox.setText("");
        databaseTextBox.setText("");
        resultTextBox.setVisible(false);
        saveButton.setEnabled(true);
        saveButton.setVisible(true);
    }

    @Override
    public void fillFormular(String userName, String userPass, String url, String database) {
        userNameTextBox.setText(userName);
        userPassTextBox.setText(userPass);
        urlTextBox.setText(url);
        databaseTextBox.setText(database);
    }

    /**
     * nastavy style na TextBoxe a ak msg!=null tak ju napise inak success
     *
     * @param errorMsg ak NULL tak je Success, inak error msg show
     */
    @Override
    public void showSaveResult(String errorMsg) {
        saveButton.setEnabled(true);
        showSaveProgress(false);

        resultTextBox.removeStyleName(style.errorResultTextBoxConfigurationCard());
        resultTextBox.removeStyleName(style.successResultTextBoxConfigurationCard());

        if (errorMsg == null) {//SUCCESS
            resultTextBox.setText(UploadPresenter.MSG_RESULT_UPLOAD_SUCCESS);
            resultTextBox.addStyleName(style.successResultTextBoxConfigurationCard());
        } else {
            RootPanel.get("error_div").add(new Label(errorMsg));
            resultTextBox.setText(errorMsg);
            resultTextBox.addStyleName(style.errorResultTextBoxConfigurationCard());
        }
        resultTextBox.setVisible(true);
    }

    @Override
    public void showSaveProgress(boolean isProgressVisible) {
        if (isProgressVisible) {
            DatabaseMentor.gryfProgressPopupPanel.center();
        } else {
            DatabaseMentor.gryfProgressPopupPanel.hide();
        }
    }

    @Override
    public void setVisibleSaveButton(boolean visible) {
        saveButton.setVisible(visible);
    }

    @UiField
    TextBox userNameTextBox;
    @UiField
    TextBox userPassTextBox;
    @UiField
    TextBox urlTextBox;
    @UiField
    TextBox databaseTextBox;
    @UiField
    Button saveButton;
    @UiField
    TextBox resultTextBox;
    @UiField
    MyStyle style;

    @UiHandler("saveButton")
    void clickUploadBundleUploadButton(ClickEvent event) {
        saveButton.setEnabled(false);
        if (checkSaveFormValid()) {
            showSaveProgress(true);
            presenter.saveConfiguration(
                    userNameTextBox.getText(),
                    userPassTextBox.getText(),
                    urlTextBox.getText(),
                    databaseTextBox.getText());
        }

    }

    private boolean checkSaveFormValid() {
        if (userNameTextBox.getText().isEmpty()) {
            showSaveResult(MSG_NO_USER_NAME);
        } else if (userPassTextBox.getText().isEmpty()) {
            showSaveResult(MSG_NO_USER_PASS);
        } else if (urlTextBox.getText().isEmpty()) {
            showSaveResult(MSG_NO_URL);
        } else if (databaseTextBox.getText().isEmpty()) {
            showSaveResult(MSG_NO_DATABASE_NAME);
        } else {
            return true;
        }
        return false;
    }

    interface MyStyle extends CssResource {

        @ClassName("cardBody-ConfigurationCard")
        String cardBodyConfigurationCard();

        @ClassName("cardLabel-ConfigurationCard")
        String cardLabelConfigurationCard();

        @ClassName("cardTitle-ConfigurationCard")
        String cardTitleConfigurationCard();

        @ClassName("resultTextBox-ConfigurationCard")
        String resultTextBoxConfigurationCard();

        @ClassName("saveButton-ConfigurationCard")
        String saveButtonConfigurationCard();

        @ClassName("successResultTextBox-ConfigurationCard")
        String successResultTextBoxConfigurationCard();

        @ClassName("errorResultTextBox-ConfigurationCard")
        String errorResultTextBoxConfigurationCard();
    }
}