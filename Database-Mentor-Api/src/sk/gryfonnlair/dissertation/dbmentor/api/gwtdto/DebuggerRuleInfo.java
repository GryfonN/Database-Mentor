package sk.gryfonnlair.dissertation.dbmentor.api.gwtdto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 4/13/14
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerRuleInfo implements Serializable {

    private static final long serialVersionUID = 8135414576567145363L;

    /**
     * Meno pre switch case zaroven na serveri
     */
    private String name;
    /**
     * Keci co sa s nim robi pre UI
     */
    private String description;

    public DebuggerRuleInfo() {
    }

    public DebuggerRuleInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
