package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.ProcedureArgInfo;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/5/14
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcedureRunTextBox extends TextBox {

    private final ProcedureArgInfo procedureArgInfo;

    public ProcedureRunTextBox(ProcedureArgInfo procedureArgInfo) {
        super();
        this.procedureArgInfo = procedureArgInfo;
        this.getElement().setPropertyString("placeholder", procedureArgInfo.getDataTypeName());
        this.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                ProcedureRunTextBox.this.procedureArgInfo.setValue(getText());
            }
        });
    }

    public ProcedureArgInfo getProcedureArgInfo() {
        return procedureArgInfo;
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        procedureArgInfo.setValue(text);
    }

    public boolean isValid() {
        return procedureArgInfo.getValue() != null && !procedureArgInfo.getValue().isEmpty();
    }
}
