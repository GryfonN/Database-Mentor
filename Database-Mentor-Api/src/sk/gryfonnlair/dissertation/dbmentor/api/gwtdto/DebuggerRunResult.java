package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/13/14
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerRunResult implements Serializable {

    private static final long serialVersionUID = -5836842365073895186L;

    private int widgetId;
    private String executeTime;
    private MCLResultSetTable table;

    public DebuggerRunResult() {
    }

    public DebuggerRunResult(int widgetId, String executeTime, MCLResultSetTable table) {
        this.widgetId = widgetId;
        this.executeTime = executeTime;
        this.table = table;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public MCLResultSetTable getTable() {
        return table;
    }

    public void setTable(MCLResultSetTable table) {
        this.table = table;
    }
}
