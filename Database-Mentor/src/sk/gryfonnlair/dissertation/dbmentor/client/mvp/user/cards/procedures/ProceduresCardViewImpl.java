package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.procedures;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.MCLResultSetTable;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureCallResult;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.ProcedureRunTextBox;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.ResultSetTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class ProceduresCardViewImpl extends Composite implements ProceduresCardPresenter.ProceduresCardView {
    interface ProceduresCardViewImplUiBinder extends UiBinder<Widget, ProceduresCardViewImpl> {

    }

    private static ProceduresCardViewImplUiBinder ourUiBinder = GWT.create(ProceduresCardViewImplUiBinder.class);

    private ProceduresCardPresenter presenter;

    //pre detail
    private String procedureName;
    private List<ProcedureRunTextBox> runFormTextBoxes = new ArrayList<ProcedureRunTextBox>(0);


    public ProceduresCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        imageCode.setUrl(GWT.getModuleBaseURL() + "../" + "images/LineProgressPopupImage.GIF");
        imageRunForm.setUrl(GWT.getModuleBaseURL() + "../" + "images/LineProgressPopupImage.GIF");
    }

    @Override
    public void clearProcedureList() {
        this.procedureList.clear();
    }

    @Override
    public void generateProcedureList(String[] arrayOfProcedures) {
        clearProcedureList();
        showList(true);
        if (arrayOfProcedures != null && arrayOfProcedures.length > 0) {
            for (final String name : arrayOfProcedures) {
                final Button button = new Button(name);
                button.setStyleName(style.procedureListButtonProcedureCard());
                button.addStyleName("btn");
                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        showList(false);
                        procedureName = name;
                        presenter.getProcedureDetails(name);
                        presenter.getProcedureCode(name);
                        presenter.getProcedureArguments(name);
                    }
                });
                this.procedureList.add(button);
            }
        } else {
            this.procedureList.add(new Label("Nemate ziadne procedury v databaze"));
        }
    }

    @Override
    public void fillDetailList(Map<String, String> procedureDetailInfo) {
        form.clear();
        if (procedureDetailInfo != null && !procedureDetailInfo.isEmpty()) {
            for (Map.Entry<String, String> entry : procedureDetailInfo.entrySet()) {
                final Label key = new Label(entry.getKey());
                key.setStyleName(style.procedureDetailLabel());
                form.add(key);
                final TextBox value = new TextBox();
                value.setText(entry.getValue());
                value.setReadOnly(true);
                form.add(value);
            }
        } else {
            final Label l = new Label("Nepodarilo sa mi ziskat ziadne informacie o procedure");
            l.setStyleName(style.procedureDetailLabel());
            form.add(l);
        }
        imageForm.setVisible(false);
        form.setVisible(true);
    }

    @Override
    public void fillCodeArea(String procedureCode) {
        if (procedureCode != null && !(procedureCode.length() < 1)) {
            code.setText(procedureCode);
        } else {
            code.setText("Nepodarilo sa mi ziskat kod procedury");
        }
        imageCode.setVisible(false);
        code.setVisible(true);
    }

    @Override
    public void fillRunForm(List<ProcedureArgInfo> list) {
        runForm.clear();
        runFormTextBoxes.clear();
        if (list != null) {

            //pridany je az dole
            final Button runButton = new Button("Execute");
            runButton.setStyleName("btn btn-block btn-lg btn-primary");
            runButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (checkRunFormTextBoxes()) {
                        List<ProcedureArgInfo> list = new ArrayList<ProcedureArgInfo>(0);
                        for (ProcedureRunTextBox b : runFormTextBoxes) {
                            list.add(b.getProcedureArgInfo());
                        }
                        presenter.callProcedure(procedureName, list);
                        imageRunForm.setVisible(true);
                        runFormResult.setVisible(false);
                    }
                }
            });

            for (ProcedureArgInfo i : list) {
                HorizontalPanel panel = new HorizontalPanel();
                panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                runForm.add(panel);
                int argType = i.getType();
                panel.add(new Label(
                        (argType == 1 ? "IN" : argType == 2 ? "INOUT" : argType == 3 ? "OUT" : argType == 4 ? "OUT" : "??") + " "));
                panel.add(new Label(i.getName()));
                ProcedureRunTextBox t = new ProcedureRunTextBox(i);
                t.addValueChangeHandler(new ValueChangeHandler<String>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<String> event) {
                        if (checkRunFormTextBoxes()) {
                            runButton.setEnabled(true);
                        } else {
                            runButton.setEnabled(true);
                        }
                    }
                });
                panel.add(t);
                runFormTextBoxes.add(t);
            }

            runForm.add(runButton);
        } else {
            Label l = new Label("Nepodarilo sa mi vygenerovat formular pre proceduru");
            l.setStyleName(style.procedureDetailLabel());
            runForm.add(l);
        }
        imageRunForm.setVisible(false);
        runForm.setVisible(true);
    }

    @Override
    public void showRunFormResult(ProcedureCallResult result) {
        runFormResult.clear();
        FlowPanel wrapper = new FlowPanel();
        wrapper.setStyleName(style.resultWrapperProcedureCard());
        if (result == null) {
            wrapper.add(new Label("Error in procedure"));
        } else {
            wrapper.add(new Label("Procedure has success"));

//            wrapper.add(new Label("RETURN:"));
//            Label resultLabel = new Label(result);
//            resultLabel.setStyleName(style.resultFunctionsCard());
//            wrapper.add(resultLabel);


            //INOUT
            if (!result.getArguments_inout().isEmpty()) {
                wrapper.add(new Label("INOUT parameters:"));
            }
            for (Map.Entry<String, String> entry : result.getArguments_inout().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Label varLabel = new Label("@" + key + "=" + value);
                varLabel.setStyleName(style.resultProcedureCard());
                wrapper.add(varLabel);
            }
            //OUT
            if (!result.getArguments_out().isEmpty()) wrapper.add(new Label("OUT parameters:"));
            for (Map.Entry<String, String> entry : result.getArguments_out().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Label varLabel = new Label("@" + key + "=" + value);
                varLabel.setStyleName(style.resultProcedureCard());
                wrapper.add(varLabel);
            }
            List<MCLResultSetTable> resultSetTableList = result.getResultSetTableList();
            for (int i = 0; i < resultSetTableList.size(); i++) {
                MCLResultSetTable table = resultSetTableList.get(i);
                ResultSetTable tab = new ResultSetTable(String.valueOf(i + 1) + ". table:", table);
                wrapper.add(tab);
            }
        }
        runFormResult.add(wrapper);
        imageRunForm.setVisible(false);
        runFormResult.setVisible(true);
    }

    @Override
    public void setPresenter(ProceduresCardPresenter proceduresCardPresenter) {
        this.presenter = proceduresCardPresenter;
    }

    private void showList(boolean listVisible) {
        ProceduresCardViewImpl.this.procedureList.setVisible(listVisible);
        ProceduresCardViewImpl.this.procedureDetailList.setVisible(!listVisible);
        if (!listVisible) {//ak idem spet na list tak resetnem
            resetDetailForm();
        }
    }

    private void resetDetailForm() {
        imageForm.setVisible(true);
        form.setVisible(false);
        imageCode.setVisible(true);
        code.setVisible(false);
        imageRunForm.setVisible(true);
        runForm.setVisible(false);
        runFormResult.setVisible(false);
    }

    /**
     * @return false ak jeden ivalidny
     */
    private boolean checkRunFormTextBoxes() {
        for (ProcedureRunTextBox b : runFormTextBoxes) {
            if (!b.isValid()) return false;
        }
        return true;
    }

    @UiField
    VerticalPanel procedureList;

    @UiField
    VerticalPanel procedureDetailList;
    @UiField
    VerticalPanel form;
    @UiField
    Image imageForm;

    @UiField
    Image imageCode;
    @UiField
    TextArea code;

    @UiField
    Image imageRunForm;
    @UiField
    VerticalPanel runForm;
    @UiField
    VerticalPanel runFormResult;

    @UiField
    MyStyle style;

    @UiHandler("backToListButton")
    void backToListButtonClick(ClickEvent event) {
        showList(true);
    }

    interface MyStyle extends CssResource {

        @ClassName("procedure-detail-label")
        String procedureDetailLabel();

        @ClassName("lock-textarea")
        String lockTextarea();

        @ClassName("procedure-textarea")
        String procedureTextarea();

        @ClassName("cardItem-ProcedureCard")
        String cardItemProcedureCard();

        @ClassName("cardTitle-ProcedureCard")
        String cardTitleProcedureCard();

        @ClassName("cardBody-ProcedureCard")
        String cardBodyProcedureCard();

        @ClassName("resultWrapper-ProcedureCard")
        String resultWrapperProcedureCard();

        @ClassName("procedureRunForm-ProcedureCard")
        String procedureRunFormProcedureCard();

        @ClassName("result-ProcedureCard")
        String resultProcedureCard();

        @ClassName("procedureListButton-ProcedureCard")
        String procedureListButtonProcedureCard();

        @ClassName("procedureRunButton-ProcedureCard")
        String procedureRunButtonProcedureCard();
    }
}