package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/7/14
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class FunctionArgInfo implements Serializable {

    private static final long serialVersionUID = -3629712759272622937L;
    //pre spetnu odozvu
    private int position;
    private String value;
    //pre formular
    private String name;
    private int type;
    private String dataTypeName;

    //pre spetne vykonanie
    private int dataTypeForSQL;
    private boolean nullEnable;

    public FunctionArgInfo() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    /**
     * argType == 1 ? "IN" : argType == 2 ? "INOUT" : argType == 3 ? "OUT" : argType == 4 ? "OUT" : "??"
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public int getDataTypeForSQL() {
        return dataTypeForSQL;
    }

    public void setDataTypeForSQL(int dataTypeForSQL) {
        this.dataTypeForSQL = dataTypeForSQL;
    }

    public boolean isNullEnable() {
        return nullEnable;
    }

    public void setNullEnable(boolean nullEnable) {
        this.nullEnable = nullEnable;
    }
}
