package sk.gryfonnlair.dissertation.dbmentor.client.mvp.user.cards.functions;

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
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;
import sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets.FunctionRunTextBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class FunctionsCardViewImpl extends Composite implements FunctionsCardPresenter.FunctionsCardView {
    interface FunctionsCardViewImplUiBinder extends UiBinder<Widget, FunctionsCardViewImpl> {

    }

    private static FunctionsCardViewImplUiBinder ourUiBinder = GWT.create(FunctionsCardViewImplUiBinder.class);

    private FunctionsCardPresenter presenter;

    //pre detail
    private String functionName;
    private List<FunctionRunTextBox> runFormTextBoxes = new ArrayList<FunctionRunTextBox>(0);

    public FunctionsCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        imageCode.setUrl(GWT.getModuleBaseURL() + "../" + "images/LineProgressPopupImage.GIF");
        imageRunForm.setUrl(GWT.getModuleBaseURL() + "../" + "images/LineProgressPopupImage.GIF");
    }

    @Override
    public void clearFunctionList() {
        this.functionList.clear();
    }

    @Override
    public void generateFunctionList(String[] arrayOfFunctions) {
        clearFunctionList();
        showList(true);
        if (arrayOfFunctions != null && arrayOfFunctions.length > 0) {
            for (final String name : arrayOfFunctions) {
                final Button button = new Button(name);
                button.setStyleName(style.functionListButtonFunctionCard());
                button.addStyleName("btn");
                button.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        showList(false);
                        functionName = name;
                        presenter.getFunctionDetails(name);
                        presenter.getFunctionCode(name);
                        presenter.getFunctionArguments(name);
                    }
                });
                this.functionList.add(button);
            }
        } else {
            this.functionList.add(new Label("Nemate ziadne funkcie v databaze"));
        }
    }

    @Override
    public void fillDetailList(Map<String, String> functionDetailInfoMap) {
        form.clear();
        if (functionDetailInfoMap != null && !functionDetailInfoMap.isEmpty()) {
            for (Map.Entry<String, String> entry : functionDetailInfoMap.entrySet()) {
                final Label key = new Label(entry.getKey());
                key.setStyleName(style.functionDetailLabel());
                form.add(key);
                final TextBox value = new TextBox();
                value.setText(entry.getValue());
                value.setReadOnly(true);
                form.add(value);
            }
        } else {
            final Label l = new Label("Nepodarilo sami ziskat ziadne informacie o funckii");
            l.setStyleName(style.functionDetailLabel());
            form.add(l);
        }

        imageForm.setVisible(false);
        form.setVisible(true);
    }

    @Override
    public void fillCodeArea(String functionCode) {
        if (functionCode != null && !(functionCode.length() < 1)) {
            code.setText(functionCode);
        } else {
            code.setText("Nepodarilo sa mi ziskat kod funkcie");
        }
        imageCode.setVisible(false);
        code.setVisible(true);
    }

    @Override
    public void fillRunForm(List<FunctionArgInfo> list) {
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
                        List<FunctionArgInfo> list = new ArrayList<FunctionArgInfo>(0);
                        for (FunctionRunTextBox b : runFormTextBoxes) {
                            list.add(b.getFunctionArgInfo());
                        }
                        presenter.callFunction(functionName, list);
                        imageRunForm.setVisible(true);
                        runFormResult.setVisible(false);
                    }
                }
            });

            for (FunctionArgInfo i : list) {
                HorizontalPanel panel = new HorizontalPanel();
                panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                runForm.add(panel);
                int argType = i.getType();
                panel.add(new Label(
                        (argType == 1 ? "IN" : argType == 2 ? "INOUT" : argType == 3 ? "OUT" : argType == 4 ? "RETURN" : "??") + " "));
                panel.add(new Label(i.getName()));
                if (i.getType() == 4) {//ak return tak sa spravy special textBOx
                    FunctionRunTextBox t = new FunctionRunTextBox(i) {
                        @Override
                        public boolean isValid() {
                            return true;
                        }
                    };
                    t.setReadOnly(true);
                    panel.add(t);
                    runFormTextBoxes.add(t);
                } else {
                    FunctionRunTextBox t = new FunctionRunTextBox(i);
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
            }

            runForm.add(runButton);
        } else {
            Label l = new Label("Nepodarilo sa mi vygenerovat formular pre funkciu");
            l.setStyleName(style.functionDetailLabel());
            runForm.add(l);
        }
        imageRunForm.setVisible(false);
        runForm.setVisible(true);
    }

    @Override
    public void showRunFormResult(String result) {
        runFormResult.clear();
        FlowPanel wrapper = new FlowPanel();
        wrapper.setStyleName(style.resultWrapperFunctionsCard());
        if ("ERROR".equalsIgnoreCase(result)) {
            wrapper.add(new Label("Error in function"));
        } else {
            wrapper.add(new Label("Function has success"));
            //RETURN
            wrapper.add(new Label("RETURN:"));
            Label resultLabel = new Label(result);
            resultLabel.setStyleName(style.resultFunctionsCard());
            wrapper.add(resultLabel);
        }
        runFormResult.add(wrapper);
        imageRunForm.setVisible(false);
        runFormResult.setVisible(true);
    }

    @Override
    public void setPresenter(FunctionsCardPresenter functionsCardPresenter) {
        this.presenter = functionsCardPresenter;
    }

    private void showList(boolean listVisible) {
        FunctionsCardViewImpl.this.functionList.setVisible(listVisible);
        FunctionsCardViewImpl.this.functionDetailList.setVisible(!listVisible);
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
        for (FunctionRunTextBox b : runFormTextBoxes) {
            if (!b.isValid()) return false;
        }
        return true;
    }

    @UiField
    VerticalPanel functionList;

    @UiField
    VerticalPanel functionDetailList;
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

        @ClassName("function-detail-label")
        String functionDetailLabel();

        @ClassName("lock-textarea")
        String lockTextarea();

        @ClassName("function-textarea")
        String functionTextarea();

        @ClassName("cardItem-FunctionCard")
        String cardItemFunctionCard();

        @ClassName("cardTitle-FunctionCard")
        String cardTitleFunctionCard();

        @ClassName("cardBody-FunctionCard")
        String cardBodyFunctionCard();

        @ClassName("functionListButton-FunctionCard")
        String functionListButtonFunctionCard();

        @ClassName("functionRunButton-FunctionsCard")
        String functionRunButtonFunctionsCard();

        @ClassName("functionRunForm-FunctionsCard")
        String functionRunFormFunctionsCard();

        @ClassName("resultWrapper-FunctionsCard")
        String resultWrapperFunctionsCard();

        @ClassName("result-FunctionsCard")
        String resultFunctionsCard();
    }
}