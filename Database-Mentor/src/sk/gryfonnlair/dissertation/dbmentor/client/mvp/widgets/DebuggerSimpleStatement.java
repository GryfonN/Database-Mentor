package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 21.04.14
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerSimpleStatement extends Composite implements HasClickHandlers {
    interface DebuggerSimpleStatementUiBinder extends UiBinder<HTMLPanel, DebuggerSimpleStatement> {
    }

    private static DebuggerSimpleStatementUiBinder ourUiBinder = GWT.create(DebuggerSimpleStatementUiBinder.class);
    private int id;
    private String command;

    public DebuggerSimpleStatement(final int id, String command) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.id = id;
        this.command = command;

        stmButton.setText(" " + command);
        stmButton.setStyleName("btn btn-block btn-lg btn-inverse");
        stmButton.addStyleName("fui-cmd");
        stmButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                stmResultPanel.clear();
                showProgressBar(true);
            }
        });

        loadingBar.setUrl(GWT.getModuleBaseURL() + "../" + "images/GryfProgressPopupImage_50.GIF");

    }

    public void showProgressBar(boolean isVisible) {
        loadingBar.setVisible(isVisible);
    }

    public void showResultTable(ResultSetTable resultSetTable) {
        stmResultPanel.clear();
        stmResultPanel.add(resultSetTable);
        showProgressBar(false);
    }

    public void showErrorMsg(String errorMsg) {
        stmResultPanel.clear();
        TextBox errorTextBox = new TextBox();
        errorTextBox.setText(errorMsg);
        errorTextBox.setStyleName(style.errorTextBoxDSS());
        stmResultPanel.add(errorTextBox);
        showProgressBar(false);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return stmButton.addClickHandler(handler);
    }

    @UiField
    Button stmButton;
    @UiField
    VerticalPanel stmResultPanel;
    @UiField
    Image loadingBar;
    @UiField
    MyStyle style;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    interface MyStyle extends CssResource {

        @ClassName("stmButton-DSS")
        String stmButtonDSS();

        @ClassName("progressBar-DSS")
        String progressBarDSS();

        @ClassName("stmMain-DSS")
        String stmMainDSS();

        @ClassName("stmResultPanel-DSS")
        String stmResultPanelDSS();

        @ClassName("errorTextBox-DSS")
        String errorTextBoxDSS();
    }
}