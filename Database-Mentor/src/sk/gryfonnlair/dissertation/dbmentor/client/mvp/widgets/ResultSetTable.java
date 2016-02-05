package sk.gryfonnlair.dissertation.dbmentor.client.mvp.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.MCLResultSetTable;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/11/14
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultSetTable extends Composite {

    public static final String MSG_EMPTY_RESULT = "Empty result";

    interface ResultSetTableUiBinder extends UiBinder<Widget, ResultSetTable> {
    }

    private static ResultSetTableUiBinder ourUiBinder = GWT.create(ResultSetTableUiBinder.class);

    public ResultSetTable(String resultSetTitle, MCLResultSetTable mclResultSetTable) {
        resultSetLabel = new Label(resultSetTitle);
        initWidget(ourUiBinder.createAndBindUi(this));

        if (mclResultSetTable.getTable().isEmpty()) {
            rootFlowPanel.add(new Label(MSG_EMPTY_RESULT));
        } else {
            int gridRows = getRowsCount(mclResultSetTable) + 1;//for headers
            int gridColumns = mclResultSetTable.getTable().size();
            Grid grid = new Grid(gridRows, gridColumns);
            grid.setBorderWidth(2);
            setupColumnsHeaders(grid, mclResultSetTable);
            setupRows(grid, mclResultSetTable);
            rootFlowPanel.add(grid);
        }
    }

    private void setupColumnsHeaders(Grid grid, MCLResultSetTable mclResultSetTable) {
        int columnPosition = 0;
        for (Map.Entry<String, List<String>> entry : mclResultSetTable.getTable().entrySet()) {
            String key = entry.getKey();
            TextBox b = new TextBox();
            b.setReadOnly(true);
            b.setWidth("150px");
            b.setText(key);
            b.setTitle(key);
            grid.setWidget(0, columnPosition++, b);
        }
    }

    private void setupRows(Grid grid, MCLResultSetTable mclResultSetTable) {
        int columnPosition = 0;
        for (Map.Entry<String, List<String>> entry : mclResultSetTable.getTable().entrySet()) {
            List<String> value1 = entry.getValue();
            for (int i = 0; i < value1.size(); i++) {
                String value = value1.get(i);
                TextBox b = new TextBox();
                b.setWidth("150px");
                b.setText(value);
                b.setTitle(value);
                grid.setWidget(i + 1, columnPosition, b);
            }
            columnPosition++;
        }
    }

    private int getRowsCount(MCLResultSetTable mclResultSetTable) {
        for (Map.Entry<String, List<String>> entry : mclResultSetTable.getTable().entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            GWT.log("ResultSetTablee.getRowsCount > tabulka ma " + value.size() + " riadkov.");
            return value.size();
        }
        return 0;
    }

    @UiField(provided = true)
    Label resultSetLabel;
    @UiField
    FlowPanel rootFlowPanel;

    interface MyStyle extends CssResource {

        @ClassName("resultsettable-scrollpanel")
        String resultsettableScrollpanel();
    }
}