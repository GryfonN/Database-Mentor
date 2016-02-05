package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.FunctionArgInfo;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/7/14
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionRunTextBox extends TextBox {

    private final FunctionArgInfo functionArgInfo;

    public FunctionRunTextBox(FunctionArgInfo functionArgInfo) {
        this.functionArgInfo = functionArgInfo;
        this.getElement().setPropertyString("placeholder", functionArgInfo.getDataTypeName());
        this.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                FunctionRunTextBox.this.functionArgInfo.setValue(getText());
            }
        });
    }

    public FunctionArgInfo getFunctionArgInfo() {
        return functionArgInfo;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        functionArgInfo.setValue(text);
    }

    public boolean isValid() {
        return functionArgInfo.getValue() != null && !functionArgInfo.getValue().isEmpty();
    }
}
