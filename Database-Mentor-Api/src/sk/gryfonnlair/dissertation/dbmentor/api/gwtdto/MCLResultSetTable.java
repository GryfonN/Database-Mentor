package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/8/14
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class MCLResultSetTable implements Serializable {

    private static final long serialVersionUID = 5973864266986328387L;
    /**
     * KEY mapy nazov stlpca, arraylist hodnoty v riadkoch
     */
    private Map<String, List<String>> table = new LinkedHashMap<String, List<String>>(0);

    public MCLResultSetTable() {
    }

    public Map<String, List<String>> getTable() {
        return table;
    }

    public void setTable(Map<String, List<String>> table) {
        this.table = table;
    }
}
