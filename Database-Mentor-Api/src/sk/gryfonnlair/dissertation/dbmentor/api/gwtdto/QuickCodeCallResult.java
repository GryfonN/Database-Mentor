package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/11/14
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickCodeCallResult implements Serializable {

    private static final long serialVersionUID = -998761832236334003L;
    private List<MCLResultSetTable> resultSetTableList = new ArrayList<MCLResultSetTable>(0);

    public QuickCodeCallResult() {
    }

    public List<MCLResultSetTable> getResultSetTableList() {
        return resultSetTableList;
    }

    public void setResultSetTableList(List<MCLResultSetTable> resultSetTableList) {
        this.resultSetTableList = resultSetTableList;
    }
}
