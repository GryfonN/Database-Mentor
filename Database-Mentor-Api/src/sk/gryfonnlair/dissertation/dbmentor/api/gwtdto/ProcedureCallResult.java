package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/10/14
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcedureCallResult implements Serializable {

    private static final long serialVersionUID = -833872912205453802L;
    private Map<String, String> arguments_out = new LinkedHashMap<String, String>(0);
    private Map<String, String> arguments_inout = new LinkedHashMap<String, String>(0);
    private List<MCLResultSetTable> resultSetTableList = new ArrayList<MCLResultSetTable>(0);

    public ProcedureCallResult() {
    }

    public Map<String, String> getArguments_out() {
        return arguments_out;
    }

    public void setArguments_out(Map<String, String> arguments_out) {
        this.arguments_out = arguments_out;
    }

    public Map<String, String> getArguments_inout() {
        return arguments_inout;
    }

    public void setArguments_inout(Map<String, String> arguments_inout) {
        this.arguments_inout = arguments_inout;
    }

    public List<MCLResultSetTable> getResultSetTableList() {
        return resultSetTableList;
    }

    public void setResultSetTableList(List<MCLResultSetTable> resultSetTableList) {
        this.resultSetTableList = resultSetTableList;
    }
}
